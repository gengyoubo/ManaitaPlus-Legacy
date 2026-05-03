package github.com.gengyoubo.MPG.core;

import github.com.gengyoubo.MPG.recipe.condition.BuiltInBaublesCondition;
import github.com.gengyoubo.MPG.recipe.condition.EasyModeCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public final class MPGConditionCore {
    private MPGConditionCore() {
    }

    public static void init() {
        CraftingHelper.register(new IConditionSerializer<BuiltInBaublesCondition>() {
            @Override
            public void write(com.google.gson.JsonObject json, BuiltInBaublesCondition value) {
            }

            @Override
            public BuiltInBaublesCondition read(com.google.gson.JsonObject json) {
                return BuiltInBaublesCondition.INSTANCE;
            }

            @Override
            public ResourceLocation getID() {
                return BuiltInBaublesCondition.INSTANCE.getID();
            }
        });

        CraftingHelper.register(new IConditionSerializer<EasyModeCondition>() {
            @Override
            public void write(com.google.gson.JsonObject json, EasyModeCondition value) {
                json.addProperty("value", value.value());
            }

            @Override
            public EasyModeCondition read(com.google.gson.JsonObject json) {
                return new EasyModeCondition(json.has("value") && json.get("value").getAsBoolean());
            }

            @Override
            public ResourceLocation getID() {
                return new ResourceLocation("manaita_plus_general", "easy_mode");
            }
        });
    }
}
