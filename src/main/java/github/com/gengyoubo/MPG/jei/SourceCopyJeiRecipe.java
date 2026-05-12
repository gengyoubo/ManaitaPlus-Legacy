package github.com.gengyoubo.MPG.jei;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public record SourceCopyJeiRecipe(ItemStack source, List<ItemStack> inputs, List<ItemStack> outputs, int multiplier) {
}
