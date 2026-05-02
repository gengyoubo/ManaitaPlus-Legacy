package github.com.gengyoubo.MPG.item.ring;

import github.com.gengyoubo.MPG.item.data.IMPGKey;
import github.com.gengyoubo.MPG.item.portable.MPGPortableItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class MPGRingItem extends MPGPortableItem implements IMPGKey {
    protected MPGRingItem(String translationPrefix) {
        super(translationPrefix);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            if (level.isClientSide()) {
                return InteractionResultHolder.sidedSuccess(stack, true);
            }
            if (MPGCuriosHelper.tryEquipRingFromHand(player, hand)) {
                return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), false);
            }
            return InteractionResultHolder.pass(stack);
        }
        if (player instanceof ServerPlayer serverPlayer) {
            openPortableMenu(serverPlayer, stack, level);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void onManaitaKeyPress(ItemStack paramItemStack, Player paramEntityPlayer) {
        if (paramEntityPlayer instanceof ServerPlayer serverPlayer) {
            openPortableMenu(serverPlayer, paramItemStack, paramEntityPlayer.level());
        }
    }

    @Override
    public void onManaitaKeyPress(ItemStack paramItemStack) {
    }
}
