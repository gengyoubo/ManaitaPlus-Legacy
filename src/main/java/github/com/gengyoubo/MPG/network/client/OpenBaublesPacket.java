package github.com.gengyoubo.MPG.network.client;

import github.com.gengyoubo.MPG.MPG;
import github.com.gengyoubo.MPG.baubles.common.capability.BaublesCapability;
import github.com.gengyoubo.MPG.baubles.common.lib.PlayerHandler;
import github.com.gengyoubo.MPG.menu.MPGBaublesMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record OpenBaublesPacket() {
    public static OpenBaublesPacket decode(FriendlyByteBuf buffer) {
        return new OpenBaublesPacket();
    }

    public static void encode(OpenBaublesPacket payload, FriendlyByteBuf buffer) {
    }

    public static void handle(OpenBaublesPacket payload, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            ServerPlayer sender = ctx.getSender();
            if (ctx.getDirection().getReceptionSide().isClient() || sender == null || !BaublesCapability.isEnabled()) {
                return;
            }
            sender.openMenu(new net.minecraft.world.SimpleMenuProvider(
                    (containerId, inventory, player) -> new MPGBaublesMenu(containerId, inventory, PlayerHandler.getPlayerBaubles(player)),
                    Component.translatable("container.manaita_baubles")
            ));
        });
        ctx.setPacketHandled(true);
    }
}
