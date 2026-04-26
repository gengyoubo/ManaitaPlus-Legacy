package sen.manaita_plus_legacy.item.tool;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import sen.manaita_plus_legacy.item.tool.base.ManaitaPlusLegacyTaggedToolItem;

public class ManaitaPlusLegacyPickaxeItem extends ManaitaPlusLegacyTaggedToolItem {
    public ManaitaPlusLegacyPickaxeItem() {
        super(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }

}
