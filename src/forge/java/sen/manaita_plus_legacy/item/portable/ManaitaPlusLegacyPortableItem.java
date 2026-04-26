package sen.manaita_plus_legacy.item.portable;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import sen.manaita_plus_legacy.util.ManaitaPlusLegacyNBTData;

public abstract class ManaitaPlusLegacyPortableItem extends Item {
    private final String translationPrefix;

    protected ManaitaPlusLegacyPortableItem(String translationPrefix) {
        super(new Properties().defaultDurability(-1).fireResistant().stacksTo(1));
        this.translationPrefix = translationPrefix;
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        return Component.translatable(translationPrefix + stack.getOrCreateTag().getInt(ManaitaPlusLegacyNBTData.ItemType) + ".name");
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

    protected final void openPortableScreen(ServerPlayer serverPlayer, ItemStack itemInHand, Level level, String titleKey, PortableMenuFactory menuFactory) {
        NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
            @Override
            public @NotNull Component getDisplayName() {
                return Component.translatable(titleKey);
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
                return menuFactory.create(containerId, inventory, player, itemInHand, level);
            }
        });
    }

    @FunctionalInterface
    protected interface PortableMenuFactory {
        AbstractContainerMenu create(int containerId, Inventory inventory, Player player, ItemStack itemInHand, Level level);
    }
}
