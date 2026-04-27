package github.com.gengyoubo.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import github.com.gengyoubo.entity.MPGEntityArrow;
import github.com.gengyoubo.item.data.IMPDoubling;
import github.com.gengyoubo.item.data.IMPKey;
import github.com.gengyoubo.util.MPText;

import java.util.List;

public class MPBowItem extends Item implements IMPKey, IMPDoubling {
    private static final int CHARGE_TICKS = 10;
    private static final int RAPID_FIRE_INTERVAL = 2;

    public MPBowItem() {
        super(new Properties().defaultDurability(-1).fireResistant());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(heldItem);
    }

    @Override
    public void onUseTick(Level level, @NotNull LivingEntity livingEntity, @NotNull ItemStack stack, int remainingUseDuration) {
        if (level.isClientSide || !(livingEntity instanceof Player player)) {
            return;
        }

        int elapsed = getUseDuration(stack) - remainingUseDuration;
        if (elapsed < CHARGE_TICKS) {
            return;
        }
        if ((elapsed - CHARGE_TICKS) % RAPID_FIRE_INTERVAL != 0) {
            return;
        }

        shootArrow(level, player, stack, player.getUsedItemHand());
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.literal(MPText.manaita_mode.formatting(Component.translatable("mode.doubling").getString() + ":" + (isDoubling(stack) ? Component.translatable("info.on").getString() : Component.translatable("info.off").getString()))));
        tooltip.add(Component.literal(MPText.manaita_infinity.formatting(Component.translatable("info.attack").getString())));
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.literal(MPText.manaita_mode.formatting(Component.translatable("item.manaita_bow.name").getString()));
    }

    @Override
    public void onManaitaKeyPress(ItemStack itemStack) {
        toggleDoubling(itemStack);
    }

    @Override
    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {
        boolean doubling = toggleDoubling(itemStack);
        player.displayClientMessage(Component.literal(String.format("[%s%s] %s%s: %s", MPText.manaita_mode.formatting(Component.translatable("item.manaita_bow.name").getString()), ChatFormatting.RESET, ChatFormatting.RESET, Component.translatable("mode.doubling").getString(), (doubling ? Component.translatable("info.on").getString() : Component.translatable("info.off").getString()))), true);
    }

    private void shootArrow(Level level, Player player, ItemStack stack, InteractionHand hand) {
        AbstractArrow abstractArrow = MPGEntityArrow.create(level, player);
        abstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 10.0F, 1.0F);
        abstractArrow.setCritArrow(true);
        abstractArrow.setBaseDamage(isDoubling(stack) ? 40.0D : 20.0D);
        abstractArrow.setSilent(true);
        level.addFreshEntity(abstractArrow);
        stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
    }
}
