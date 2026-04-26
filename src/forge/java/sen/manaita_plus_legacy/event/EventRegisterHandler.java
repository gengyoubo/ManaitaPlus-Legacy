package sen.manaita_plus_legacy.event;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyAttributeCore;
import sen.manaita_plus_legacy.datagen.ManaitaPlusBlockStateProvider;
import sen.manaita_plus_legacy.datagen.ManaitaPlusItemModelProvider;
import sen.manaita_plus_legacy.loottable.ManaitaPlusLegacyLootTable;
import sen.manaita_plus_legacy.recipe.condition.EasyModeCondition;
import sen.manaita_plus_legacy.recipe.ingredient.ManaitaPlusLegacyNBTIngredient;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ManaitaPlusLegacy.MODID)
public class EventRegisterHandler {
    @SubscribeEvent
    public static void onRegisters(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
            CraftingHelper.register(new ResourceLocation("manaita_plus_legacy", "nbt"), ManaitaPlusLegacyNBTIngredient.Serializer.INSTANCE);
            CraftingHelper.register(EasyModeCondition.Serializer.INSTANCE);
        }
    }

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entityType -> event.add(entityType, ManaitaPlusLegacyAttributeCore.Type.get()));
    }


    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        gen.addProvider(event.includeServer(), new ManaitaPlusLegacyLootTable(gen.getPackOutput()));

        PackOutput packOutput = gen.getPackOutput();
        gen.addProvider(event.includeClient(), new ManaitaPlusBlockStateProvider(packOutput, event.getExistingFileHelper()));
        gen.addProvider(event.includeClient(), new ManaitaPlusItemModelProvider(packOutput, event.getExistingFileHelper()));
    }

}
