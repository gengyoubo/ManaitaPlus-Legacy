package sen.manaita_plus_legacy.item.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import sen.manaita_plus_legacy.item.tool.base.ManaitaPlusLegacyTaggedToolItem;
import sen.manaita_plus_legacy.item.tool.base.ManaitaPlusLegacyToolActionHelper;

public class ManaitaPlusLegacyShovelItem extends ManaitaPlusLegacyTaggedToolItem {
    public ManaitaPlusLegacyShovelItem() {
        super(BlockTags.MINEABLE_WITH_SHOVEL);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return ToolActions.DEFAULT_SHOVEL_ACTIONS.contains(toolAction);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (ManaitaPlusLegacyToolActionHelper.applyShovelAction(context, blockPos, blockState)) {
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
