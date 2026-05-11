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
import github.com.gengyoubo.util.MPNBTData;
import github.com.gengyoubo.gui.MPBrewingStandScreen;
import github.com.gengyoubo.gui.MPCraftingScreen;
import github.com.gengyoubo.gui.MPFurnaceScreen;
import github.com.gengyoubo.recipe.MPNBTCraftingRecipe;
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
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@JeiPlugin
public class MPJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation("manaita_plus_general", "jei_plugin");
    private static final String RECIPE_PATH_PREFIX = "recipes/";
    private static final String RECIPE_FILE_SUFFIX = ".json";
    private static final String MANAITA_CRAFTING_TYPE = MPG.MODID + ":manaita_crafting_type";

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        ItemLike[] items = {
                MPBlockCore.CraftingBlockItem,
                MPBlockCore.FurnaceBlockItem,
                MPBlockCore.BrewingBlockItem,
                MPBlockCore.HookBlockItem,

                MPItemCore.ManaitaCraftingPortable,
                MPItemCore.ManaitaFurnacePortable,
                MPItemCore.ManaitaBrewingPortable
        };

        for (ItemLike item : items) {
            registration.registerSubtypeInterpreter((Item) item, MPJeiPlugin::getTypedSubtype);
        }

        if (MPTrinketsCompat.isLoaded()) {
            ItemLike[] trinketItems = {
                    MPItemCore.ManaitaCraftingRing,
                    MPItemCore.ManaitaFurnaceRing,
                    MPItemCore.ManaitaBrewingRing
            };

            for (ItemLike item : trinketItems) {
                registration.registerSubtypeInterpreter((Item) item, MPJeiPlugin::getTypedSubtype);
            }
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
        List<CraftingRecipe> manaitaCraftingRecipes = createManaitaCraftingRecipes();
        MPG.LOGGER.info("Registering {} Manaita typed crafting recipes with JEI", manaitaCraftingRecipes.size());
        registration.addRecipes(RecipeTypes.CRAFTING, manaitaCraftingRecipes);
        registration.addItemStackInfo(
                new ItemStack(MPItemCore.ManaitaSource),
                Component.translatable("jei.manaita_plus_general.source.info.1", MPGConfig.source_doubling_value)
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
        registration.addRecipeCatalyst(new ItemStack(MPItemCore.ManaitaSource), MPSourceCopyRecipeCategory.TYPE);
    }

    private static List<MPSourceCopyJeiRecipe> createSourceCopyRecipes() {
        List<ItemStack> inputs = Stream.concat(
                        BuiltInRegistries.ITEM.stream()
                                .filter(MPJeiPlugin::isCopyableItem)
                                .sorted(Comparator.comparing(item -> BuiltInRegistries.ITEM.getKey(item).toString()))
                                .map(Item::getDefaultInstance),
                        createTypedCopyableStacks()
                )
                .filter(Predicate.not(ItemStack::isEmpty))
                .toList();

        if (inputs.isEmpty()) {
            return List.of();
        }

        List<ItemStack> outputs = inputs.stream()
                .map(MPJeiPlugin::createCopyOutput)
                .toList();

        return List.of(new MPSourceCopyJeiRecipe(inputs, outputs, MPGConfig.source_doubling_value));
    }

    private static ItemStack createCopyOutput(ItemStack input) {
        ItemStack output = input.copy();
        output.setCount(Math.max(input.getCount() * MPGConfig.source_doubling_value, 1));
        return output;
    }

    private static boolean isCopyableItem(Item item) {
        if (item == MPItemCore.ManaitaSource) {
            return false;
        }
        if (item instanceof MPCraftingBlockItem || item instanceof MPFurnaceBlockItem || item instanceof MPBrewingBlockItem || item instanceof MPHookBlockItem) {
            return false;
        }
        return item != net.minecraft.world.item.Items.AIR;
    }

    private static Stream<ItemStack> createTypedCopyableStacks() {
        Stream<ItemStack> baseStacks = createTypedStacks(
                MPBlockCore.CraftingBlockItem,
                MPBlockCore.FurnaceBlockItem,
                MPBlockCore.BrewingBlockItem,
                MPBlockCore.HookBlockItem,
                MPItemCore.ManaitaCraftingPortable,
                MPItemCore.ManaitaFurnacePortable,
                MPItemCore.ManaitaBrewingPortable
        );

        Stream<ItemStack> ringStacks = MPTrinketsCompat.isLoaded()
                ? createTypedStacks(
                MPItemCore.ManaitaCraftingRing,
                MPItemCore.ManaitaFurnaceRing,
                MPItemCore.ManaitaBrewingRing
        )
                : Stream.empty();

        return Stream.concat(baseStacks, ringStacks);
    }

    private static Stream<ItemStack> createTypedStacks(Item... items) {
        return Arrays.stream(items).flatMap(MPJeiPlugin::createTypedStacks);
    }

    private static Stream<ItemStack> createTypedStacks(Item item) {
        return IntStream.rangeClosed(0, 8)
                .mapToObj(type -> {
                    ItemStack stack = new ItemStack(item);
                    stack.getOrCreateTag().putInt(MPNBTData.ItemType, type);
                    return stack;
                });
    }

    private static List<CraftingRecipe> createManaitaCraftingRecipes() {
        return loadManaitaTypedCraftingRecipes().stream()
                .filter(MPJeiPlugin::isVisibleInCraftingJei)
                .sorted(Comparator.comparing(recipe -> recipe.getId().toString()))
                .toList();
    }

    private static List<CraftingRecipe> loadManaitaTypedCraftingRecipes() {
        Map<ResourceLocation, Resource> resources = Minecraft.getInstance().getResourceManager()
                .listResources("recipes", resource -> resource.getPath().endsWith(RECIPE_FILE_SUFFIX));

        return resources.entrySet().stream()
                .filter(entry -> MPG.MODID.equals(entry.getKey().getNamespace()))
                .map(MPJeiPlugin::loadManaitaTypedCraftingRecipe)
                .flatMap(Optional::stream)
                .toList();
    }

    private static Optional<CraftingRecipe> loadManaitaTypedCraftingRecipe(Map.Entry<ResourceLocation, Resource> entry) {
        ResourceLocation resourceId = entry.getKey();
        try (Reader reader = entry.getValue().openAsReader()) {
            var json = GsonHelper.parse(reader);
            if (!MANAITA_CRAFTING_TYPE.equals(GsonHelper.getAsString(json, "type", ""))) {
                return Optional.empty();
            }
            return Optional.of(new MPNBTCraftingRecipe.Serializer().fromJson(toRecipeId(resourceId), json));
        } catch (IOException | RuntimeException exception) {
            MPG.LOGGER.warn("Failed to load Manaita typed crafting recipe {} for JEI", resourceId, exception);
            return Optional.empty();
        }
    }

    private static ResourceLocation toRecipeId(ResourceLocation resourceId) {
        String path = resourceId.getPath();
        if (path.startsWith(RECIPE_PATH_PREFIX)) {
            path = path.substring(RECIPE_PATH_PREFIX.length());
        }
        if (path.endsWith(RECIPE_FILE_SUFFIX)) {
            path = path.substring(0, path.length() - RECIPE_FILE_SUFFIX.length());
        }
        return new ResourceLocation(resourceId.getNamespace(), path);
    }

    private static boolean isVisibleInCraftingJei(CraftingRecipe recipe) {
        return !(recipe instanceof MPNBTCraftingRecipe typedRecipe)
                || typedRecipe.getDisplayResult().getItem() != MPItemCore.ManaitaSource;
    }

    private static String getTypedSubtype(ItemStack stack, mezz.jei.api.ingredients.subtypes.UidContext context) {
        if (stack.getTag() != null && stack.hasTag() && stack.getTag().contains(MPNBTData.ItemType)) {
            return MPNBTData.ItemType + ":" + stack.getTag().getInt(MPNBTData.ItemType);
        }
        return MPNBTData.ItemType + ":0";
    }
}


