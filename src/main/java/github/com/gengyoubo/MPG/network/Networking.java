package github.com.gengyoubo.MPG.network;

import github.com.gengyoubo.MPG.MPG;
import github.com.gengyoubo.MPG.network.client.KeyPressPacket;
import github.com.gengyoubo.MPG.network.client.OpenBaublesPacket;
import github.com.gengyoubo.MPG.network.server.ChangeEntityDataPacket;
import github.com.gengyoubo.MPG.network.server.DestroyBlockPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class Networking {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MPG.MODID, "main"),
            () -> PROTOCOL_VERSION,
            NetworkRegistry.acceptMissingOr(PROTOCOL_VERSION),
            NetworkRegistry.acceptMissingOr(PROTOCOL_VERSION)
    );

    private Networking() {
    }

    public static void registerMessage() {
        int id = 0;
        CHANNEL.messageBuilder(KeyPressPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(KeyPressPacket::encode)
                .decoder(KeyPressPacket::decode)
                .consumerMainThread(KeyPressPacket::handle)
                .add();
        CHANNEL.messageBuilder(OpenBaublesPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(OpenBaublesPacket::encode)
                .decoder(OpenBaublesPacket::decode)
                .consumerMainThread(OpenBaublesPacket::handle)
                .add();
        CHANNEL.messageBuilder(DestroyBlockPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(DestroyBlockPacket::encode)
                .decoder(DestroyBlockPacket::decode)
                .consumerMainThread(DestroyBlockPacket::handle)
                .add();
        CHANNEL.messageBuilder(ChangeEntityDataPacket.class, id, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ChangeEntityDataPacket::encode)
                .decoder(ChangeEntityDataPacket::decode)
                .consumerMainThread(ChangeEntityDataPacket::handle)
                .add();
    }

    public static void sendToServer(Object message) {
        CHANNEL.sendToServer(message);
    }

    public static void sendToSameLevelPlayers(Entity entity, Object message) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
    }

    public static void sendToTrackBySeen(ServerLevel level, Player player, Object message) {
        sendToSameLevelPlayers(player, message);
    }
}
