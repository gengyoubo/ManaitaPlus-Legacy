package github.com.gengyoubo.network.client;

import github.com.gengyoubo.item.data.IMPKey;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class MPKeyPressPacket {
    private final byte keyCode;

    public MPKeyPressPacket(FriendlyByteBuf buffer) {
        this.keyCode = buffer.readByte();
    }

    public MPKeyPressPacket(byte keyCode) {
        this.keyCode = keyCode;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeByte(keyCode);
    }

    public void handle(ServerPlayer sender) {
        switch (keyCode) {
            case 0 -> {
                ItemStack mainHandItem = sender.getMainHandItem();
                if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof IMPKey keyItem) {
                    keyItem.onManaitaKeyPress(mainHandItem, sender);
                }
            }
            case 1 -> {
                for (ItemStack itemStack : sender.getInventory().armor) {
                    if (!itemStack.isEmpty() && itemStack.getItem() instanceof IMPKey keyItem) {
                        keyItem.onManaitaKeyPress(itemStack, sender);
                    }
                }
            }
            default -> {
            }
        }
    }
}
