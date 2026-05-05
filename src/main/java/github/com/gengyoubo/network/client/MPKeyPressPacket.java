package github.com.gengyoubo.network.client;

import github.com.gengyoubo.compat.MPTrinketsCompat;
import github.com.gengyoubo.item.data.IMPKey;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class MPKeyPressPacket {
    private final byte keyCode;
    private final boolean shiftDown;

    public MPKeyPressPacket(FriendlyByteBuf buffer) {
        this.keyCode = buffer.readByte();
        this.shiftDown = buffer.readBoolean();
    }

    public MPKeyPressPacket(byte keyCode, boolean shiftDown) {
        this.keyCode = keyCode;
        this.shiftDown = shiftDown;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeByte(keyCode);
        buf.writeBoolean(shiftDown);
    }

    public void handle(ServerPlayer sender) {
        switch (keyCode) {
            case 0 -> {
                if (!shiftDown) {
                    var ring = MPTrinketsCompat.findPrimaryEquippedRing(sender);
                    if (ring.isPresent() && ring.get().getItem() instanceof IMPKey keyItem) {
                        keyItem.onManaitaKeyPress(ring.get(), sender);
                        return;
                    }
                }
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
