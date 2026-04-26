package github.com.gengyoubo.block.entity;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public final class MPGBrewingLogicHelper {
    private MPGBrewingLogicHelper() {
    }

    public static void finishBrew(Level level, double x, double y, double z, NonNullList<ItemStack> items, int ingredientSlot) {
        ItemStack ingredient = items.get(ingredientSlot);
        ingredient.shrink(1);
        items.set(ingredientSlot, ingredient);
    }

    public static boolean canPlaceItem(int index, ItemStack stack, ItemStack currentSlotItem) {
        if (index == 3) {
            return !stack.isEmpty();
        }
        if (index == 4) {
            return stack.is(Items.BLAZE_POWDER);
        }
        return !stack.isEmpty() && currentSlotItem.isEmpty();
    }
}
