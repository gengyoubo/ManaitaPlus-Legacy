package github.com.gengyoubo.core;

import github.com.gengyoubo.recipe.MPGNBTCraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.RegistryObject;
import github.com.gengyoubo.recipe.MPGCraftingRecipe;

import static github.com.gengyoubo.MPG.RECIPE_SERIALIZER_DEFERRED_REGISTER;

public class MPGRecipeSerializerCore {
    public static final RegistryObject<RecipeSerializer<?>> CraftingRecipe = RECIPE_SERIALIZER_DEFERRED_REGISTER.register("manaita_crafting", MPGCraftingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> NBTCraftingRecipe = RECIPE_SERIALIZER_DEFERRED_REGISTER.register("manaita_crafting_type", MPGNBTCraftingRecipe.Serializer::new);

    public static void init() {
    }

}
