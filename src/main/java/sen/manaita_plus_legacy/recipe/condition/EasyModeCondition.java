package sen.manaita_plus_legacy.recipe.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.ManaitaPlusLegacyConfig;

public class EasyModeCondition implements ICondition {
    public static final ResourceLocation ID = new ResourceLocation(ManaitaPlusLegacy.MODID, "easy_mode");
    private final boolean value;

    public EasyModeCondition(boolean value) {
        this.value = value;
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test(IContext context) {
        return ManaitaPlusLegacyConfig.easy_mode_value == value;
    }

    public static class Serializer implements IConditionSerializer<EasyModeCondition> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, EasyModeCondition value) {
            json.addProperty("value", value.value);
        }

        @Override
        public EasyModeCondition read(JsonObject json) {
            return new EasyModeCondition(GsonHelper.getAsBoolean(json, "value", true));
        }

        @Override
        public ResourceLocation getID() {
            return ID;
        }
    }
}
