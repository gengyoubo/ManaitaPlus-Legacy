package github.com.gengyoubo.jei;

import github.com.gengyoubo.MPGConfig;
import github.com.gengyoubo.MPG;
import github.com.gengyoubo.block.item.MPBrewingBlockItem;
import github.com.gengyoubo.block.item.MPCraftingBlockItem;
import github.com.gengyoubo.block.item.MPFurnaceBlockItem;
import github.com.gengyoubo.block.item.MPHookBlockItem;
import github.com.gengyoubo.core.MPItemCore;
import github.com.gengyoubo.recipe.MPNBTCraftingRecipe;
import github.com.gengyoubo.gui.MPBrewingStandScreen;
import github.com.gengyoubo.gui.MPCraftingScreen;
import github.com.gengyoubo.gui.MPFurnaceScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

@JeiPlugin
public class MPJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation("manaita_plus_general", "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        MPG.LOGGER.info("Registering JEI categories for {}", UID);
        registration.addRecipeCategories(new MPSourceCopyRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        MPG.LOGGER.info("Registering JEI recipes for source copying");
        registration.addRecipes(MPSourceCopyRecipeCategory.TYPE, createSourceCopyRecipes());
        registration.addRecipes(RecipeTypes.CRAFTING, createTypedCraftingRecipes());
        registration.addItemStackInfo(
                new ItemStack(MPItemCore.ManaitaSource.get()),
                Component.translatable("jei.manaita_plus_general.source.info.1"),
                Component.translatable("jei.manaita_plus_general.source.info.2", MPGConfig.source_doubling_value)
        );
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(MPCraftingScreen.class, 88, 32, 28, 23, RecipeTypes.CRAFTING);
        registration.addRecipeClickArea(MPFurnaceScreen.class, 78, 32, 28, 23, RecipeTypes.SMELTING);
        registration.addRecipeClickArea(MPBrewingStandScreen.class, 97, 16, 14, 30, RecipeTypes.BREWING);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(MPItemCore.ManaitaSource.get()), MPSourceCopyRecipeCategory.TYPE);
    }

    private static List<MPSourceCopyJeiRecipe> createSourceCopyRecipes() {
        return java.util.stream.Stream.concat(
                        java.util.stream.Stream.of(MPItemCore.ManaitaSource.get()),
                        BuiltInRegistries.ITEM.stream()
                )
                .distinct()
                .filter(MPJeiPlugin::isCopyableItem)
                .sorted(Comparator.comparing(item -> BuiltInRegistries.ITEM.getKey(item).toString()))
                .map(Item::getDefaultInstance)
                .filter(stack -> !stack.isEmpty())
                .map(MPJeiPlugin::createRecipe)
                .toList();
    }

    private static MPSourceCopyJeiRecipe createRecipe(ItemStack input) {
        int sourceResultCount = input.getCount() * MPGConfig.source_doubling_value;
        ItemStack output = input.copy();
        output.setCount(Math.max(sourceResultCount, 1));
        return new MPSourceCopyJeiRecipe(input, output, sourceResultCount);
    }

    private static boolean isCopyableItem(Item item) {
        if (item instanceof MPCraftingBlockItem || item instanceof MPFurnaceBlockItem || item instanceof MPBrewingBlockItem || item instanceof MPHookBlockItem) {
            return false;
        }
        return item != net.minecraft.world.item.Items.AIR;
    }

    private static List<CraftingRecipe> createTypedCraftingRecipes() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            MPG.LOGGER.warn("Skipping JEI typed crafting recipe registration because no client level is loaded yet");
            return List.of();
        }
        return minecraft.level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING).stream()
                .filter(MPNBTCraftingRecipe.class::isInstance)
                .map(CraftingRecipe.class::cast)
                .sorted(Comparator.comparing(recipe -> recipe.getId().toString()))
                .toList();
    }
}
