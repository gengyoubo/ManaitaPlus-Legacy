package github.com.gengyoubo.item.tool;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import github.com.gengyoubo.item.tool.base.MPToolActionHelper;
import github.com.gengyoubo.item.tool.base.MPToolBase;
import github.com.gengyoubo.util.ManaitaPlusLegacyEntityData;
import github.com.gengyoubo.util.MPText;

import java.util.List;

public class MPPaxelItem extends MPToolBase {
    public static final TagKey<Block> MINEABLE = BlockTags.create(new ResourceLocation("mineable"));

    public MPPaxelItem() {
        super(MINEABLE);
    }
    @Override
    public boolean isCorrectToolForDrops(@NotNull ItemStack stack, @NotNull BlockState state) {
        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        ManaitaPlusLegacyEntityData.death.add(entity);
        entity.hurt(entity.damageSources().playerAttack(player), 10000);
        if (entity instanceof LivingEntity living) {
            living.setHealth(0F);
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.empty());
        tooltip.add(Component.literal(MPText.manaita_infinity.formatting(Component.translatable("info.attack").getString())));
    }

    @Override
    public void inventoryTick(ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean selected) {
        stack.setPopTime(0);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        int range = getRange(context.getItemInHand()) >> 1;
        boolean changed = MPToolActionHelper.applyInRange(context, range, (pos, state) ->
                MPToolActionHelper.applyAxeActions(context, pos, state)
                        | MPToolActionHelper.applyGrowPlantAction(context, pos, state)
                        | MPToolActionHelper.applyShovelAction(context, pos, state));
        return changed ? InteractionResult.sidedSuccess(context.getLevel().isClientSide) : InteractionResult.PASS;
    }
}

