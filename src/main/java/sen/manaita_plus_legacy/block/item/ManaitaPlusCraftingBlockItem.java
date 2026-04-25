package sen.manaita_plus_legacy.block.item;

import net.minecraft.world.item.Item;
import sen.manaita_plus_legacy.block.ManaitaPlusCraftingBlock;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyBlockCore;

public class ManaitaPlusCraftingBlockItem extends ManaitaPlusTypedBlockItem {
    public ManaitaPlusCraftingBlockItem() {
        super(ManaitaPlusLegacyBlockCore.CraftingBlock.get(), new Item.Properties().fireResistant(), "block.crafting.", ManaitaPlusCraftingBlock.class);
    }
}
