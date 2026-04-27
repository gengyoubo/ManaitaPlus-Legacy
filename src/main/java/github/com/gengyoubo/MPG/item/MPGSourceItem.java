package github.com.gengyoubo.MPG.item;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import github.com.gengyoubo.MPG.menu.MPGCraftingMenu;
import github.com.gengyoubo.MPG.util.MPText;

import java.util.List;

public class MPGSourceItem extends Item {
    public MPGSourceItem() {
        super(new Item.Properties().fireResistant());
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.literal(MPText.manaita_mode.formatting(I18n.get("item.source.name")));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.literal(MPText.manaita_infinity.formatting(I18n.get("info.source.1"))));
    }

    @OnlyIn(net.minecraftforge.api.distmarker.Dist.CLIENT)
    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(
                    serverPlayer,
                    new SimpleMenuProvider(
                            (windowId, inventory, menuPlayer) -> new MPGCraftingMenu(windowId, inventory, level),
                            Component.translatable("container.crafting"))
            );
        }
        return InteractionResultHolder.sidedSuccess(heldItem, level.isClientSide());
    }
}
