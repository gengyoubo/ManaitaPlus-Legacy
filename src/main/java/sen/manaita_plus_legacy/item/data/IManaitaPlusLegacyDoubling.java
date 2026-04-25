package sen.manaita_plus_legacy.item.data;

import net.minecraft.world.item.ItemStack;
import sen.manaita_plus_legacy.util.ManaitaPlusLegacyNBTData;

public interface IManaitaPlusLegacyDoubling {
    default boolean isDoubling(ItemStack itemStack) {
        if (!itemStack.hasTag()) return false;
        if (itemStack.getTag() != null) {
            return itemStack.getTag().getBoolean(ManaitaPlusLegacyNBTData.Doubling);
        }
        return false;
    }

    default void setDoubling(ItemStack itemStack, boolean doubling) {
        itemStack.getOrCreateTag().putBoolean(ManaitaPlusLegacyNBTData.Doubling, doubling);
    }

    default boolean toggleDoubling(ItemStack itemStack) {
        boolean doubling = !isDoubling(itemStack);
        setDoubling(itemStack, doubling);
        return doubling;
    }
}
