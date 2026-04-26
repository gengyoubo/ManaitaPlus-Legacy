package sen.manaita_plus_legacy.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import sen.manaita_plus_legacy.item.data.IManaitaPlusLegacyDestroy;
import sen.manaita_plus_legacy.network.server.ChangeEntityDataPacket;
import sen.manaita_plus_legacy.network.server.DestroyBlockPacket;
import sen.manaita_plus_legacy.util.ManaitaPlusLegacyEntityData;
import sen.manaita_plus_legacy.util.ManaitaPlusUtils;

public final class ClientPacketHandlers {
    private ClientPacketHandlers() {}

    public static void handleDestroyBlock(DestroyBlockPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null || mc.player == null) return;
        if (!(packet.getItem() instanceof IManaitaPlusLegacyDestroy des)) return;

        BlockPos blockPos = packet.getBlockPos();
        int range = packet.getRange();
        int xM = blockPos.getX() + range;
        int yM = blockPos.getY() + range;
        int zM = blockPos.getZ() + range;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int x = blockPos.getX() - range; x <= xM; x++) {
            for (int y = blockPos.getY() - range; y <= yM; y++) {
                for (int z = blockPos.getZ() - range; z <= zM; z++) {
                    BlockState blockState = level.getBlockState(mutableBlockPos.set(x, y, z));
                    if (des.accept(blockState)) continue;

                    ManaitaPlusUtils.setBlock(level, mutableBlockPos, level.getFluidState(mutableBlockPos).createLegacyBlock(), 10);
                    SoundType soundtype = blockState.getSoundType(level, mutableBlockPos, mc.player);
                    mc.getSoundManager().play(new SimpleSoundInstance(soundtype.getHitSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 8.0F, soundtype.getPitch() * 0.5F, SoundInstance.createUnseededRandom(), mutableBlockPos));
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
        for (ManaitaPlusLegacyEntityData entityList : ManaitaPlusLegacyEntityData.values()) {
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
