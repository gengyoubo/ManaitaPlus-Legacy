package github.com.gengyoubo.network;

import github.com.gengyoubo.MPG;
import github.com.gengyoubo.network.client.MPKeyPressPacket;
import github.com.gengyoubo.network.server.MPChangeEntityDataPacket;
import github.com.gengyoubo.network.server.MPDestroyBlockPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class MPNetworking {
    public static final ResourceLocation KEY_PRESS = id("key_press");
    public static final ResourceLocation DESTROY_BLOCK = id("destroy_block");
    public static final ResourceLocation CHANGE_ENTITY_DATA = id("change_entity_data");

    private MPNetworking() {
    }

    public static void initServer() {
        ServerPlayNetworking.registerGlobalReceiver(KEY_PRESS, (server, player, handler, buf, responseSender) -> {
            MPKeyPressPacket packet = new MPKeyPressPacket(buf);
            server.execute(() -> packet.handle(player));
        });
    }

    public static void sendToSameLevelPlayers(Level level, MPDestroyBlockPacket packet) {
        if (level instanceof ServerLevel serverLevel) {
            for (ServerPlayer serverPlayer : serverLevel.players()) {
                ServerPlayNetworking.send(serverPlayer, DESTROY_BLOCK, packet.toBuf());
            }
        }
    }

    public static void sendToSameLevelPlayers(Level level, MPChangeEntityDataPacket packet) {
        if (level instanceof ServerLevel serverLevel) {
            for (ServerPlayer serverPlayer : serverLevel.players()) {
                ServerPlayNetworking.send(serverPlayer, CHANGE_ENTITY_DATA, packet.toBuf());
            }
        }
    }

    @Deprecated
    public static void sendToNearByPlayers(Level level, MPDestroyBlockPacket packet, int range) {
        sendToSameLevelPlayers(level, packet);
    }

    public static void sendToTrackBySeen(Level level, Player player, MPDestroyBlockPacket packet) {
        if (level instanceof ServerLevel serverLevel) {
            FriendlyByteBuf buf = packet.toBuf();
            for (ServerPlayer serverPlayer : serverLevel.players()) {
                if (serverPlayer == player || serverPlayer.distanceToSqr(player) <= 128.0D * 128.0D) {
                    ServerPlayNetworking.send(serverPlayer, DESTROY_BLOCK, PacketByteBufs.copy(buf));
                }
            }
        }
    }

    public static FriendlyByteBuf createBuf() {
        return PacketByteBufs.create();
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(MPG.MODID, path);
    }
}
