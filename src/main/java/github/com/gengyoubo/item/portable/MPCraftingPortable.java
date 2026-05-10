package github.com.gengyoubo.item.portable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MPCraftingPortable extends MPGPortableItem {
    public MPCraftingPortable() {
        super("item.portableCrafting.");
    }

    @Override
    protected void openPortableMenu(ServerPlayer serverPlayer, ItemStack itemInHand, Level level) {
        MPPortableMenuOpener.openCrafting(serverPlayer, itemInHand, level);
    }
}



