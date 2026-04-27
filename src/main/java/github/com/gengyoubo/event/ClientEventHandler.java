package github.com.gengyoubo.event;

import github.com.gengyoubo.MPG;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import github.com.gengyoubo.core.MPGKeyBoardCore;
import github.com.gengyoubo.item.data.IMPGKey;
import github.com.gengyoubo.network.Networking;
import github.com.gengyoubo.network.client.KeyPressPacket;

@Mod.EventBusSubscriber(modid = MPG.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
    private static final Minecraft MC = Minecraft.getInstance();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
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
}
