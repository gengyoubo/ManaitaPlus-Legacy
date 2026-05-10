package github.com.gengyoubo.core;

import github.com.gengyoubo.recipe.MPCraftingRecipe;
import github.com.gengyoubo.recipe.MPNBTCraftingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;

import static github.com.gengyoubo.MPG.register;

public class MPRecipeSerializerCore {
    public static final RecipeSerializer<MPCraftingRecipe> CraftingRecipe =
            register(BuiltInRegistries.RECIPE_SERIALIZER, "manaita_crafting", new MPCraftingRecipe.Serializer());
    public static final RecipeSerializer<MPNBTCraftingRecipe> NBTCraftingRecipe =
            register(BuiltInRegistries.RECIPE_SERIALIZER, "manaita_crafting_type", new MPNBTCraftingRecipe.Serializer());
}

