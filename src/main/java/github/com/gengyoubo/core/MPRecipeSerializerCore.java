package github.com.gengyoubo.core;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.RegistryObject;

import static github.com.gengyoubo.MPG.RECIPE_SERIALIZER_DEFERRED_REGISTER;

public class MPRecipeSerializerCore {
    public static final RegistryObject<RecipeSerializer<?>> CraftingRecipe = RECIPE_SERIALIZER_DEFERRED_REGISTER.register("manaita_crafting", ManaitaPlusLegacyCraftingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> NBTCraftingRecipe = RECIPE_SERIALIZER_DEFERRED_REGISTER.register("manaita_crafting_type", ManaitaPlusLegacyNBTCraftingRecipe.Serializer::new);

    public static void init() {
    }

}

