package sen.manaita_plus_legacy.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sen.manaita_plus_legacy.gui.BrewingStandScreen;
import sen.manaita_plus_legacy.gui.CraftingManaitaScreen;
import sen.manaita_plus_legacy.gui.FurnaceManaitaScreen;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyBlockCore;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyItemCore;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyMenuCore;
import sen.manaita_plus_legacy.menu.ManaitaPlusLegacyBrewingStandMenu;
import sen.manaita_plus_legacy.menu.ManaitaPlusLegacyCraftingMenu;
import sen.manaita_plus_legacy.menu.ManaitaPlusLegacyFurnaceMenu;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation("manaita_plus_legacy", "jei_plugin");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CraftingManaitaScreen.class, 88, 32, 28, 23, RecipeTypes.CRAFTING);
        registration.addRecipeClickArea(FurnaceManaitaScreen.class, 78, 32, 28, 23, RecipeTypes.SMELTING);
        registration.addRecipeClickArea(BrewingStandScreen.class, 97, 16, 14, 30, RecipeTypes.BREWING);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ManaitaPlusLegacyBlockCore.CraftingBlock.get()), RecipeTypes.CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(ManaitaPlusLegacyItemCore.ManaitaCraftingPortable.get()), RecipeTypes.CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(ManaitaPlusLegacyBlockCore.FurnaceBlock.get()), RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(new ItemStack(ManaitaPlusLegacyItemCore.ManaitaFurnacePortable.get()), RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(new ItemStack(ManaitaPlusLegacyBlockCore.BrewingBlock.get()), RecipeTypes.BREWING);
        registration.addRecipeCatalyst(new ItemStack(ManaitaPlusLegacyItemCore.ManaitaBrewingPortable.get()), RecipeTypes.BREWING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ManaitaPlusLegacyCraftingMenu.class, ManaitaPlusLegacyMenuCore.CraftingManaita.get(), RecipeTypes.CRAFTING, 1, 9, 10, 36);
        registration.addRecipeTransferHandler(ManaitaPlusLegacyFurnaceMenu.class, ManaitaPlusLegacyMenuCore.FurnaceManaita.get(), RecipeTypes.SMELTING, 0, 1, 3, 36);
        registration.addRecipeTransferHandler(ManaitaPlusLegacyBrewingStandMenu.class, ManaitaPlusLegacyMenuCore.BrewingStandManaita.get(), RecipeTypes.BREWING, 1, 3, 5, 36);
    }
}
