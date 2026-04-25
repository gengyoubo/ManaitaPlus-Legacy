package sen.manaita_plus_legacy.item.tool.base;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.item.data.IManaitaPlusLegacyDestroy;
import sen.manaita_plus_legacy.item.data.IManaitaPlusLegacyDoubling;
import sen.manaita_plus_legacy.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.item.tier.ManaitaPlusLegacyToolTier;
import sen.manaita_plus_legacy.util.ManaitaPlusLegacyNBTData;
import sen.manaita_plus_legacy.util.ManaitaPlusText;
import sen.manaita_plus_legacy.util.ManaitaPlusUtils;

import java.util.List;
import java.util.Objects;

public class ManaitaPlusLegacyToolBase extends DiggerItem implements IManaitaPlusLegacyKey, IManaitaPlusLegacyDestroy, IManaitaPlusLegacyDoubling {
    public ManaitaPlusLegacyToolBase(TagKey<Block> tagKey) {
        super(Float.MAX_VALUE, Float.MAX_VALUE, new ManaitaPlusLegacyToolTier(), tagKey, new Properties().fireResistant());
    }

    @Override
    public boolean accept(BlockState state) {
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        ManaitaPlusLegacyToolActionHelper.appendRangeAndDoublingTooltip(tooltip, getRange(stack), isDoubling(stack));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        ManaitaPlusLegacyToolActionHelper.handleRangeOrEnchantmentUse(level, player, itemInHand, (getRange(itemInHand) + 2) % 21, range -> setRange(itemInHand, range, player));
        return InteractionResultHolder.pass(itemInHand);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level level, @NotNull BlockState state, @NotNull BlockPos blockPos, @NotNull LivingEntity livingEntity) {
        return true;
    }

    @Override
    public int getRange(ItemStack itemStack) {
        if (!itemStack.hasTag()) {
            return 1;
        }
        int range = Objects.requireNonNull(itemStack.getTag()).getInt(ManaitaPlusLegacyNBTData.Range);
        if (range == 0) {
            itemStack.getTag().putInt(ManaitaPlusLegacyNBTData.Range, 1);
            return 1;
        }
        return range;
    }

    public void setRange(ItemStack itemStack, int range) {
        setRange(itemStack, range, null);
    }

    public void setRange(ItemStack itemStack, int range, Player player) {
        if (range == 0) {
            range = 1;
        }
        itemStack.getOrCreateTag().putInt(ManaitaPlusLegacyNBTData.Range, range);
        if (player != null) {
            ManaitaPlusUtils.chat(player, Component.literal(ManaitaPlusText.manaita_mode.formatting(itemStack.getDisplayName().getString() + " " + I18n.get("mode.range.name") + ": " + range + "x" + range + "x" + range)));
        }
    }

    @Override
    public void onManaitaKeyPress(ItemStack itemStack) {
        toggleDoubling(itemStack);
    }

    @Override
    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {
        boolean doubling = toggleDoubling(itemStack);
        ManaitaPlusUtils.chat(player, Component.literal(ManaitaPlusText.manaita_mode.formatting(itemStack.getDisplayName().getString() + " " + I18n.get("mode.doubling") + ": " + (doubling ? I18n.get("info.on") : I18n.get("info.off")))));
    }

}
