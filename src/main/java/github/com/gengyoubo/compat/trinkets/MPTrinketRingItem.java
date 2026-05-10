package github.com.gengyoubo.compat.trinkets;

import dev.emi.trinkets.api.TrinketItem;
import github.com.gengyoubo.item.MPTypedRingItem;
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

public class MPTrinketRingItem extends TrinketItem implements IMPKey {
    private final String translationPrefix;
    private final MPTypedRingItem.RingKind ringKind;

    public MPTrinketRingItem(String translationPrefix, MPTypedRingItem.RingKind ringKind) {
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
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown()) {
            if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                openWorkMenu(serverPlayer, stack);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        return super.use(level, player, hand);
    }

    @Override
    public void onManaitaKeyPress(ItemStack itemStack, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            openWorkMenu(serverPlayer, itemStack);
        }
    }

    private void openWorkMenu(ServerPlayer player, ItemStack stack) {
        switch (ringKind) {
            case CRAFTING -> MPPortableMenuOpener.openCrafting(player, stack, player.level());
            case FURNACE -> MPPortableMenuOpener.openFurnace(player, stack, player.level());
            case BREWING -> MPPortableMenuOpener.openBrewing(player, stack, player.level());
        }
    }

    public static String getRingTypeKey(int type) {
        return getRingString(type);
    }

    @NotNull
    public static String getRingString(int type) {
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
}
