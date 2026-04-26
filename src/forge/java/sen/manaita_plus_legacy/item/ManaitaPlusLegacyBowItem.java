package sen.manaita_plus_legacy.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.entity.ManaitaPlusLegacyEntityArrow;
import sen.manaita_plus_legacy.item.data.IManaitaPlusLegacyDoubling;
import sen.manaita_plus_legacy.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.util.ManaitaPlusText;

import java.util.List;

public class ManaitaPlusLegacyBowItem extends Item implements IManaitaPlusLegacyKey, IManaitaPlusLegacyDoubling {
    public ManaitaPlusLegacyBowItem() {
        super(new Properties().defaultDurability(-1).fireResistant());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide) {
            AbstractArrow abstractArrow = ManaitaPlusLegacyEntityArrow.create(level, player);
            abstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 10.0F, 1.0F);
            abstractArrow.setCritArrow(true);
            level.addFreshEntity(abstractArrow);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.doubling") + ":" + (isDoubling(stack) ? I18n.get("info.on") : I18n.get("info.off")))));
        tooltip.add(Component.literal(ManaitaPlusText.manaita_infinity.formatting(I18n.get("info.attack"))));
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("item.manaita_bow.name")));
    }

    @Override
    public void onManaitaKeyPress(ItemStack itemStack) {
        toggleDoubling(itemStack);
    }

    @Override
    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {
        boolean doubling = toggleDoubling(itemStack);
        player.displayClientMessage(Component.literal(String.format("[%s%s] %s%s: %s", ManaitaPlusText.manaita_mode.formatting(I18n.get("item.manaita_bow.name")), ChatFormatting.RESET, ChatFormatting.RESET, I18n.get("mode.doubling"), (doubling ? I18n.get("info.on") : I18n.get("info.off")))), true);
    }
}
