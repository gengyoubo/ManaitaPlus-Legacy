package sen.manaita_plus_legacy.block.item;

import sen.manaita_plus_legacy.block.ManaitaPlusBrewingStandBlock;

import static sen.manaita_plus_legacy.core.ManaitaPlusLegacyBlockCore.BrewingBlock;

public class ManaitaPlusBrewingBlockItem extends ManaitaPlusTypedBlockItem {
    public ManaitaPlusBrewingBlockItem() {
        super(BrewingBlock.get(), new Properties().fireResistant(), "block.brewing.", ManaitaPlusBrewingStandBlock.class);
    }
}
