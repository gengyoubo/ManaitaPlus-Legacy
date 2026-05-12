package github.com.gengyoubo.MPG.jei;

import com.google.gson.JsonObject;
import github.com.gengyoubo.MPG.MPG;
import github.com.gengyoubo.MPG.core.MPGBlockCore;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.IRuntimeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
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
import github.com.gengyoubo.MPG.recipe.MPGNBTCraftingRecipe;
import github.com.gengyoubo.MPG.util.MPGNBTData;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(MPG.MODID, "jei_plugin");
    private static final String RECIPE_PATH_PREFIX = "recipes/";
    private static final String RECIPE_FILE_SUFFIX = ".json";
    private static final String MANAITA_CRAFTING_TYPE = MPG.MODID + ":manaita_crafting_type";

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        Item[] typedItems = {
                MPGBlockCore.CraftingBlockItem.get(),
                MPGBlockCore.FurnaceBlockItem.get(),
                MPGBlockCore.BrewingBlockItem.get(),
                MPGBlockCore.HookBlockItem.get(),
                MPGItemCore.ManaitaCraftingPortable.get(),
                MPGItemCore.ManaitaFurnacePortable.get(),
                MPGItemCore.ManaitaBrewingPortable.get()
        };
        for (Item item : typedItems) {
            registration.registerSubtypeInterpreter(item, JEIPlugin::getTypedSubtype);
        }

        if (ModList.get().isLoaded("curios")) {
            Item[] ringItems = {
                    MPGItemCore.ManaitaCraftingRing.get(),
                    MPGItemCore.ManaitaFurnaceRing.get(),
                    MPGItemCore.ManaitaBrewingRing.get()
            };
            for (Item item : ringItems) {
                registration.registerSubtypeInterpreter(item, JEIPlugin::getTypedSubtype);
            }
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
        List<CraftingRecipe> manaitaCraftingRecipes = createManaitaCraftingRecipes();
        registration.addRecipes(RecipeTypes.CRAFTING, manaitaCraftingRecipes);
        registration.addItemStackInfo(
                new ItemStack(MPGItemCore.ManaitaSource.get()),
                Component.translatable("jei.manaita_plus_general.source.info.1", MPGConfig.source_doubling_value)
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
        List<ItemStack> inputs = Stream.concat(
                        BuiltInRegistries.ITEM.stream()
                                .filter(JEIPlugin::isCopyableItem)
                                .sorted(Comparator.comparing(item -> BuiltInRegistries.ITEM.getKey(item).toString()))
                                .map(Item::getDefaultInstance),
                        createTypedCopyableStacks()
                )
                .filter(stack -> !stack.isEmpty())
                .toList();

        if (inputs.isEmpty()) {
            return List.of();
        }

        List<ItemStack> outputs = inputs.stream()
                .map(JEIPlugin::createCopyOutput)
                .toList();
        return List.of(new SourceCopyJeiRecipe(source, List.copyOf(inputs), outputs, MPGConfig.source_doubling_value));
    }

    private static ItemStack createCopyOutput(ItemStack input) {
        ItemStack output = input.copy();
        output.setCount(Math.max(input.getCount() * MPGConfig.source_doubling_value, 1));
        return output;
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

    private static void addTypedSourceInputs(List<ItemStack> inputs, Item item, int maxType) {
        for (int type = 0; type <= maxType; type++) {
            ItemStack input = new ItemStack(item);
            input.getOrCreateTag().putInt(MPGNBTData.ItemType, type);
            inputs.add(input);
        }
    }

    private static Stream<ItemStack> createTypedCopyableStacks() {
        Stream<ItemStack> baseStacks = createTypedStacks(
                MPGBlockCore.CraftingBlockItem.get(),
                MPGBlockCore.FurnaceBlockItem.get(),
                MPGBlockCore.BrewingBlockItem.get(),
                MPGBlockCore.HookBlockItem.get(),
                MPGItemCore.ManaitaCraftingPortable.get(),
                MPGItemCore.ManaitaFurnacePortable.get(),
                MPGItemCore.ManaitaBrewingPortable.get()
        );

        Stream<ItemStack> ringStacks = ModList.get().isLoaded("curios")
                ? createTypedStacks(
                MPGItemCore.ManaitaCraftingRing.get(),
                MPGItemCore.ManaitaFurnaceRing.get(),
                MPGItemCore.ManaitaBrewingRing.get()
        )
                : Stream.empty();

        return Stream.concat(baseStacks, ringStacks);
    }

    private static Stream<ItemStack> createTypedStacks(Item... items) {
        return Arrays.stream(items).flatMap(JEIPlugin::createTypedStacks);
    }

    private static Stream<ItemStack> createTypedStacks(Item item) {
        int maxType = item instanceof MPHookBlockItem ? 7 : 8;
        return IntStream.rangeClosed(0, maxType)
                .mapToObj(type -> {
                    ItemStack stack = new ItemStack(item);
                    stack.getOrCreateTag().putInt(MPGNBTData.ItemType, type);
                    return stack;
                });
    }

    private static List<CraftingRecipe> createManaitaCraftingRecipes() {
        return loadManaitaTypedCraftingRecipes().stream()
                .filter(JEIPlugin::isVisibleInCraftingJei)
                .sorted(Comparator.comparing(recipe -> recipe.getId().toString()))
                .toList();
    }

    private static List<CraftingRecipe> loadManaitaTypedCraftingRecipes() {
        Map<ResourceLocation, Resource> resources = Minecraft.getInstance().getResourceManager()
                .listResources(RECIPE_PATH_PREFIX.substring(0, RECIPE_PATH_PREFIX.length() - 1), resource -> resource.getPath().endsWith(RECIPE_FILE_SUFFIX));

        return resources.entrySet().stream()
                .filter(entry -> MPG.MODID.equals(entry.getKey().getNamespace()))
                .map(JEIPlugin::loadManaitaTypedCraftingRecipe)
                .flatMap(Optional::stream)
                .toList();
    }

    private static Optional<CraftingRecipe> loadManaitaTypedCraftingRecipe(Map.Entry<ResourceLocation, Resource> entry) {
        ResourceLocation resourceId = entry.getKey();
        try (Reader reader = entry.getValue().openAsReader()) {
            JsonObject json = GsonHelper.parse(reader);
            if (!MANAITA_CRAFTING_TYPE.equals(GsonHelper.getAsString(json, "type", ""))) {
                return Optional.empty();
            }
            return Optional.of(new MPGNBTCraftingRecipe.Serializer().fromJson(toRecipeId(resourceId), json));
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
        return !(recipe instanceof MPGNBTCraftingRecipe typedRecipe)
                || typedRecipe.getDisplayResult().getItem() != MPGItemCore.ManaitaSource.get();
    }

    private static String getTypedSubtype(ItemStack stack, mezz.jei.api.ingredients.subtypes.UidContext context) {
        if (stack.getTag() != null && stack.hasTag() && stack.getTag().contains(MPGNBTData.ItemType)) {
            return MPGNBTData.ItemType + ":" + stack.getTag().getInt(MPGNBTData.ItemType);
        }
        return MPGNBTData.ItemType + ":0";
    }
}
