package sen.manaita_plus_legacy.blockEntity;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sen.manaita_plus_legacy.block.entity.ManaitaPlusFurnaceBlockEntity;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyBlockCore;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class RenderFurnaceManaitaBlockEntity extends AbstractRenderManaitaBlockEntity<ManaitaPlusFurnaceBlockEntity> {
    public RenderFurnaceManaitaBlockEntity(BlockEntityRendererProvider.Context context) {
        super(new ItemStack(ManaitaPlusLegacyBlockCore.FurnaceBlockItem.get()));
        Objects.requireNonNull(context);
    }
}
