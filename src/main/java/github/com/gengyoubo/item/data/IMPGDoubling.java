package github.com.gengyoubo.item.data;

import net.minecraft.world.item.ItemStack;
import github.com.gengyoubo.util.MPGNBTData;

public interface IMPGDoubling {
    default boolean isDoubling(ItemStack itemStack) {
        if (!itemStack.hasTag()) return false;
        if (itemStack.getTag() != null) {
            return itemStack.getTag().getBoolean(MPGNBTData.Doubling);
        }
        return false;
    }

    default void setDoubling(ItemStack itemStack, boolean doubling) {
        itemStack.getOrCreateTag().putBoolean(MPGNBTData.Doubling, doubling);
    }

    default boolean toggleDoubling(ItemStack itemStack) {
        boolean doubling = !isDoubling(itemStack);
        setDoubling(itemStack, doubling);
        return doubling;
    }
}
