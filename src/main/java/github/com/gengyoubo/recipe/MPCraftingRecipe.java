package github.com.gengyoubo.recipe;

import com.google.gson.JsonObject;
import github.com.gengyoubo.MPGConfig;
import github.com.gengyoubo.block.item.MPBrewingBlockItem;
import github.com.gengyoubo.block.item.MPCraftingBlockItem;
import github.com.gengyoubo.block.item.MPFurnaceBlockItem;
import github.com.gengyoubo.block.item.MPHookBlockItem;
import github.com.gengyoubo.core.MPBlockCore;
import github.com.gengyoubo.core.MPItemCore;
import github.com.gengyoubo.core.MPRecipeSerializerCore;
import github.com.gengyoubo.item.MPSourceItem;
import github.com.gengyoubo.util.MPNBTData;
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

public class MPCraftingRecipe implements CraftingRecipe {
    private static final ItemStack SOURCE_RESULT = new ItemStack(MPItemCore.ManaitaSource.get());

    private final ResourceLocation id;
    private final CraftingBookCategory category;

    public MPCraftingRecipe(ResourceLocation id, CraftingBookCategory category) {
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
        int sourceStacks = 0;
        int nonEmptyCount = 0;
        ItemStack otherStack = ItemStack.EMPTY;
        for (ItemStack stack : container.getItems()) {
            if (stack.isEmpty()) {
                continue;
            }
            nonEmptyCount++;
            if (stack.getItem() instanceof MPSourceItem) {
                sourceStacks++;
            } else {
                otherStack = stack;
            }
        }

        if (nonEmptyCount != 2 || sourceStacks == 0) {
            return false;
        }

        if (sourceStacks == 2) {
            return true;
        }

        if (!otherStack.isEmpty() && !(otherStack.getItem() instanceof MPSourceItem)) {
            if (!(otherStack.getItem() instanceof MPCraftingBlockItem)
                    && !(otherStack.getItem() instanceof MPFurnaceBlockItem)
                    && !(otherStack.getItem() instanceof MPBrewingBlockItem)
                    && !(otherStack.getItem() instanceof MPHookBlockItem)) {
                return true;
            }
        }

        boolean hasUpgradeableBlock = false;
        boolean hasMaterial = false;
        boolean hasHook = false;
        for (ItemStack stack : container.getItems()) {
            Item item = stack.getItem();
            if (item instanceof MPHookBlockItem) {
                hasHook = true;
            } else if (item instanceof MPCraftingBlockItem || item instanceof MPFurnaceBlockItem || item instanceof MPBrewingBlockItem) {
                hasUpgradeableBlock = true;
            } else if (item == Items.OAK_PLANKS || item == Items.COBBLESTONE || item == Items.IRON_BLOCK || item == Items.REDSTONE_BLOCK || item == Items.GOLD_BLOCK || item == Items.DIAMOND_BLOCK || item == Items.EMERALD_BLOCK || item == Items.NETHERITE_BLOCK) {
                hasMaterial = true;
            }
        }
        return (hasUpgradeableBlock && hasMaterial) || (hasUpgradeableBlock && hasHook);
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer container, @NotNull RegistryAccess registryAccess) {
        ItemStack material = ItemStack.EMPTY;
        ItemStack blockInput = ItemStack.EMPTY;
        ItemStack hookInput = ItemStack.EMPTY;
        boolean hasSource = false;
        int sourceStacks = 0;
        ItemStack sourceTarget = ItemStack.EMPTY;

        for (ItemStack stack : container.getItems()) {
            Item item = stack.getItem();
            if (item instanceof MPSourceItem) {
                hasSource = true;
                sourceStacks++;
                if (sourceTarget.isEmpty()) {
                    sourceTarget = stack;
                }
                continue;
            }
            if (stack.isEmpty()) {
                continue;
            }
            if (item instanceof MPHookBlockItem) {
                hookInput = stack;
                continue;
            }
            if (item instanceof MPCraftingBlockItem || item instanceof MPFurnaceBlockItem || item instanceof MPBrewingBlockItem) {
                blockInput = stack;
                continue;
            }
            material = stack;
        }

        if (sourceStacks == 2 && material.isEmpty() && blockInput.isEmpty() && hookInput.isEmpty()) {
            ItemStack copied = sourceTarget.copy();
            copied.setCount(sourceTarget.getCount() * MPGConfig.source_doubling_value);
            return copied;
        }

        if (hasSource && !material.isEmpty() && blockInput.isEmpty() && hookInput.isEmpty()) {
            ItemStack copied = material.copy();
            copied.setCount(material.getCount() * MPGConfig.source_doubling_value);
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
            result.getOrCreateTag().putInt(MPNBTData.ItemType, type);
            return result;
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack assemblePortable(ItemStack blockInput, ItemStack hookInput) {
        if (blockInput.getOrCreateTag().getInt(MPNBTData.ItemType) != 0) {
            return ItemStack.EMPTY;
        }

        ItemStack portable;
        Item item = blockInput.getItem();
        if (item instanceof MPCraftingBlockItem) {
            portable = new ItemStack(MPItemCore.ManaitaCraftingPortable.get());
        } else if (item instanceof MPFurnaceBlockItem) {
            portable = new ItemStack(MPItemCore.ManaitaFurnacePortable.get());
        } else if (item instanceof MPBrewingBlockItem) {
            portable = new ItemStack(MPItemCore.ManaitaBrewingPortable.get());
        } else {
            return ItemStack.EMPTY;
        }

        int hookType = hookInput.getOrCreateTag().getInt(MPNBTData.ItemType);
        portable.getOrCreateTag().putInt(MPNBTData.ItemType, hookType + 1);
        return portable;
    }

    private static ItemStack createBlockResult(Item blockItem) {
        if (blockItem instanceof MPCraftingBlockItem) {
            return new ItemStack(MPBlockCore.CraftingBlockItem.get());
        }
        if (blockItem instanceof MPFurnaceBlockItem) {
            return new ItemStack(MPBlockCore.FurnaceBlockItem.get());
        }
        if (blockItem instanceof MPBrewingBlockItem) {
            return new ItemStack(MPBlockCore.BrewingBlockItem.get());
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
        return MPRecipeSerializerCore.CraftingRecipe.get();
    }

    public static class Serializer implements RecipeSerializer<MPCraftingRecipe> {
        @Override
        public @NotNull MPCraftingRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            CraftingBookCategory category = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
            return new MPCraftingRecipe(id, category);
        }

        @Override
        public @NotNull MPCraftingRecipe fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
            return new MPCraftingRecipe(id, buf.readEnum(CraftingBookCategory.class));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, MPCraftingRecipe recipe) {
            buf.writeEnum(recipe.category);
        }
    }
}
