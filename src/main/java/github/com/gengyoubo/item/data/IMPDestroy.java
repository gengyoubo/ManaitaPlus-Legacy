package github.com.gengyoubo.item.data;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public interface IMPDestroy {
    boolean accept(BlockState state);
    int getRange(ItemStack itemStack);
    // --娉ㄩ噴鎺夋鏌?(2026/4/24 23:35):void setRange(ItemStack itemStack,int range);
}

