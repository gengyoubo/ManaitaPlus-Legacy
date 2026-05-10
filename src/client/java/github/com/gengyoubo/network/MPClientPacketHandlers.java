package github.com.gengyoubo.network;

import github.com.gengyoubo.item.data.IMPDestroy;
import github.com.gengyoubo.network.server.MPChangeEntityDataPacket;
import github.com.gengyoubo.network.server.MPDestroyBlockPacket;
import github.com.gengyoubo.util.MPEntityData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public final class MPClientPacketHandlers {
    private MPClientPacketHandlers() {
    }

    public static void handleDestroyBlock(MPDestroyBlockPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;

        if (level == null || mc.player == null) {
            return;
        }

        if (!(packet.getItem() instanceof IMPDestroy destroyItem)) {
            return;
        }

        BlockPos center = packet.getBlockPos();
        int range = packet.getRange();

        int minX = center.getX() - range;
        int maxX = center.getX() + range;
        int minY = center.getY() - range;
        int maxY = center.getY() + range;
        int minZ = center.getZ() - range;
        int maxZ = center.getZ() + range;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    pos.set(x, y, z);

                    if (!level.isLoaded(pos)) {
                        continue;
                    }

                    BlockState state = level.getBlockState(pos);

                    if (state.isAir()) {
                        continue;
                    }

                    if (destroyItem.accept(state)) {
                        continue;
                    }

                    destroyClientBlock(mc, level, pos, destroyItem);
                }
            }
        }
    }

    private static void destroyClientBlock(
            Minecraft mc,
            ClientLevel level,
            BlockPos pos,
            IMPDestroy destroyItem
    ) {
        BlockState state = level.getBlockState(pos);

        if (destroyItem.accept(state)) {
            return;
        }

        level.setBlock(pos, level.getFluidState(pos).createLegacyBlock(), 10);

        SoundType soundType = state.getSoundType();
        mc.getSoundManager().play(new SimpleSoundInstance(
                soundType.getHitSound(),
                SoundSource.BLOCKS,
                (soundType.getVolume() + 1.0F) / 8.0F,
                soundType.getPitch() * 0.5F,
                SoundInstance.createUnseededRandom(),
                pos
        ));
    }

    public static void handleChangeEntityData(MPChangeEntityDataPacket packet) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        Entity entity = level.getEntity(packet.getId());
        if (entity == null) {
            return;
        }

        boolean remove = packet.getFlag() < 0;
        int flags = remove ? -packet.getFlag() : packet.getFlag();
        for (MPEntityData entityData : MPEntityData.values()) {
            if ((entityData.getFlag() & flags) != 0) {
                if (remove) {
                    entityData.remove(entity);
                } else {
                    entityData.add(entity);
                }
            }
        }
    }
}
