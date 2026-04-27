package github.com.gengyoubo.MPG.event;

import github.com.gengyoubo.MPG.MPG;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLEnvironment;
import github.com.gengyoubo.MPG.core.MPGKeyBoardCore;
import github.com.gengyoubo.MPG.item.data.IMPGKey;
import github.com.gengyoubo.MPG.network.Networking;
import github.com.gengyoubo.MPG.network.client.KeyPressPacket;

import java.util.List;

public class ClientEventHandler {
    private static final Minecraft MC = Minecraft.getInstance();
    private static boolean registered = true;
    private static boolean autoLoadRequested = true;
    private static boolean autoLoadCheckLogged = true;

    public static void register() {
        if (registered) {
            return;
        }
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        registered = true;
        MPG.LOGGER.info("Registered client runtime event handlers");
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onKeyInput(InputEvent.Key event) {
        if (MC.player == null) return;
        if (MPGKeyBoardCore.MESSAGE_KEY.isDown()) {
            ItemStack mainHandItem = MC.player.getMainHandItem();
            if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof IMPGKey keyItem) {
                keyItem.onManaitaKeyPressOnClient(mainHandItem, MC.player);
            }
            Networking.sendToServer(new KeyPressPacket((byte) 0));
        }
        if (MPGKeyBoardCore.MESSAGE_ARMOR_KEY.isDown()) {
            for (ItemStack itemStack : MC.player.getInventory().armor) {
                if (!itemStack.isEmpty() && itemStack.getItem() instanceof IMPGKey keyItem) {
                    keyItem.onManaitaKeyPressOnClient(itemStack, MC.player);
                }
            }
            Networking.sendToServer(new KeyPressPacket((byte) 1));
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (FMLEnvironment.production) {
            return;
        }
        if (MC.level != null || !(MC.screen instanceof TitleScreen)) {
            return;
        }
        if (!autoLoadCheckLogged) {
            autoLoadCheckLogged = true;
            MPG.LOGGER.info("Detected title screen in dev environment, checking for first-world auto-load");
        }
        if (autoLoadRequested) {
            return;
        }

        autoLoadRequested = true;
        tryAutoLoadFirstWorld();
    }

    private static void tryAutoLoadFirstWorld() {
        LevelStorageSource levelSource = MC.getLevelSource();
        LevelStorageSource.LevelCandidates candidates;
        try {
            candidates = levelSource.findLevelCandidates();
        } catch (LevelStorageException exception) {
            MPG.LOGGER.warn("Failed to enumerate local worlds for dev auto-load", exception);
            return;
        }

        if (candidates.isEmpty()) {
            MPG.LOGGER.info("No local worlds found, skipping dev auto-load");
            return;
        }

        levelSource.loadLevelSummaries(candidates).thenAccept((summaries) -> MC.execute(() -> loadFirstWorld(summaries)));
    }

    private static void loadFirstWorld(List<LevelSummary> summaries) {
        if (MC.level != null || !(MC.screen instanceof TitleScreen) || summaries.isEmpty()) {
            return;
        }

        LevelSummary firstWorld = summaries.get(0);
        MPG.LOGGER.info("Dev auto-loading first world: {}", firstWorld.getLevelId());
        MC.createWorldOpenFlows().loadLevel(MC.screen, firstWorld.getLevelId());
    }
}
