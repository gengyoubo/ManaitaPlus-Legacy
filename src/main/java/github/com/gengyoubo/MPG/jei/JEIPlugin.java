package github.com.gengyoubo.MPG.jei;

import github.com.gengyoubo.MPG.core.MPGBlockCore;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.IRuntimeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import net.minecraftforge.fml.ModList;
import github.com.gengyoubo.MPG.MPGConfig;
import github.com.gengyoubo.MPG.block.item.MPBrewingBlockItem;
import github.com.gengyoubo.MPG.block.item.MPCraftingBlockItem;
import github.com.gengyoubo.MPG.block.item.MPFurnaceBlockItem;
import github.com.gengyoubo.MPG.block.item.MPHookBlockItem;
import github.com.gengyoubo.MPG.gui.BrewingStandScreen;
import github.com.gengyoubo.MPG.gui.CraftingManaitaScreen;
import github.com.gengyoubo.MPG.gui.FurnaceManaitaScreen;
import github.com.gengyoubo.MPG.core.MPGItemCore;
import github.com.gengyoubo.MPG.core.MPGMenuCore;
import github.com.gengyoubo.MPG.menu.MPGBrewingStandMenu;
import github.com.gengyoubo.MPG.menu.MPGCraftingMenu;
import github.com.gengyoubo.MPG.menu.MPGFurnaceMenu;
import github.com.gengyoubo.MPG.util.MPGNBTData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation("manaita_plus_general", "jei_plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        IIngredientSubtypeInterpreter<ItemStack> typeInterpreter = (stack, context) ->
                stack.hasTag() ? String.valueOf(stack.getTag().getInt(MPGNBTData.ItemType)) : IIngredientSubtypeInterpreter.NONE;
        registration.registerSubtypeInterpreter(MPGBlockCore.CraftingBlockItem.get(), typeInterpreter);
        registration.registerSubtypeInterpreter(MPGBlockCore.FurnaceBlockItem.get(), typeInterpreter);
        registration.registerSubtypeInterpreter(MPGBlockCore.BrewingBlockItem.get(), typeInterpreter);
        registration.registerSubtypeInterpreter(MPGBlockCore.HookBlockItem.get(), typeInterpreter);
        registration.registerSubtypeInterpreter(MPGItemCore.ManaitaCraftingPortable.get(), typeInterpreter);
        registration.registerSubtypeInterpreter(MPGItemCore.ManaitaFurnacePortable.get(), typeInterpreter);
        registration.registerSubtypeInterpreter(MPGItemCore.ManaitaBrewingPortable.get(), typeInterpreter);
        if (ModList.get().isLoaded("curios")) {
            registration.registerSubtypeInterpreter(MPGItemCore.ManaitaCraftingRing.get(), typeInterpreter);
            registration.registerSubtypeInterpreter(MPGItemCore.ManaitaFurnaceRing.get(), typeInterpreter);
            registration.registerSubtypeInterpreter(MPGItemCore.ManaitaBrewingRing.get(), typeInterpreter);
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SourceCopyRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CraftingManaitaScreen.class, 88, 32, 28, 23, RecipeTypes.CRAFTING);
        registration.addRecipeClickArea(FurnaceManaitaScreen.class, 78, 32, 28, 23, RecipeTypes.SMELTING);
        registration.addRecipeClickArea(BrewingStandScreen.class, 97, 16, 14, 30, RecipeTypes.BREWING);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(MPGBlockCore.CraftingBlock.get()), RecipeTypes.CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(MPGItemCore.ManaitaCraftingPortable.get()), RecipeTypes.CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(MPGItemCore.ManaitaSource.get()), SourceCopyRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(MPGBlockCore.FurnaceBlock.get()), RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(new ItemStack(MPGItemCore.ManaitaFurnacePortable.get()), RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(new ItemStack(MPGBlockCore.BrewingBlock.get()), RecipeTypes.BREWING);
        registration.addRecipeCatalyst(new ItemStack(MPGItemCore.ManaitaBrewingPortable.get()), RecipeTypes.BREWING);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(SourceCopyRecipeCategory.TYPE, createSourceCopyRecipes());
        registration.addItemStackInfo(
                new ItemStack(MPGItemCore.ManaitaSource.get()),
                Component.translatable("jei.manaita_plus_general.source.info.1"),
                Component.translatable("jei.manaita_plus_general.source.info.2", MPGConfig.source_doubling_value),
                Component.translatable("jei.manaita_plus_general.source.info.3"),
                Component.translatable("jei.manaita_plus_general.source.info.4", MPGConfig.source_doubling_value)
        );
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(MPGCraftingMenu.class, MPGMenuCore.CraftingManaita.get(), RecipeTypes.CRAFTING, 1, 9, 10, 36);
        registration.addRecipeTransferHandler(MPGFurnaceMenu.class, MPGMenuCore.FurnaceManaita.get(), RecipeTypes.SMELTING, 0, 1, 3, 36);
        registration.addRecipeTransferHandler(MPGBrewingStandMenu.class, MPGMenuCore.BrewingStandManaita.get(), RecipeTypes.BREWING, 1, 3, 5, 36);
    }

    private static List<SourceCopyJeiRecipe> createSourceCopyRecipes() {
        ItemStack source = new ItemStack(MPGItemCore.ManaitaSource.get());
        List<SourceCopyJeiRecipe> recipes = new ArrayList<>(BuiltInRegistries.ITEM.stream()
                .filter(JEIPlugin::isCopyableItem)
                .sorted(Comparator.comparing(item -> BuiltInRegistries.ITEM.getKey(item).toString()))
                .map(Item::getDefaultInstance)
                .filter(stack -> !stack.isEmpty())
                .map(input -> {
                    ItemStack normalizedInput = input.copy();
                    normalizedInput.setCount(1);
                    ItemStack output = normalizedInput.copy();
                    output.setCount(MPGConfig.source_doubling_value);
                    return new SourceCopyJeiRecipe(source.copy(), normalizedInput, output);
                })
                .toList());

        addTypedSourceRecipes(recipes, source, MPGBlockCore.CraftingBlockItem.get(), 8);
        addTypedSourceRecipes(recipes, source, MPGBlockCore.FurnaceBlockItem.get(), 8);
        addTypedSourceRecipes(recipes, source, MPGBlockCore.BrewingBlockItem.get(), 8);
        addTypedSourceRecipes(recipes, source, MPGBlockCore.HookBlockItem.get(), 7);
        addTypedSourceRecipes(recipes, source, MPGItemCore.ManaitaCraftingPortable.get(), 8);
        addTypedSourceRecipes(recipes, source, MPGItemCore.ManaitaFurnacePortable.get(), 8);
        addTypedSourceRecipes(recipes, source, MPGItemCore.ManaitaBrewingPortable.get(), 8);
        if (ModList.get().isLoaded("curios")) {
            addTypedSourceRecipes(recipes, source, MPGItemCore.ManaitaCraftingRing.get(), 8);
            addTypedSourceRecipes(recipes, source, MPGItemCore.ManaitaFurnaceRing.get(), 8);
            addTypedSourceRecipes(recipes, source, MPGItemCore.ManaitaBrewingRing.get(), 8);
        }
        return recipes;
    }

    @Override
    public void registerRuntime(IRuntimeRegistration registration) {
        if (ModList.get().isLoaded("curios")) {
            return;
        }
        registration.getIngredientManager().removeIngredientsAtRuntime(
                VanillaTypes.ITEM_STACK,
                List.of(
                        new ItemStack(MPGItemCore.ManaitaCraftingRing.get()),
                        new ItemStack(MPGItemCore.ManaitaFurnaceRing.get()),
                        new ItemStack(MPGItemCore.ManaitaBrewingRing.get())
                )
        );
    }

    private static boolean isCopyableItem(Item item) {
        if (item == Items.AIR || item == MPGItemCore.ManaitaSource.get()) {
            return false;
        }
        return !(item instanceof MPCraftingBlockItem)
                && !(item instanceof MPFurnaceBlockItem)
                && !(item instanceof MPBrewingBlockItem)
                && !(item instanceof MPHookBlockItem);
    }

    private static void addTypedSourceRecipes(List<SourceCopyJeiRecipe> recipes, ItemStack source, Item item, int maxType) {
        for (int type = 0; type <= maxType; type++) {
            ItemStack input = new ItemStack(item);
            input.getOrCreateTag().putInt(MPGNBTData.ItemType, type);
            ItemStack output = input.copy();
            output.setCount(MPGConfig.source_doubling_value);
            recipes.add(new SourceCopyJeiRecipe(source.copy(), input, output));
        }
    }
}
