package github.com.gengyoubo.block.item;

import github.com.gengyoubo.block.MPCraftingBlock;
import net.minecraft.world.item.Item;

import static github.com.gengyoubo.core.MPBlockCore.CraftingBlock;

public class MPCraftingBlockItem extends MPTypedBlockItem {
    public MPCraftingBlockItem() {
        super(CraftingBlock, new Item.Properties().fireResistant(), "block.crafting.", MPCraftingBlock.class);
    }
}
