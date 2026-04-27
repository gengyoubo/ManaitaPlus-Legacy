package github.com.gengyoubo.item.data;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public interface IMPGDestroy {
    boolean accept(BlockState state);
    int getRange(ItemStack itemStack);
    // --注释掉检查 (2026/4/24 23:35):void setRange(ItemStack itemStack,int range);
}
