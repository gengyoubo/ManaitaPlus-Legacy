package github.com.gengyoubo.item;

import github.com.gengyoubo.compat.MPTrinketsCompat;
import github.com.gengyoubo.item.data.IMPKey;
import github.com.gengyoubo.item.portable.MPPortableMenuOpener;
import github.com.gengyoubo.util.MPNBTData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static github.com.gengyoubo.compat.trinkets.MPTrinketRingItem.getRingString;

public class MPTypedRingItem extends Item implements IMPKey {
    private final String translationPrefix;
    private final RingKind ringKind;

    public MPTypedRingItem(String translationPrefix, RingKind ringKind) {
        super(new Properties().stacksTo(1).fireResistant());
        this.translationPrefix = translationPrefix;
        this.ringKind = ringKind;
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        return Component.translatable(
                translationPrefix + getRingTypeKey(stack.getOrCreateTag().getInt(MPNBTData.ItemType)) + "name");
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide && MPTrinketsCompat.equipItem(player, stack)) {
                return InteractionResultHolder.success(stack);
            }
            return InteractionResultHolder.pass(stack);
        }

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            openWorkMenu(serverPlayer, stack);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void onManaitaKeyPress(ItemStack itemStack, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            openWorkMenu(serverPlayer, itemStack);
        }
    }

    public void openWorkMenu(ServerPlayer player, ItemStack stack) {
        switch (ringKind) {
            case CRAFTING -> MPPortableMenuOpener.openCrafting(player, stack, player.level());
            case FURNACE -> MPPortableMenuOpener.openFurnace(player, stack, player.level());
            case BREWING -> MPPortableMenuOpener.openBrewing(player, stack, player.level());
        }
    }

    private static String getRingTypeKey(int type) {
        return getRingString(type);
    }

    public enum RingKind {
        CRAFTING,
        FURNACE,
        BREWING
    }
}
