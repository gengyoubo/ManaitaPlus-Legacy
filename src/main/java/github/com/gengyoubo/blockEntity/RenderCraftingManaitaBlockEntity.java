package github.com.gengyoubo.blockEntity;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import github.com.gengyoubo.block.entity.MPCraftingBlockEntity;
import github.com.gengyoubo.core.MPGBlockCore;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class RenderCraftingManaitaBlockEntity extends AbstractRenderManaitaBlockEntity<MPCraftingBlockEntity> {
    public RenderCraftingManaitaBlockEntity(BlockEntityRendererProvider.Context context) {
        super(new ItemStack(MPGBlockCore.CraftingBlockItem.get()));
        Objects.requireNonNull(context);
    }
}
