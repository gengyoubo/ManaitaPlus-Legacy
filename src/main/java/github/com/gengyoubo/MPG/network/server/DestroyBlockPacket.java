package github.com.gengyoubo.MPG.network.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;
import github.com.gengyoubo.MPG.network.ClientPacketHandlers;
import github.com.gengyoubo.MPG.MPG;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record DestroyBlockPacket(BlockPos blockPos, int range, Item item) {
    public static DestroyBlockPacket decode(@NotNull FriendlyByteBuf buffer) {
        BlockPos blockPos = buffer.readBlockPos();
        int range = buffer.readInt();
        ResourceLocation itemId = buffer.readResourceLocation();
        Item item = BuiltInRegistries.ITEM.get(itemId);
        return new DestroyBlockPacket(blockPos, range, item);
    }

    public static void encode(@NotNull DestroyBlockPacket payload, @NotNull FriendlyByteBuf buffer) {
        buffer.writeBlockPos(payload.blockPos);
        buffer.writeInt(payload.range);
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(payload.item);
        buffer.writeResourceLocation(itemId);
    }

    public static void handle(DestroyBlockPacket payload, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            if (ctx.getDirection().getReceptionSide().isServer()) {
                return;
            }
            ClientPacketHandlers.handleDestroyBlock(payload);
        });
        ctx.setPacketHandled(true);
    }
}
