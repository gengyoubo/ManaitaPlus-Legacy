package github.com.gengyoubo.compat;

import github.com.gengyoubo.item.MPTypedRingItem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

public final class MPTrinketsCompat {
    public static final String MOD_ID = "trinkets";

    private MPTrinketsCompat() {
    }

    public static boolean isLoaded() {
        return FabricLoader.getInstance().isModLoaded(MOD_ID);
    }

    public static Item createRingItem(String translationPrefix, String ringKindName) {
        if (!isLoaded()) {
            return new Item(new Item.Properties().stacksTo(1).fireResistant());
        }

        MPTypedRingItem.RingKind ringKind = MPTypedRingItem.RingKind.valueOf(ringKindName);
        return new MPTypedRingItem(translationPrefix, ringKind);
    }

    public static boolean equipItem(Player player, ItemStack stack) {
        if (!isLoaded() || player == null || stack.isEmpty()) {
            return false;
        }

        try {
            Class<?> trinketItemClass = Class.forName("dev.emi.trinkets.api.TrinketItem");
            Method equipMethod = trinketItemClass.getMethod("equipItem", LivingEntity.class, ItemStack.class);
            return (boolean) equipMethod.invoke(null, player, stack);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to equip Trinkets item", e);
        }
    }

    public static Optional<ItemStack> findPrimaryEquippedRing(Player player) {
        if (!isLoaded() || player == null) {
            return Optional.empty();
        }

        try {
            Class<?> trinketsApiClass = Class.forName("dev.emi.trinkets.api.TrinketsApi");
            Method getTrinketComponent = trinketsApiClass.getMethod("getTrinketComponent", LivingEntity.class);
            Optional<?> component = (Optional<?>) getTrinketComponent.invoke(null, player);
            if (component.isEmpty()) {
                return Optional.empty();
            }

            Object trinketComponent = component.get();
            Method getInventory = trinketComponent.getClass().getMethod("getInventory");
            @SuppressWarnings("unchecked")
            Map<String, Map<String, ?>> inventories = (Map<String, Map<String, ?>>) getInventory.invoke(trinketComponent);

            ItemStack handRing = findFirstRing(inventories, "hand");
            if (!handRing.isEmpty()) {
                return Optional.of(handRing);
            }

            ItemStack offhandRing = findFirstRing(inventories, "offhand");
            if (!offhandRing.isEmpty()) {
                return Optional.of(offhandRing);
            }
            return Optional.empty();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to query Trinkets ring inventory", e);
        }
    }

    private static ItemStack findFirstRing(Map<String, Map<String, ?>> inventories, String groupName) {
        Map<String, ?> group = inventories.get(groupName);
        if (group == null) {
            return ItemStack.EMPTY;
        }

        Object inventory = group.get("ring");
        if (!(inventory instanceof Container container)) {
            return ItemStack.EMPTY;
        }

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof MPTypedRingItem) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }
}
