package github.com.gengyoubo.block.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import github.com.gengyoubo.core.MPBlockCore;

public class MPCraftingBlockItem extends BlockItem {
    public MPCraftingBlockItem() {
        super(MPBlockCore.CraftingBlock.get(), new Item.Properties().fireResistant());
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable("block.crafting.name");
    }
}

