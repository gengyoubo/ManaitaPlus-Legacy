package sen.manaita_plus_legacy.core;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus_legacy.menu.ManaitaPlusLegacyBrewingStandMenu;
import sen.manaita_plus_legacy.menu.ManaitaPlusLegacyCraftingMenu;
import sen.manaita_plus_legacy.menu.ManaitaPlusLegacyFurnaceMenu;
import static sen.manaita_plus_legacy.ManaitaPlusLegacy.MENU_TYPES;

public class ManaitaPlusLegacyMenuCore {
    public static final RegistryObject<MenuType<ManaitaPlusLegacyCraftingMenu>> CraftingManaita = MENU_TYPES.register("manaita_crafting", () -> IForgeMenuType.create(ManaitaPlusLegacyCraftingMenu::new));
    public static final RegistryObject<MenuType<ManaitaPlusLegacyFurnaceMenu>> FurnaceManaita = MENU_TYPES.register("manaita_furnace", () -> IForgeMenuType.create(ManaitaPlusLegacyFurnaceMenu::new));
    public static final RegistryObject<MenuType<ManaitaPlusLegacyBrewingStandMenu>> BrewingStandManaita = MENU_TYPES.register("manaita_brewing", () -> IForgeMenuType.create(ManaitaPlusLegacyBrewingStandMenu::new));

    public static void init() {
    }

}
