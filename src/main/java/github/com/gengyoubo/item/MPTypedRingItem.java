package github.com.gengyoubo.item;

import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import github.com.gengyoubo.item.data.IMPKey;
import github.com.gengyoubo.item.portable.MPPortableMenuOpener;
import github.com.gengyoubo.util.MPNBTData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class MPTypedRingItem extends TrinketItem implements IMPKey {
    private final String translationPrefix;
    private final RingKind ringKind;

    public MPTypedRingItem(String translationPrefix, RingKind ringKind) {
        super(new Properties().stacksTo(1).fireResistant());
        this.translationPrefix = translationPrefix;
        this.ringKind = ringKind;
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        return Component.translatable(
                translationPrefix + getRingTypeKey(stack.getOrCreateTag().getInt(MPNBTData.ItemType)) + "name");
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            return super.use(level, player, hand);
        }

        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            openWorkMenu(serverPlayer, stack);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void onManaitaKeyPress(ItemStack itemStack, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            openWorkMenu(serverPlayer, itemStack);
        }
    }

    public void openWorkMenu(ServerPlayer player, ItemStack stack) {
        switch (ringKind) {
            case CRAFTING -> MPPortableMenuOpener.openCrafting(player, stack, player.level());
            case FURNACE -> MPPortableMenuOpener.openFurnace(player, stack, player.level());
            case BREWING -> MPPortableMenuOpener.openBrewing(player, stack, player.level());
        }
    }

    public static Optional<ItemStack> findPrimaryEquippedRing(Player player) {
        return TrinketsApi.getTrinketComponent(player).flatMap(component -> {
            ItemStack handRing = findFirstRing(component, "hand", "ring");
            if (!handRing.isEmpty()) {
                return Optional.of(handRing);
            }
            ItemStack offhandRing = findFirstRing(component, "offhand", "ring");
            if (!offhandRing.isEmpty()) {
                return Optional.of(offhandRing);
            }
            return Optional.empty();
        });
    }

    private static ItemStack findFirstRing(TrinketComponent component, String groupName, String slotName) {
        Map<String, TrinketInventory> group = component.getInventory().get(groupName);
        if (group == null) {
            return ItemStack.EMPTY;
        }
        TrinketInventory inventory = group.get(slotName);
        if (inventory == null) {
            return ItemStack.EMPTY;
        }
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof MPTypedRingItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private static String getRingTypeKey(int type) {
        return switch (type) {
            case 1 -> "wooden.";
            case 2 -> "stone.";
            case 3 -> "iron.";
            case 4 -> "gold.";
            case 5 -> "emerald.";
            case 6 -> "diamond.";
            case 7 -> "redstone.";
            case 8 -> "netherite.";
            default -> "";
        };
    }

    public enum RingKind {
        CRAFTING,
        FURNACE,
        BREWING
    }
}
