package github.com.gengyoubo.MPG.event;

import github.com.gengyoubo.MPG.MPG;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import github.com.gengyoubo.MPG.core.MPGKeyBoardCore;
import github.com.gengyoubo.MPG.item.data.IMPGKey;
import github.com.gengyoubo.MPG.item.ring.MPGCuriosHelper;
import github.com.gengyoubo.MPG.network.Networking;
import github.com.gengyoubo.MPG.network.client.KeyPressPacket;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = MPG.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
    private static final Minecraft MC = Minecraft.getInstance();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Player player = MC.player;
        if (player == null) {
            return;
        }

        if (MPGKeyBoardCore.MESSAGE_KEY.isDown()) {
            Optional<ItemStack> keyStack = MPGCuriosHelper.findFirstMatching(
                    player,
                    stack -> stack.getItem() instanceof IMPGKey
            );

            if (keyStack.isPresent()) {
                ItemStack stack = keyStack.get();
                ((IMPGKey) stack.getItem()).onManaitaKeyPressOnClient(stack, player);
            } else {
                ItemStack mainHandStack = player.getMainHandItem();

                if (!mainHandStack.isEmpty() && mainHandStack.getItem() instanceof IMPGKey keyItem) {
                    keyItem.onManaitaKeyPressOnClient(mainHandStack, player);
                }
            }

            Networking.sendToServer(new KeyPressPacket((byte) 0));
        }

        if (MPGKeyBoardCore.MESSAGE_ARMOR_KEY.isDown()) {
            for (ItemStack stack : player.getInventory().armor) {
                if (!stack.isEmpty() && stack.getItem() instanceof IMPGKey keyItem) {
                    keyItem.onManaitaKeyPressOnClient(stack, player);
                }
            }

            Networking.sendToServer(new KeyPressPacket((byte) 1));
        }
    }
}
