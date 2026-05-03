package net.minecraftforge.common.extensions;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public final class IForgeMenuType {
    private IForgeMenuType() {
    }

    public static <T extends AbstractContainerMenu> MenuType<T> create(MenuFactory<T> factory) {
        return new ExtendedScreenHandlerType<>(factory::create);
    }

    @FunctionalInterface
    public interface MenuFactory<T extends AbstractContainerMenu> {
        T create(int syncId, Inventory inventory, FriendlyByteBuf buf);
    }
}
