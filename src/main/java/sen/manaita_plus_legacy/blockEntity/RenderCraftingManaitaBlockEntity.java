package sen.manaita_plus_legacy.blockEntity;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sen.manaita_plus_legacy.block.entity.ManaitaPlusCraftingBlockEntity;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyBlockCore;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class RenderCraftingManaitaBlockEntity extends AbstractRenderManaitaBlockEntity<ManaitaPlusCraftingBlockEntity> {
    public RenderCraftingManaitaBlockEntity(BlockEntityRendererProvider.Context context) {
        super(new ItemStack(ManaitaPlusLegacyBlockCore.CraftingBlockItem.get()));
        Objects.requireNonNull(context);
    }
}
