package github.com.gengyoubo.item.portable;

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
import github.com.gengyoubo.util.MPNBTData;

import java.util.List;

public abstract class MPGPortableItem extends Item {
    private final String translationPrefix;

    protected MPGPortableItem(String translationPrefix) {
        super(new Properties().defaultDurability(-1).fireResistant().stacksTo(1));
        this.translationPrefix = translationPrefix;
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        return Component.translatable(translationPrefix + stack.getOrCreateTag().getInt(MPNBTData.ItemType) + ".name");
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (player instanceof ServerPlayer serverPlayer) {
            openPortableMenu(serverPlayer, itemInHand, level);
        }
        return super.use(level, player, hand);
    }

    protected abstract void openPortableMenu(ServerPlayer serverPlayer, ItemStack itemInHand, Level level);
}
