package github.com.gengyoubo.MPG.recipe.ingredient;

import com.google.gson.JsonObject;
import github.com.gengyoubo.MPG.recipe.MPGNBTCraftingRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.NotNull;
import github.com.gengyoubo.MPG.util.MPGNBTData;

import java.util.stream.Stream;

public class MPGNBTIngredient extends Ingredient {

    public MPGNBTIngredient(Stream<? extends Value> p_43907_) {
        super(p_43907_);
    }

    @Override
    public boolean test(@org.jetbrains.annotations.Nullable ItemStack p_43914_) {
        if (p_43914_ == null) {
            return false;
        } else if (this.isEmpty()) {
            return p_43914_.isEmpty();
        } else {
            for(ItemStack itemstack : this.getItems()) {
                if (itemstack.hasTag() && p_43914_.hasTag()) {
                    assert itemstack.getTag() != null;
                    assert p_43914_.getTag() != null;
                    int manaitaType = itemstack.getTag().getInt(MPGNBTData.ItemType);
                    if (manaitaType != 0 && (!p_43914_.hasTag() || manaitaType != p_43914_.getTag().getInt(MPGNBTData.ItemType))) continue;
                }
                if (itemstack.is(p_43914_.getItem())) {
                    return true;
                }
            }

            return false;
        }
    }


    @Override
    public @NotNull IIngredientSerializer<? extends Ingredient> getSerializer()
    {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements IIngredientSerializer<MPGNBTIngredient>
    {
        public static final Serializer INSTANCE = new Serializer();

        public @NotNull MPGNBTIngredient parse(FriendlyByteBuf buffer)
        {
            return MPGNBTCraftingRecipe.Serializer.fromValues(Stream.generate(() -> new Ingredient.ItemValue(buffer.readItem())).limit(buffer.readVarInt()));
        }

        public @NotNull MPGNBTIngredient parse(@NotNull JsonObject json)
        {
            return MPGNBTCraftingRecipe.Serializer.fromValues(Stream.of(Ingredient.valueFromJson(json)));
        }

        public void write(FriendlyByteBuf buffer, MPGNBTIngredient ingredient)
        {
            ItemStack[] items = ingredient.getItems();
            buffer.writeVarInt(items.length);

            for (ItemStack stack : items)
                buffer.writeItem(stack);
        }
    }
}