package sen.manaita_plus_legacy.item.portable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sen.manaita_plus_legacy.menu.ManaitaPlusLegacyCraftingMenu;

public class ManaitaPlusLegacyCraftingPortable extends ManaitaPlusLegacyPortableItem {
    public ManaitaPlusLegacyCraftingPortable() {
        super("item.portableCrafting.");
    }

    @Override
    protected void openPortableMenu(ServerPlayer serverPlayer, ItemStack itemInHand, Level level) {
        openPortableScreen(serverPlayer, itemInHand, level, "container.crafting_manaita",
                (containerId, inventory, player, heldStack, world) -> new ManaitaPlusLegacyCraftingMenu(containerId, inventory, player.level()));
    }
}


