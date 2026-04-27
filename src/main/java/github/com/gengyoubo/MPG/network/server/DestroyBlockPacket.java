package github.com.gengyoubo.MPG.network.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import github.com.gengyoubo.MPG.network.ClientPacketHandlers;

import java.util.Objects;
import java.util.function.Supplier;

public class DestroyBlockPacket {
    private final BlockPos blockPos;
    private final int range;
    private final Item item;

    public DestroyBlockPacket(FriendlyByteBuf buffer) {
        blockPos = buffer.readBlockPos();
        range = buffer.readInt();
        ResourceLocation itemId = buffer.readResourceLocation();
        Item resolved = ForgeRegistries.ITEMS.getValue(itemId);
        item = resolved == null ? Items.AIR : resolved;
    }


    public DestroyBlockPacket(BlockPos blockPos, int range, Item item) {
        this.blockPos = blockPos;
        this.range = range;
        this.item = item;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(range);
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
        buf.writeResourceLocation(itemId == null ? Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(Items.AIR)) : itemId);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (!ctx.get().getDirection().getReceptionSide().isClient()) return;
            ClientPacketHandlers.handleDestroyBlock(this);
        });
        ctx.get().setPacketHandled(true);
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
