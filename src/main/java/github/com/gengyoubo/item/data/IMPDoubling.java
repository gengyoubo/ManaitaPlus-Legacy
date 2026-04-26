package github.com.gengyoubo.item.data;

import net.minecraft.world.item.ItemStack;
import github.com.gengyoubo.util.MPNBTData;

public interface IMPDoubling {
    default boolean isDoubling(ItemStack itemStack) {
        if (!itemStack.hasTag()) return false;
        if (itemStack.getTag() != null) {
            return itemStack.getTag().getBoolean(MPNBTData.Doubling);
        }
        return false;
    }

    default void setDoubling(ItemStack itemStack, boolean doubling) {
        itemStack.getOrCreateTag().putBoolean(MPNBTData.Doubling, doubling);
    }

    default boolean toggleDoubling(ItemStack itemStack) {
        boolean doubling = !isDoubling(itemStack);
        setDoubling(itemStack, doubling);
        return doubling;
    }
}

