package sen.manaita_plus_legacy.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
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
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.gui.BrewingStandScreen;
import sen.manaita_plus_legacy.gui.CraftingManaitaScreen;
import sen.manaita_plus_legacy.gui.FurnaceManaitaScreen;
import sen.manaita_plus_legacy.blockEntity.RenderBrewingManaitaBlockEntity;
import sen.manaita_plus_legacy.blockEntity.RenderCraftingManaitaBlockEntity;
import sen.manaita_plus_legacy.blockEntity.RenderFurnaceManaitaBlockEntity;
import sen.manaita_plus_legacy.entity.ManaitaPlusLightningBoltRenderer;
import sen.manaita_plus_legacy.entity.RenderManaitaArrow;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyKeyBoardCore;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyBlockEntityCore;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyBlockCore;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyItemCore;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyMenuCore;
import sen.manaita_plus_legacy.util.ManaitaPlusLegacyNBTData;

import static sen.manaita_plus_legacy.core.ManaitaPlusLegacyEntityCore.ManaitaArrow;
import static sen.manaita_plus_legacy.core.ManaitaPlusLegacyEntityCore.ManaitaLightningBolt;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ManaitaPlusLegacy.MODID, value = Dist.CLIENT)
public class RegisterEventHandler {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ManaitaPlusLegacyMenuCore.CraftingManaita.get(), CraftingManaitaScreen::new);
            MenuScreens.register(ManaitaPlusLegacyMenuCore.FurnaceManaita.get(), FurnaceManaitaScreen::new);
            MenuScreens.register(ManaitaPlusLegacyMenuCore.BrewingStandManaita.get(), BrewingStandScreen::new);

            acceptTypePropertyFunction(
                    ManaitaPlusLegacyBlockCore.CraftingBlockItem.get(),
                    ManaitaPlusLegacyBlockCore.FurnaceBlockItem.get(),
                    ManaitaPlusLegacyBlockCore.BrewingBlockItem.get(),
                    ManaitaPlusLegacyBlockCore.HookBlockItem.get(),
                    ManaitaPlusLegacyItemCore.ManaitaCraftingPortable.get(),
                    ManaitaPlusLegacyItemCore.ManaitaFurnacePortable.get(),
                    ManaitaPlusLegacyItemCore.ManaitaBrewingPortable.get()
            );
        });
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ManaitaLightningBolt.get(), ManaitaPlusLightningBoltRenderer::new);
        event.registerEntityRenderer(ManaitaArrow.get(), RenderManaitaArrow::new);

        BlockEntityRenderers.register(ManaitaPlusLegacyBlockEntityCore.FURNACE_BLOCK_ENTITY.get(), RenderFurnaceManaitaBlockEntity::new);
        BlockEntityRenderers.register(ManaitaPlusLegacyBlockEntityCore.BREWING_BLOCK_ENTITY.get(), RenderBrewingManaitaBlockEntity::new);
        BlockEntityRenderers.register(ManaitaPlusLegacyBlockEntityCore.CRAFTING_BLOCK_ENTITY.get(), RenderCraftingManaitaBlockEntity::new);
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event)
    {
        ManaitaPlusLegacyKeyBoardCore.MESSAGE_KEY = new KeyMapping("key.manaita", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 88, "key.categories.misc");
        ManaitaPlusLegacyKeyBoardCore.MESSAGE_ARMOR_KEY = new KeyMapping("key.manaita.armor", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 89, "key.categories.misc");
        event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_KEY);
        event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_ARMOR_KEY);
    }

    private static void acceptTypePropertyFunction(Item... items) {
        ResourceLocation location = new ResourceLocation(ManaitaPlusLegacy.MODID, ManaitaPlusLegacyNBTData.Type);
        ClampedItemPropertyFunction typePropertyFunction = (stack, level, entity, seed) -> {
            if (stack.getTag() != null) {
                return stack.hasTag() ? stack.getTag().getInt(ManaitaPlusLegacyNBTData.ItemType) : 0;
            }
            return 0;
        };
        for (Item item : items) {
            ItemProperties.register(item, location, typePropertyFunction);
        }
    }
}
