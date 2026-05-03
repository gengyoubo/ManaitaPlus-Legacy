package github.com.gengyoubo.jei;

import github.com.gengyoubo.compat.MPTrinketsCompat;
import github.com.gengyoubo.MPGConfig;
import github.com.gengyoubo.MPG;
import github.com.gengyoubo.block.item.MPBrewingBlockItem;
import github.com.gengyoubo.block.item.MPCraftingBlockItem;
import github.com.gengyoubo.block.item.MPFurnaceBlockItem;
import github.com.gengyoubo.block.item.MPHookBlockItem;
import github.com.gengyoubo.core.MPBlockCore;
import github.com.gengyoubo.core.MPItemCore;
import github.com.gengyoubo.recipe.MPNBTCraftingRecipe;
import github.com.gengyoubo.util.MPNBTData;
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
import mezz.jei.api.registration.ISubtypeRegistration;
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
import java.util.stream.Stream;

@JeiPlugin
public class MPJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = github.com.gengyoubo.util.MPResource.id("manaita_plus_general", "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(MPBlockCore.CraftingBlockItem.get(), MPJeiPlugin::getTypedSubtype);
        registration.registerSubtypeInterpreter(MPBlockCore.FurnaceBlockItem.get(), MPJeiPlugin::getTypedSubtype);
        registration.registerSubtypeInterpreter(MPBlockCore.BrewingBlockItem.get(), MPJeiPlugin::getTypedSubtype);
        registration.registerSubtypeInterpreter(MPBlockCore.HookBlockItem.get(), MPJeiPlugin::getTypedSubtype);
        registration.registerSubtypeInterpreter(MPItemCore.ManaitaCraftingPortable.get(), MPJeiPlugin::getTypedSubtype);
        registration.registerSubtypeInterpreter(MPItemCore.ManaitaFurnacePortable.get(), MPJeiPlugin::getTypedSubtype);
        registration.registerSubtypeInterpreter(MPItemCore.ManaitaBrewingPortable.get(), MPJeiPlugin::getTypedSubtype);
        if (MPTrinketsCompat.isLoaded()) {
            registration.registerSubtypeInterpreter(MPItemCore.ManaitaCraftingRing.get(), MPJeiPlugin::getTypedSubtype);
            registration.registerSubtypeInterpreter(MPItemCore.ManaitaFurnaceRing.get(), MPJeiPlugin::getTypedSubtype);
            registration.registerSubtypeInterpreter(MPItemCore.ManaitaBrewingRing.get(), MPJeiPlugin::getTypedSubtype);
        }
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
        return Stream.concat(
                        BuiltInRegistries.ITEM.stream()
                                .filter(MPJeiPlugin::isCopyableItem)
                                .sorted(Comparator.comparing(item -> BuiltInRegistries.ITEM.getKey(item).toString()))
                                .map(Item::getDefaultInstance),
                        createTypedCopyableStacks()
                )
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
        if (item == MPItemCore.ManaitaSource.get()) {
            return false;
        }
        if (item instanceof MPCraftingBlockItem || item instanceof MPFurnaceBlockItem || item instanceof MPBrewingBlockItem || item instanceof MPHookBlockItem) {
            return false;
        }
        return item != net.minecraft.world.item.Items.AIR;
    }

    private static Stream<ItemStack> createTypedCopyableStacks() {
        Stream<ItemStack> baseStacks = Stream.of(
                        createTypedStacks(MPBlockCore.CraftingBlockItem.get(), 8),
                        createTypedStacks(MPBlockCore.FurnaceBlockItem.get(), 8),
                        createTypedStacks(MPBlockCore.BrewingBlockItem.get(), 8),
                        createTypedStacks(MPBlockCore.HookBlockItem.get(), 7),
                        createTypedStacks(MPItemCore.ManaitaCraftingPortable.get(), 8),
                        createTypedStacks(MPItemCore.ManaitaFurnacePortable.get(), 8),
                        createTypedStacks(MPItemCore.ManaitaBrewingPortable.get(), 8)
                )
                .flatMap(s -> s);

        Stream<ItemStack> ringStacks = MPTrinketsCompat.isLoaded()
                ? Stream.of(
                        createTypedStacks(MPItemCore.ManaitaCraftingRing.get(), 8),
                        createTypedStacks(MPItemCore.ManaitaFurnaceRing.get(), 8),
                        createTypedStacks(MPItemCore.ManaitaBrewingRing.get(), 8)
                ).flatMap(s -> s)
                : Stream.empty();

        return Stream.concat(baseStacks, ringStacks);
    }

    private static Stream<ItemStack> createTypedStacks(Item item, int maxType) {
        return java.util.stream.IntStream.rangeClosed(0, maxType)
                .mapToObj(type -> {
                    ItemStack stack = new ItemStack(item);
                    stack.getOrCreateTag().putInt(MPNBTData.ItemType, type);
                    return stack;
                });
    }

    private static List<CraftingRecipe> createTypedCraftingRecipes() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            MPG.LOGGER.warn("Skipping JEI typed crafting recipe registration because no client level is loaded yet");
            return List.of();
        }
        return minecraft.level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING).stream()
                .filter(MPNBTCraftingRecipe.class::isInstance)
                .sorted(Comparator.comparing(recipe -> recipe.getId().toString()))
                .toList();
    }

    private static String getTypedSubtype(ItemStack stack, mezz.jei.api.ingredients.subtypes.UidContext context) {
        if (stack.getTag() != null && stack.hasTag() && stack.getTag().contains(MPNBTData.ItemType)) {
            return MPNBTData.ItemType + ":" + stack.getTag().getInt(MPNBTData.ItemType);
        }
        return "default";
    }
}
