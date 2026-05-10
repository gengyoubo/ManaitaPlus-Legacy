package github.com.gengyoubo.core;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import github.com.gengyoubo.menu.MPBrewingStandMenu;
import github.com.gengyoubo.menu.MPCraftingMenu;
import github.com.gengyoubo.menu.MPFurnaceMenu;
import static github.com.gengyoubo.MPG.register;

public class MPMenuCore {
    public static final MenuType<MPCraftingMenu> CraftingManaita =
            register(BuiltInRegistries.MENU, "manaita_crafting", new ExtendedScreenHandlerType<>(MPCraftingMenu::new));
    public static final MenuType<MPFurnaceMenu> FurnaceManaita =
            register(BuiltInRegistries.MENU, "manaita_furnace", new ExtendedScreenHandlerType<>(MPFurnaceMenu::new));
    public static final MenuType<MPBrewingStandMenu> BrewingStandManaita =
            register(BuiltInRegistries.MENU, "manaita_brewing", new ExtendedScreenHandlerType<>(MPBrewingStandMenu::new));
}

