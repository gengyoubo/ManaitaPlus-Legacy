package sen.manaita_plus_legacy.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sen.manaita_plus_legacy.ManaitaPlusLegacyConfig;
import sen.manaita_plus_legacy.block.item.ManaitaPlusBrewingBlockItem;
import sen.manaita_plus_legacy.block.item.ManaitaPlusCraftingBlockItem;
import sen.manaita_plus_legacy.block.item.ManaitaPlusFurnaceBlockItem;
import sen.manaita_plus_legacy.block.item.ManaitaPlusHookBlockItem;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyBlockCore;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyItemCore;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyRecipeSerializerCore;
import sen.manaita_plus_legacy.item.ManaitaPlusLegacySourceItem;
import sen.manaita_plus_legacy.util.ManaitaPlusLegacyNBTData;

public class ManaitaPlusLegacyCraftingRecipe implements CraftingRecipe {
    private static final ItemStack SOURCE_RESULT = new ItemStack(ManaitaPlusLegacyItemCore.ManaitaSource.get());

    private final ResourceLocation id;
    private final CraftingBookCategory category;

    public ManaitaPlusLegacyCraftingRecipe(ResourceLocation id, CraftingBookCategory category) {
        this.id = id;
        this.category = category;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull CraftingBookCategory category() {
        return category;
    }

    @Override
    public boolean matches(CraftingContainer container, @NotNull Level level) {
        boolean hasSource = false;
        int nonEmptyCount = 0;
        for (ItemStack stack : container.getItems()) {
            if (stack.isEmpty()) {
                continue;
            }
            nonEmptyCount++;
            if (stack.getItem() instanceof ManaitaPlusLegacySourceItem) {
                hasSource = true;
            }
        }

        if (nonEmptyCount != 2 || !hasSource) {
            return false;
        }

        boolean hasUpgradeableBlock = false;
        boolean hasMaterial = false;
        for (ItemStack stack : container.getItems()) {
            Item item = stack.getItem();
            if (item instanceof ManaitaPlusCraftingBlockItem || item instanceof ManaitaPlusFurnaceBlockItem || item instanceof ManaitaPlusBrewingBlockItem || item instanceof ManaitaPlusHookBlockItem) {
                hasUpgradeableBlock = true;
            } else if (item == Items.OAK_PLANKS || item == Items.COBBLESTONE || item == Items.IRON_BLOCK || item == Items.REDSTONE_BLOCK || item == Items.GOLD_BLOCK || item == Items.DIAMOND_BLOCK || item == Items.EMERALD_BLOCK || item == Items.NETHERITE_BLOCK) {
                hasMaterial = true;
            }
        }
        return hasUpgradeableBlock && hasMaterial;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer container, @NotNull RegistryAccess registryAccess) {
        ItemStack material = ItemStack.EMPTY;
        ItemStack blockInput = ItemStack.EMPTY;
        ItemStack hookInput = ItemStack.EMPTY;
        boolean hasSource = false;

        for (ItemStack stack : container.getItems()) {
            Item item = stack.getItem();
            if (item instanceof ManaitaPlusLegacySourceItem) {
                hasSource = true;
                continue;
            }
            if (stack.isEmpty()) {
                continue;
            }
            if (item instanceof ManaitaPlusHookBlockItem) {
                hookInput = stack;
                continue;
            }
            if (item instanceof ManaitaPlusCraftingBlockItem || item instanceof ManaitaPlusFurnaceBlockItem || item instanceof ManaitaPlusBrewingBlockItem) {
                blockInput = stack;
                continue;
            }
            material = stack;
        }

        if (hasSource && !material.isEmpty() && blockInput.isEmpty() && hookInput.isEmpty()) {
            ItemStack copied = material.copy();
            copied.setCount(ManaitaPlusLegacyConfig.source_doubling_value);
            return copied;
        }

        if (!blockInput.isEmpty() && !hookInput.isEmpty()) {
            return assemblePortable(blockInput, hookInput);
        }

        if (!material.isEmpty() && !blockInput.isEmpty()) {
            int type = resolveMaterialType(material.getItem());
            if (type <= 0) {
                return ItemStack.EMPTY;
            }
            ItemStack result = createBlockResult(blockInput.getItem());
            if (result.isEmpty()) {
                return ItemStack.EMPTY;
            }
            result.getOrCreateTag().putInt(ManaitaPlusLegacyNBTData.ItemType, type);
            return result;
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack assemblePortable(ItemStack blockInput, ItemStack hookInput) {
        if (blockInput.getOrCreateTag().getInt(ManaitaPlusLegacyNBTData.ItemType) != 0) {
            return ItemStack.EMPTY;
        }

        ItemStack portable;
        Item item = blockInput.getItem();
        if (item instanceof ManaitaPlusCraftingBlockItem) {
            portable = new ItemStack(ManaitaPlusLegacyItemCore.ManaitaCraftingPortable.get());
        } else if (item instanceof ManaitaPlusFurnaceBlockItem) {
            portable = new ItemStack(ManaitaPlusLegacyItemCore.ManaitaFurnacePortable.get());
        } else if (item instanceof ManaitaPlusBrewingBlockItem) {
            portable = new ItemStack(ManaitaPlusLegacyItemCore.ManaitaBrewingPortable.get());
        } else {
            return ItemStack.EMPTY;
        }

        int hookType = hookInput.getOrCreateTag().getInt(ManaitaPlusLegacyNBTData.ItemType);
        portable.getOrCreateTag().putInt(ManaitaPlusLegacyNBTData.ItemType, hookType + 1);
        return portable;
    }

    private static ItemStack createBlockResult(Item blockItem) {
        if (blockItem instanceof ManaitaPlusCraftingBlockItem) {
            return new ItemStack(ManaitaPlusLegacyBlockCore.CraftingBlockItem.get());
        }
        if (blockItem instanceof ManaitaPlusFurnaceBlockItem) {
            return new ItemStack(ManaitaPlusLegacyBlockCore.FurnaceBlockItem.get());
        }
        if (blockItem instanceof ManaitaPlusBrewingBlockItem) {
            return new ItemStack(ManaitaPlusLegacyBlockCore.BrewingBlockItem.get());
        }
        return ItemStack.EMPTY;
    }

    private static int resolveMaterialType(Item material) {
        if (material == Items.OAK_PLANKS) return 1;
        if (material == Items.COBBLESTONE) return 2;
        if (material == Items.IRON_BLOCK) return 3;
        if (material == Items.GOLD_BLOCK) return 4;
        if (material == Items.DIAMOND_BLOCK) return 5;
        if (material == Items.EMERALD_BLOCK) return 6;
        if (material == Items.REDSTONE_BLOCK) return 7;
        if (material == Items.NETHERITE_BLOCK) return 8;
        return -1;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return SOURCE_RESULT;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ManaitaPlusLegacyRecipeSerializerCore.CraftingRecipe.get();
    }

    public static class Serializer implements RecipeSerializer<ManaitaPlusLegacyCraftingRecipe> {
        @Override
        public @NotNull ManaitaPlusLegacyCraftingRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            CraftingBookCategory category = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
            return new ManaitaPlusLegacyCraftingRecipe(id, category);
        }

        @Override
        public ManaitaPlusLegacyCraftingRecipe fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
            return new ManaitaPlusLegacyCraftingRecipe(id, buf.readEnum(CraftingBookCategory.class));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ManaitaPlusLegacyCraftingRecipe recipe) {
            buf.writeEnum(recipe.category);
        }
    }
}
