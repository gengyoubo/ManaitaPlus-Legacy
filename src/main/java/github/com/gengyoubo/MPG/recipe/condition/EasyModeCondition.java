package github.com.gengyoubo.MPG.recipe.condition;

import github.com.gengyoubo.MPG.MPGConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

public record EasyModeCondition(boolean value) implements ICondition {
    @Override
    public @NotNull ResourceLocation getID() {
        return new ResourceLocation("manaita_plus_general", "easy_mode");
    }

    @Override
    public boolean test(@NotNull IContext context) {
        return MPGConfig.easy_mode_value == value;
    }
}
