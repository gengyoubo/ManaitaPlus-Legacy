package sen.manaita_plus_legacy.core;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus_legacy.item.*;
import sen.manaita_plus_legacy.item.portable.ManaitaPlusLegacyBrewingPortable;
import sen.manaita_plus_legacy.item.portable.ManaitaPlusLegacyCraftingPortable;
import sen.manaita_plus_legacy.item.portable.ManaitaPlusLegacyFurnacePortable;

import static sen.manaita_plus_legacy.ManaitaPlusLegacy.ITEMS;

public class ManaitaPlusLegacyItemCore {
    public static final RegistryObject<Item> ManaitaSwordGod = ITEMS.register("manaita_sword_god", ManaitaPlusLegacyGodSwordItem::new);

    public static final RegistryObject<Item> ManaitaBow = ITEMS.register("manaita_bow", ManaitaPlusLegacyBowItem::new);


    public static final RegistryObject<Item> ManaitaSource = ITEMS.register("manaita_source", ManaitaPlusLegacySourceItem::new);

    public static final RegistryObject<Item> ManaitaCraftingPortable = ITEMS.register("manaita_crafting_portable", ManaitaPlusLegacyCraftingPortable::new);
    public static final RegistryObject<Item> ManaitaFurnacePortable = ITEMS.register("manaita_furnace_portable", ManaitaPlusLegacyFurnacePortable::new);
    public static final RegistryObject<Item> ManaitaBrewingPortable = ITEMS.register("manaita_brewing_portable", ManaitaPlusLegacyBrewingPortable::new);

    public static void init() {
    }

}

