package github.com.gengyoubo.event;

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
import github.com.gengyoubo.MPG;
import github.com.gengyoubo.core.MPGAttributeCore;
import github.com.gengyoubo.datagen.MPBlockStateProvider;
import github.com.gengyoubo.datagen.MPItemModelProvider;
import github.com.gengyoubo.loottable.MPGLootTable;
import github.com.gengyoubo.recipe.condition.EasyModeCondition;
import github.com.gengyoubo.recipe.ingredient.MPGNBTIngredient;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MPG.MODID)
public class EventRegisterHandler {
    @SubscribeEvent
    public static void onRegisters(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
            CraftingHelper.register(new ResourceLocation("manaita_plus_general", "nbt"), MPGNBTIngredient.Serializer.INSTANCE);
            CraftingHelper.register(EasyModeCondition.Serializer.INSTANCE);
        }
    }

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entityType -> event.add(entityType, MPGAttributeCore.Type.get()));
    }


    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        gen.addProvider(event.includeServer(), new MPGLootTable(gen.getPackOutput()));

        PackOutput packOutput = gen.getPackOutput();
        gen.addProvider(event.includeClient(), new MPBlockStateProvider(packOutput, event.getExistingFileHelper()));
        gen.addProvider(event.includeClient(), new MPItemModelProvider(packOutput, event.getExistingFileHelper()));
    }

}
