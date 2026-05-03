package github.com.gengyoubo.MPG.block.entity;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

public final class MPGBrewingLogicHelper {
    private MPGBrewingLogicHelper() {
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
            return PotionBrewing.isIngredient(stack);
        }
        if (index == 4) {
            return stack.is(Items.BLAZE_POWDER);
        }
        return (PotionUtils.getPotion(stack) != Potions.EMPTY || stack.is(Items.GLASS_BOTTLE)) && currentSlotItem.isEmpty();
    }
}
