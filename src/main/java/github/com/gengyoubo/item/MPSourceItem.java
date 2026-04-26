package github.com.gengyoubo.item;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import github.com.gengyoubo.menu.MPCraftingMenu;
import github.com.gengyoubo.util.MPText;

import java.util.List;

public class MPSourceItem extends Item {
    public MPSourceItem() {
        super(new Item.Properties().fireResistant());
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.literal(MPText.manaita_mode.formatting(Component.translatable("item.source.name").getString()));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.literal(MPText.manaita_infinity.formatting(Component.translatable("info.source.1").getString())));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new ExtendedScreenHandlerFactory() {
                @Override
                public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                    buf.writeBlockPos(BlockPos.ZERO);
                }

                @Override
                public @NotNull Component getDisplayName() {
                    return Component.translatable("container.crafting");
                }

                @Override
                public @NotNull net.minecraft.world.inventory.AbstractContainerMenu createMenu(int windowId, @NotNull net.minecraft.world.entity.player.Inventory inventory, @NotNull Player menuPlayer) {
                    return new MPCraftingMenu(windowId, inventory, level);
                }
            });
        }
        return InteractionResultHolder.sidedSuccess(heldItem, level.isClientSide());
    }
}

