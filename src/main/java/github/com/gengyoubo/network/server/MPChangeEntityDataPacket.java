package github.com.gengyoubo.network.server;

import github.com.gengyoubo.network.MPNetworking;
import net.minecraft.network.FriendlyByteBuf;

public class MPChangeEntityDataPacket {
    private final int id;
    private final int flag;

    public MPChangeEntityDataPacket(FriendlyByteBuf buffer) {
        this.id = buffer.readInt();
        this.flag = buffer.readInt();
    }

    public MPChangeEntityDataPacket(int id, int flag) {
        this.id = id;
        this.flag = flag;
    }

    public FriendlyByteBuf toBuf() {
        FriendlyByteBuf buf = MPNetworking.createBuf();
        buf.writeInt(id);
        buf.writeInt(flag);
        return buf;
    }

    public int getId() {
        return id;
    }

    public int getFlag() {
        return flag;
    }
}
