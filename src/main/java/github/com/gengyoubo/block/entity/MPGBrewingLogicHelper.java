package github.com.gengyoubo.block.entity;

import github.com.gengyoubo.MPGConfig;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.Level;

public final class MPGBrewingLogicHelper {
    private MPGBrewingLogicHelper() {
    }

    public static boolean canBrew(NonNullList<ItemStack> items, int ingredientSlot) {
        ItemStack ingredient = items.get(ingredientSlot);
        if (ingredient.isEmpty() || !PotionBrewing.isIngredient(ingredient)) {
            return false;
        }

        for (int slot = 0; slot < 3; slot++) {
            if (PotionBrewing.hasMix(items.get(slot), ingredient)) {
                return true;
            }
        }
        return false;
    }

    public static void finishBrew(Level level, double x, double y, double z, NonNullList<ItemStack> items, int ingredientSlot) {
        ItemStack ingredient = items.get(ingredientSlot);
        if (!canBrew(items, ingredientSlot)) {
            return;
        }

        for (int slot = 0; slot < 3; slot++) {
            ItemStack potion = items.get(slot);
            if (PotionBrewing.hasMix(potion, ingredient)) {
                ItemStack result = PotionBrewing.mix(ingredient, potion);
                result.setCount(Math.max(1, potion.getCount()) * MPGConfig.brewing_doubling_value);
                items.set(slot, result);
            }
        }

        ItemStack remainder = ingredient.getItem().hasCraftingRemainingItem()
                ? new ItemStack(ingredient.getItem().getCraftingRemainingItem())
                : ItemStack.EMPTY;
        ingredient.shrink(1);
        if (ingredient.isEmpty()) {
            items.set(ingredientSlot, remainder);
        } else {
            items.set(ingredientSlot, ingredient);
            if (!remainder.isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(level, x, y, z, remainder);
                level.addFreshEntity(itemEntity);
            }
        }
    }

    public static boolean canPlaceItem(int index, ItemStack stack, ItemStack currentSlotItem) {
        if (index == 3) {
            return PotionBrewing.isIngredient(stack);
        }
        if (index == 4) {
            return stack.is(Items.BLAZE_POWDER);
        }
        return !stack.isEmpty() && currentSlotItem.isEmpty();
    }
}
