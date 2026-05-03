package github.com.gengyoubo.MPG.network.server;

import github.com.gengyoubo.MPG.MPG;
import github.com.gengyoubo.MPG.network.ClientPacketHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ChangeEntityDataPacket(int id, int flag) {
    public static ChangeEntityDataPacket decode(FriendlyByteBuf buffer) {
        return new ChangeEntityDataPacket(buffer.readInt(), buffer.readInt());
    }

    public static void encode(ChangeEntityDataPacket payload, FriendlyByteBuf buffer) {
        buffer.writeInt(payload.id);
        buffer.writeInt(payload.flag);
    }

    public static void handle(ChangeEntityDataPacket payload, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            if (ctx.getDirection().getReceptionSide().isServer()) {
                return;
            }
            ClientPacketHandlers.handleChangeEntityData(payload);
        });
        ctx.setPacketHandled(true);
    }
}
