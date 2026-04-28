package github.com.gengyoubo.item.data;

import net.minecraft.world.item.ItemStack;
import github.com.gengyoubo.util.MPNBTData;

public interface IMPDoubling {
    default boolean isDoubling(ItemStack itemStack) {
        if (!github.com.gengyoubo.util.MPItemStackData.hasTag(itemStack)) return false;
        if (github.com.gengyoubo.util.MPItemStackData.getTag(itemStack) != null) {
            return github.com.gengyoubo.util.MPItemStackData.getTag(itemStack).getBoolean(MPNBTData.Doubling);
        }
        return false;
    }

    default void setDoubling(ItemStack itemStack, boolean doubling) {
        github.com.gengyoubo.util.MPItemStackData.putBoolean(itemStack, MPNBTData.Doubling, doubling);
    }

    default boolean toggleDoubling(ItemStack itemStack) {
        boolean doubling = !isDoubling(itemStack);
        setDoubling(itemStack, doubling);
        return doubling;
    }
}


