package sen.manaita_plus_legacy.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyKeyBoardCore;
import sen.manaita_plus_legacy.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.network.Networking;
import sen.manaita_plus_legacy.network.client.KeyPressPacket;

@Mod.EventBusSubscriber(modid = ManaitaPlusLegacy.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
    private static final Minecraft MC = Minecraft.getInstance();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (MC.player == null) return;
        if (ManaitaPlusLegacyKeyBoardCore.MESSAGE_KEY.isDown()) {
            ItemStack mainHandItem = MC.player.getMainHandItem();
            if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof IManaitaPlusLegacyKey keyItem) {
                keyItem.onManaitaKeyPressOnClient(mainHandItem, MC.player);
            }
            Networking.sendToServer(new KeyPressPacket((byte) 0));
        }
        if (ManaitaPlusLegacyKeyBoardCore.MESSAGE_ARMOR_KEY.isDown()) {
            for (ItemStack itemStack : MC.player.getInventory().armor) {
                if (!itemStack.isEmpty() && itemStack.getItem() instanceof IManaitaPlusLegacyKey keyItem) {
                    keyItem.onManaitaKeyPressOnClient(itemStack, MC.player);
                }
            }
            Networking.sendToServer(new KeyPressPacket((byte) 1));
        }
    }
}
