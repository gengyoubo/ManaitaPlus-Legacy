package github.com.gengyoubo.item.data;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IMPGKey {
    default void onManaitaKeyPress(ItemStack paramItemStack) {
    }

    default void onManaitaKeyPress(ItemStack paramItemStack, Player paramEntityPlayer) {
        onManaitaKeyPress(paramItemStack);
    }

    default void onManaitaKeyPressOnClient(ItemStack paramItemStack, Player paramEntityPlayer) {
    }
}
