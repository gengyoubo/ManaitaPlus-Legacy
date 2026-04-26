package sen.manaita_plus_legacy.blockEntity;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sen.manaita_plus_legacy.block.entity.ManaitaPlusBrewingStandBlockEntity;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyBlockCore;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class RenderBrewingManaitaBlockEntity extends AbstractRenderManaitaBlockEntity<ManaitaPlusBrewingStandBlockEntity> {
    public RenderBrewingManaitaBlockEntity(BlockEntityRendererProvider.Context context) {
        super(new ItemStack(ManaitaPlusLegacyBlockCore.BrewingBlockItem.get()));
        Objects.requireNonNull(context);
    }
}
