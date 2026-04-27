package github.com.gengyoubo.core;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegistryObject;
import github.com.gengyoubo.menu.MPGBrewingStandMenu;
import github.com.gengyoubo.menu.MPGCraftingMenu;
import github.com.gengyoubo.menu.MPGFurnaceMenu;
import static github.com.gengyoubo.MPG.MENU_TYPES;

public class MPGMenuCore {
    public static final RegistryObject<MenuType<MPGCraftingMenu>> CraftingManaita = MENU_TYPES.register("manaita_crafting", () -> IForgeMenuType.create(MPGCraftingMenu::new));
    public static final RegistryObject<MenuType<MPGFurnaceMenu>> FurnaceManaita = MENU_TYPES.register("manaita_furnace", () -> IForgeMenuType.create(MPGFurnaceMenu::new));
    public static final RegistryObject<MenuType<MPGBrewingStandMenu>> BrewingStandManaita = MENU_TYPES.register("manaita_brewing", () -> IForgeMenuType.create(MPGBrewingStandMenu::new));

    public static void init() {
    }

}
