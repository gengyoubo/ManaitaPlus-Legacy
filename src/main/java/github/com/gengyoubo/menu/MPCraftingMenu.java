package github.com.gengyoubo.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MPCraftingMenu extends CraftingMenu {
    private boolean alwaysValid;

    @SuppressWarnings("unused")
    public MPCraftingMenu(int containerId, Inventory inventory, BlockPos blockPos) {
        this(containerId, inventory, ContainerLevelAccess.create(inventory.player.level(), blockPos));
        this.alwaysValid = true;
    }

    public MPCraftingMenu(int containerId, Inventory inventory, Level level) {
        this(containerId, inventory, ContainerLevelAccess.create(level, BlockPos.ZERO));
        this.alwaysValid = true;
    }

    public MPCraftingMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(containerId, inventory, access);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return alwaysValid || super.stillValid(player);
    }
}
