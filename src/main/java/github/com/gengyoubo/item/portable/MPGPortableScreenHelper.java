package github.com.gengyoubo.item.portable;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class MPGPortableScreenHelper {
    private MPGPortableScreenHelper() {
    }

    public static void openPortableScreen(ServerPlayer serverPlayer, ItemStack itemInHand, Level level, String titleKey, PortableMenuFactory menuFactory) {
        serverPlayer.openMenu(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                buf.writeBlockPos(BlockPos.ZERO);
            }

            @Override
            public @NotNull Component getDisplayName() {
                return Component.translatable(titleKey);
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
                return menuFactory.create(containerId, inventory, player, itemInHand, level);
            }
        });
    }

    @FunctionalInterface
    public interface PortableMenuFactory {
        AbstractContainerMenu create(int containerId, Inventory inventory, Player player, ItemStack itemInHand, Level level);
    }
}
