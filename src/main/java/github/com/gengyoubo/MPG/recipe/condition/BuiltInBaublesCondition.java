package github.com.gengyoubo.MPG.recipe.condition;

import github.com.gengyoubo.MPG.MPG;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

public record BuiltInBaublesCondition() implements ICondition {
    public static final BuiltInBaublesCondition INSTANCE = new BuiltInBaublesCondition();

    @Override
    public @NotNull ResourceLocation getID() {
        return new ResourceLocation(MPG.MODID, "built_in_baubles");
    }

    @Override
    public boolean test(@NotNull IContext context) {
        return true;
    }
}
