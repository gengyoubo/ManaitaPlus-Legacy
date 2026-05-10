package github.com.gengyoubo.jei;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public record MPSourceCopyJeiRecipe(List<ItemStack> inputs, List<ItemStack> outputs, int multiplier) {
}
