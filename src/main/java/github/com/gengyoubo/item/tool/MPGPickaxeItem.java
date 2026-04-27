package github.com.gengyoubo.item.tool;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import github.com.gengyoubo.item.tool.base.ManaitaPlusLegacyTaggedToolItem;

public class MPGPickaxeItem extends ManaitaPlusLegacyTaggedToolItem {
    public MPGPickaxeItem() {
        super(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }

}
