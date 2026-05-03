package github.com.gengyoubo.MPG.network.client;

import github.com.gengyoubo.MPG.baubles.common.lib.PlayerHandler;
import github.com.gengyoubo.MPG.item.data.IMPGKey;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import github.com.gengyoubo.MPG.MPG;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class KeyPressPacket {
    private final byte keyCode;

    public KeyPressPacket(byte key) {
        this.keyCode = key;
    }

    public static KeyPressPacket decode(FriendlyByteBuf buffer) {
        return new KeyPressPacket(buffer.readByte());
    }

    public static void encode(KeyPressPacket payload, FriendlyByteBuf buffer) {
        buffer.writeByte(payload.keyCode);
    }

    public static void handle(KeyPressPacket payload, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            if (ctx.getDirection().getReceptionSide().isClient()) {
                return;
            }
            ServerPlayer sender = ctx.getSender();
            if (sender == null) {
                return;
            }
            switch (payload.keyCode) {
                case 0 -> {
                    ItemStack equippedRing = PlayerHandler.getEquippedRing(sender).orElse(ItemStack.EMPTY);
                    if (!equippedRing.isEmpty() && equippedRing.getItem() instanceof IMPGKey keyItem) {
                        keyItem.onManaitaKeyPress(equippedRing, sender);
                        return;
                    }
                    ItemStack mainHandItem = sender.getMainHandItem();
                    if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof IMPGKey keyItem) {
                        keyItem.onManaitaKeyPress(mainHandItem, sender);
                    }
                }
                case 1 -> {
                    for (ItemStack itemStack : sender.getInventory().armor) {
                        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IMPGKey keyItem) {
                            keyItem.onManaitaKeyPress(itemStack);
                        }
                    }
                }
                default -> {
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
