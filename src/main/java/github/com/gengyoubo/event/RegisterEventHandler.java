package github.com.gengyoubo.event;

import com.mojang.blaze3d.platform.InputConstants;
import github.com.gengyoubo.MPG;
import github.com.gengyoubo.core.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import github.com.gengyoubo.gui.BrewingStandScreen;
import github.com.gengyoubo.gui.CraftingManaitaScreen;
import github.com.gengyoubo.gui.FurnaceManaitaScreen;
import github.com.gengyoubo.blockEntity.RenderBrewingManaitaBlockEntity;
import github.com.gengyoubo.blockEntity.RenderCraftingManaitaBlockEntity;
import github.com.gengyoubo.blockEntity.RenderFurnaceManaitaBlockEntity;
import github.com.gengyoubo.entity.MPLightningBoltRenderer;
import github.com.gengyoubo.entity.RenderManaitaArrow;
import github.com.gengyoubo.util.MPGNBTData;

import static github.com.gengyoubo.core.MPGEntityCore.ManaitaArrow;
import static github.com.gengyoubo.core.MPGEntityCore.ManaitaLightningBolt;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MPG.MODID, value = Dist.CLIENT)
public class RegisterEventHandler {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(MPGMenuCore.CraftingManaita.get(), CraftingManaitaScreen::new);
            MenuScreens.register(MPGMenuCore.FurnaceManaita.get(), FurnaceManaitaScreen::new);
            MenuScreens.register(MPGMenuCore.BrewingStandManaita.get(), BrewingStandScreen::new);

            acceptTypePropertyFunction(
                    MPGBlockCore.CraftingBlockItem.get(),
                    MPGBlockCore.FurnaceBlockItem.get(),
                    MPGBlockCore.BrewingBlockItem.get(),
                    MPGBlockCore.HookBlockItem.get(),
                    MPGItemCore.ManaitaCraftingPortable.get(),
                    MPGItemCore.ManaitaFurnacePortable.get(),
                    MPGItemCore.ManaitaBrewingPortable.get()
            );
        });
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ManaitaLightningBolt.get(), MPLightningBoltRenderer::new);
        event.registerEntityRenderer(ManaitaArrow.get(), RenderManaitaArrow::new);

        BlockEntityRenderers.register(MPGBlockEntityCore.FURNACE_BLOCK_ENTITY.get(), RenderFurnaceManaitaBlockEntity::new);
        BlockEntityRenderers.register(MPGBlockEntityCore.BREWING_BLOCK_ENTITY.get(), RenderBrewingManaitaBlockEntity::new);
        BlockEntityRenderers.register(MPGBlockEntityCore.CRAFTING_BLOCK_ENTITY.get(), RenderCraftingManaitaBlockEntity::new);
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event)
    {
        MPGKeyBoardCore.MESSAGE_KEY = new KeyMapping("key.manaita", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 88, "key.categories.misc");
        MPGKeyBoardCore.MESSAGE_ARMOR_KEY = new KeyMapping("key.manaita.armor", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 89, "key.categories.misc");
        event.register(MPGKeyBoardCore.MESSAGE_KEY);
        event.register(MPGKeyBoardCore.MESSAGE_ARMOR_KEY);
    }

    @SuppressWarnings("deprecation")
    private static void acceptTypePropertyFunction(Item... items) {
        ResourceLocation location = new ResourceLocation(MPG.MODID, MPGNBTData.Type);
        ItemPropertyFunction typePropertyFunction = (stack, level, entity, seed) ->
                stack.hasTag() ? stack.getTag().getInt(MPGNBTData.ItemType) : 0.0F;
        for (Item item : items) {
            ItemProperties.register(item, location, typePropertyFunction);
        }
    }
}
