package github.com.gengyoubo.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import github.com.gengyoubo.network.ClientPacketHandlers;

import java.util.function.Supplier;

public class ChangeEntityDataPacket {
    private final int id;
    private final int flag;

    public ChangeEntityDataPacket(FriendlyByteBuf buffer) {
        this.id = buffer.readInt();
        this.flag = buffer.readInt();
    }


    public ChangeEntityDataPacket(int id, int flag) {
       this.id = id;
       this.flag = flag;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(id);
        buf.writeInt(flag);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (!ctx.get().getDirection().getReceptionSide().isClient()) return;
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlers.handleChangeEntityData(this));
        });
        ctx.get().setPacketHandled(true);
    }

    public int getId() {
        return id;
    }

    public int getFlag() {
        return flag;
    }
}
