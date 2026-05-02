package github.com.gengyoubo.MPG.network.client;

import github.com.gengyoubo.MPG.item.data.IMPGKey;
import github.com.gengyoubo.MPG.item.ring.MPGCuriosHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class KeyPressPacket {
    private final byte keyCode;


    public KeyPressPacket(FriendlyByteBuf buffer) {
        keyCode = buffer.readByte();
    }


    public KeyPressPacket(byte key) {
        this.keyCode = key;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeByte(keyCode);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) return;
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;
            switch (keyCode) {
                case 0:
                    if (MPGCuriosHelper.findFirstMatching(sender, stack -> stack.getItem() instanceof IMPGKey).isPresent()) {
                        MPGCuriosHelper.findFirstMatching(sender, stack -> stack.getItem() instanceof IMPGKey)
                                .ifPresent(stack -> ((IMPGKey) stack.getItem()).onManaitaKeyPress(stack, sender));
                        break;
                    }
                    ItemStack mainHandItem = sender.getMainHandItem();
                    if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof IMPGKey keyItem) {
                        keyItem.onManaitaKeyPress(mainHandItem);
                    }
                    break;
                case 1:
                    for (ItemStack itemStack : sender.getInventory().armor) {
                        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IMPGKey keyItem) {
                            keyItem.onManaitaKeyPress(itemStack);
                        }
                    }
                    break;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
