package sen.manaita_plus_legacy.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.network.client.KeyPressPacket;
import sen.manaita_plus_legacy.network.server.DestroyBlockPacket;
import sen.manaita_plus_legacy.network.server.ChangeEntityDataPacket;

public class Networking {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ++ID;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(ManaitaPlusLegacy.MODID, "manaita_networking"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );
        INSTANCE.messageBuilder(KeyPressPacket.class, nextID())
                .encoder(KeyPressPacket::toBytes)
                .decoder(KeyPressPacket::new)
                .consumerNetworkThread(KeyPressPacket::handler)
                .add();
        INSTANCE.messageBuilder(DestroyBlockPacket.class, nextID())
                .encoder(DestroyBlockPacket::toBytes)
                .decoder(DestroyBlockPacket::new)
                .consumerNetworkThread(DestroyBlockPacket::handler)
                .add();
        INSTANCE.messageBuilder(ChangeEntityDataPacket.class, nextID())
                .encoder(ChangeEntityDataPacket::toBytes)
                .decoder(ChangeEntityDataPacket::new)
                .consumerNetworkThread(ChangeEntityDataPacket::handler)
                .add();
    }

    public static void sendToSameLevelPlayers(Level level,Object packet) {
        if (level instanceof ServerLevel serverLevel) {
            for (ServerPlayer serverPlayer : serverLevel.players()) {
                Networking.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        packet
                );
            }
        }
    }

// --注释掉检查 START (2026/4/24 23:35):
//    public static void sendToNearByPlayers(Level level, Player center, Object packet, int range) {
//        if (level instanceof ServerLevel serverLevel) {
//            if (center == null) {
//                sendToSameLevelPlayers(level, packet);
//                return;
//            }
//            double finalRange = (double) range * (double) range;
//            for (ServerPlayer serverPlayer : serverLevel.players()) {
//                if (serverPlayer.distanceToSqr(center) <= finalRange) {
//                    Networking.INSTANCE.send(
//                            PacketDistributor.PLAYER.with(() -> serverPlayer),
//                            packet
//                    );
//                }
//            }
//        }
//    }
// --注释掉检查 STOP (2026/4/24 23:35)

    @Deprecated
    public static void sendToNearByPlayers(Level level, Object packet, int range) {
        sendToSameLevelPlayers(level, packet);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }

    public static void sendToTrackBySeen(Level level, Player player, Object packet) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().broadcast(player,INSTANCE.toVanillaPacket(packet, NetworkDirection.PLAY_TO_CLIENT));
        }
    }
}
