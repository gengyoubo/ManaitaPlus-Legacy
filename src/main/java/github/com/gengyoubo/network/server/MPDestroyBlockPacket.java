package github.com.gengyoubo.network.server;

import github.com.gengyoubo.network.MPNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class MPDestroyBlockPacket {
    private final BlockPos blockPos;
    private final int range;
    private final Item item;

    public MPDestroyBlockPacket(FriendlyByteBuf buffer) {
        this.blockPos = buffer.readBlockPos();
        this.range = buffer.readInt();
        ResourceLocation itemId = buffer.readResourceLocation();
        this.item = BuiltInRegistries.ITEM.get(itemId);
    }

    public MPDestroyBlockPacket(BlockPos blockPos, int range, Item item) {
        this.blockPos = blockPos;
        this.range = range;
        this.item = item;
    }

    public FriendlyByteBuf toBuf() {
        FriendlyByteBuf buf = MPNetworking.createBuf();
        buf.writeBlockPos(blockPos);
        buf.writeInt(range);
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
        buf.writeResourceLocation(itemId);
        return buf;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public int getRange() {
        return range;
    }

    public Item getItem() {
        return item;
    }
}
