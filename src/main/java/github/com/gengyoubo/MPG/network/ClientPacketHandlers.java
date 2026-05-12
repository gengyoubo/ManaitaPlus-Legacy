package github.com.gengyoubo.MPG.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import github.com.gengyoubo.MPG.item.data.IMPGDestroy;
import github.com.gengyoubo.MPG.network.server.ChangeEntityDataPacket;
import github.com.gengyoubo.MPG.network.server.DestroyBlockPacket;
import github.com.gengyoubo.MPG.util.MPGEntityData;
import github.com.gengyoubo.MPG.util.MPUtils;

public final class ClientPacketHandlers {
    private ClientPacketHandlers() {}

    public static void handleDestroyBlock(DestroyBlockPacket packet) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        net.minecraft.client.player.LocalPlayer player = minecraft.player;

        if (level == null || player == null) {
            return;
        }

        if (!(packet.getItem() instanceof IMPGDestroy destroyItem)) {
            return;
        }

        BlockPos center = packet.getBlockPos();
        int range = packet.getRange();

        int minX = center.getX() - range;
        int minY = center.getY() - range;
        int minZ = center.getZ() - range;
        int maxX = center.getX() + range;
        int maxY = center.getY() + range;
        int maxZ = center.getZ() + range;

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    mutablePos.set(x, y, z);

                    BlockState state = level.getBlockState(mutablePos);
                    if (destroyItem.accept(state)) {
                        continue;
                    }

                    MPUtils.setBlock(level, mutablePos, level.getFluidState(mutablePos).createLegacyBlock(), 10);

                    SoundType soundType = state.getSoundType();
                    minecraft.getSoundManager().play(new SimpleSoundInstance(
                            soundType.getHitSound(),
                            SoundSource.BLOCKS,
                            (soundType.getVolume() + 1.0F) / 8.0F,
                            soundType.getPitch() * 0.5F,
                            SoundInstance.createUnseededRandom(),
                            mutablePos.immutable()
                    ));
                }
            }
        }
    }

    public static void handleChangeEntityData(ChangeEntityDataPacket packet) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        Entity entity = level.getEntity(packet.getId());
        if (entity == null) return;

        boolean remove = packet.getFlag() < 0;
        int flags = remove ? -packet.getFlag() : packet.getFlag();
        for (MPGEntityData entityList : MPGEntityData.values()) {
            if ((entityList.getFlag() & flags) != 0) {
                if (remove) {
                    entityList.remove(entity);
                } else {
                    entityList.add(entity);
                }
            }
        }
    }
}
