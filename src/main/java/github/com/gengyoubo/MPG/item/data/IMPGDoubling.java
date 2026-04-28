package github.com.gengyoubo.MPG.item.data;

import net.minecraft.world.item.ItemStack;
import github.com.gengyoubo.MPG.util.MPGItemStackData;
import github.com.gengyoubo.MPG.util.MPGNBTData;

public interface IMPGDoubling {
    default boolean isDoubling(ItemStack itemStack) {
        return MPGItemStackData.getBoolean(itemStack, MPGNBTData.Doubling);
    }

    default void setDoubling(ItemStack itemStack, boolean doubling) {
        MPGItemStackData.putBoolean(itemStack, MPGNBTData.Doubling, doubling);
    }

    default boolean toggleDoubling(ItemStack itemStack) {
        boolean doubling = !isDoubling(itemStack);
        setDoubling(itemStack, doubling);
        return doubling;
    }
}
