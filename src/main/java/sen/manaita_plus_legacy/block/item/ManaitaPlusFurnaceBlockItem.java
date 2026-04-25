package sen.manaita_plus_legacy.block.item;

import net.minecraft.world.item.Item;
import sen.manaita_plus_legacy.block.ManaitaPlusFurnaceBlock;

import static sen.manaita_plus_legacy.core.ManaitaPlusLegacyBlockCore.FurnaceBlock;

public class ManaitaPlusFurnaceBlockItem extends ManaitaPlusTypedBlockItem {
    public ManaitaPlusFurnaceBlockItem() {
        super(FurnaceBlock.get(), new Item.Properties().fireResistant(), "block.furnace.", ManaitaPlusFurnaceBlock.class);
    }
}
