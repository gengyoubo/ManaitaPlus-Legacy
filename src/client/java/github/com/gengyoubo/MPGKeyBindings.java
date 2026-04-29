package github.com.gengyoubo;

import com.mojang.blaze3d.platform.InputConstants;
import github.com.gengyoubo.core.MPKeyBoardCore;
import github.com.gengyoubo.item.MPTypedRingItem;
import github.com.gengyoubo.item.data.IMPKey;
import github.com.gengyoubo.network.MPNetworking;
import github.com.gengyoubo.network.client.MPKeyPressPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public final class MPGKeyBindings {
    private MPGKeyBindings() {
    }

    public static void init() {
        MPKeyBoardCore.MESSAGE_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.manaita",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "key.categories.misc"
        ));
        MPKeyBoardCore.MESSAGE_ARMOR_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.manaita.armor",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                "key.categories.misc"
        ));

        migrateLegacyKeybindings();

        ClientTickEvents.END_CLIENT_TICK.register(MPGKeyBindings::onClientTick);
    }

    private static void migrateLegacyKeybindings() {
        if ("key.keyboard.x".equals(MPKeyBoardCore.MESSAGE_KEY.saveString())) {
            MPKeyBoardCore.MESSAGE_KEY.setKey(InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_C));
        }
    }

    private static void onClientTick(Minecraft client) {
        if (client.player == null) {
            return;
        }

        while (MPKeyBoardCore.MESSAGE_KEY.consumeClick()) {
            boolean shiftDown = Screen.hasShiftDown();
            if (!shiftDown && MPTypedRingItem.findPrimaryEquippedRing(client.player).isPresent()) {
                sendKeyPacket((byte) 0, false);
                continue;
            }
            ItemStack mainHandItem = client.player.getMainHandItem();
            if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof IMPKey keyItem) {
                keyItem.onManaitaKeyPressOnClient(mainHandItem, client.player);
            }
            sendKeyPacket((byte) 0, shiftDown);
        }

        while (MPKeyBoardCore.MESSAGE_ARMOR_KEY.consumeClick()) {
            for (ItemStack itemStack : client.player.getInventory().armor) {
                if (!itemStack.isEmpty() && itemStack.getItem() instanceof IMPKey keyItem) {
                    keyItem.onManaitaKeyPressOnClient(itemStack, client.player);
                }
            }
            sendKeyPacket((byte) 1, Screen.hasShiftDown());
        }
    }

    private static void sendKeyPacket(byte keyCode, boolean shiftDown) {
        MPKeyPressPacket packet = new MPKeyPressPacket(keyCode, shiftDown);
        var buf = MPNetworking.createBuf();
        packet.write(buf);
        ClientPlayNetworking.send(MPNetworking.KEY_PRESS, buf);
    }
}
