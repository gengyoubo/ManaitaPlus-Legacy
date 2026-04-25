package sen.manaita_plus_legacy.block.entity;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public final class ManaitaPlusLegacyBrewingLogicHelper {
    private ManaitaPlusLegacyBrewingLogicHelper() {
    }

    public static void finishBrew(Level level, double x, double y, double z, NonNullList<ItemStack> items, int ingredientSlot) {
        net.minecraftforge.event.ForgeEventFactory.onPotionBrewed(items);
        ItemStack ingredient = items.get(ingredientSlot);
        if (ingredient.hasCraftingRemainingItem()) {
            ItemStack remain = ingredient.getCraftingRemainingItem().copy();
            ingredient.shrink(1);
            if (ingredient.isEmpty()) {
                ingredient = remain;
            } else {
                Containers.dropItemStack(level, x, y, z, remain);
            }
        } else {
            ingredient.shrink(1);
        }
        items.set(ingredientSlot, ingredient);
    }

    public static boolean canPlaceItem(int index, ItemStack stack, ItemStack currentSlotItem) {
        if (index == 3) {
            return net.minecraftforge.common.brewing.BrewingRecipeRegistry.isValidIngredient(stack);
        }
        if (index == 4) {
            return stack.is(Items.BLAZE_POWDER);
        }
        return net.minecraftforge.common.brewing.BrewingRecipeRegistry.isValidInput(stack) && currentSlotItem.isEmpty();
    }
}
