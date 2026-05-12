package github.com.gengyoubo.MPG.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import github.com.gengyoubo.MPG.MPGConfig;
import github.com.gengyoubo.MPG.core.MPGItemCore;
import github.com.gengyoubo.MPG.core.MPGRecipeSerializerCore;
import github.com.gengyoubo.MPG.item.MPGSourceItem;

public class MPGCraftingRecipe implements CraftingRecipe {
    private static final ItemStack SOURCE_RESULT = new ItemStack(MPGItemCore.ManaitaSource.get());

    private final ResourceLocation id;
    private final CraftingBookCategory category;

    public MPGCraftingRecipe(ResourceLocation id, CraftingBookCategory category) {
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
        int sourceCount = 0;
        int nonEmptyCount = 0;

        for (ItemStack stack : container.getItems()) {
            if (stack.isEmpty()) {
                continue;
            }
            nonEmptyCount++;
            if (stack.getItem() instanceof MPGSourceItem) {
                sourceCount++;
            }
        }

        return nonEmptyCount == 2 && sourceCount >= 1;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer container, @NotNull RegistryAccess registryAccess) {
        ItemStack sourceTarget = ItemStack.EMPTY;
        boolean consumedSource = false;

        for (ItemStack stack : container.getItems()) {
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.getItem() instanceof MPGSourceItem && !consumedSource) {
                consumedSource = true;
                continue;
            }
            sourceTarget = stack;
        }

        if (consumedSource && !sourceTarget.isEmpty()) {
            ItemStack copied = sourceTarget.copy();
            copied.setCount(MPGConfig.source_doubling_value);
            return copied;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(@NotNull CraftingContainer container) {
        NonNullList<ItemStack> remainingItems = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        boolean consumedSource = false;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.getItem() instanceof MPGSourceItem && !consumedSource) {
                consumedSource = true;
                continue;
            }
            ItemStack remaining = stack.copy();
            remaining.setCount(1);
            remainingItems.set(i, remaining);
        }
        return remainingItems;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return SOURCE_RESULT;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return MPGRecipeSerializerCore.CraftingRecipe.get();
    }

    public static class Serializer implements RecipeSerializer<MPGCraftingRecipe> {
        @Override
        public @NotNull MPGCraftingRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            CraftingBookCategory category = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
            return new MPGCraftingRecipe(id, category);
        }

        @Override
        public MPGCraftingRecipe fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
            return new MPGCraftingRecipe(id, buf.readEnum(CraftingBookCategory.class));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, MPGCraftingRecipe recipe) {
            buf.writeEnum(recipe.category);
        }
    }
}
