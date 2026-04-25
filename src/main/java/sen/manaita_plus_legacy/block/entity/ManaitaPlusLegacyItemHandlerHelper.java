package sen.manaita_plus_legacy.block.entity;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public final class ManaitaPlusLegacyItemHandlerHelper {
    private ManaitaPlusLegacyItemHandlerHelper() {
    }

    public static LazyOptional<? extends IItemHandler>[] createSidedHandlers(WorldlyContainer container) {
        return SidedInvWrapper.create(container, Direction.UP, Direction.DOWN, Direction.NORTH);
    }

    public static <T> LazyOptional<T> getSidedItemHandlerCapability(boolean removed, Capability<T> capability, @Nullable Direction facing, LazyOptional<? extends IItemHandler>[] handlers) {
        if (!removed && facing != null && capability == ForgeCapabilities.ITEM_HANDLER) {
            if (facing == Direction.UP) {
                return handlers[0].cast();
            }
            if (facing == Direction.DOWN) {
                return handlers[1].cast();
            }
            return handlers[2].cast();
        }
        return LazyOptional.empty();
    }

    public static void invalidateHandlers(LazyOptional<? extends IItemHandler>[] handlers) {
        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
    }
}
