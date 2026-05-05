package github.com.gengyoubo.item.portable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import github.com.gengyoubo.menu.MPCraftingMenu;

public final class MPPortableMenuOpener {
    private MPPortableMenuOpener() {
    }

    public static void openCrafting(ServerPlayer serverPlayer, ItemStack stack, Level level) {
        MPGPortableScreenHelper.openPortableScreen(serverPlayer, stack, level, "container.crafting_manaita",
                (containerId, inventory, player, heldStack, world) -> new MPCraftingMenu(containerId, inventory, player.level()));
    }

    public static void openFurnace(ServerPlayer serverPlayer, ItemStack stack, Level level) {
        MPGPortableScreenHelper.openPortableScreen(serverPlayer, stack, level, "container.furnace_manaita",
                (containerId, inventory, player, heldStack, world) -> {
                    MPFurnacePortable.MPFurnaceBlockEntity block = new MPFurnacePortable.MPFurnaceBlockEntity(player, heldStack);
                    return block.createMenu(containerId, inventory, player);
                });
    }

    public static void openBrewing(ServerPlayer serverPlayer, ItemStack stack, Level level) {
        MPGPortableScreenHelper.openPortableScreen(serverPlayer, stack, level, "container.brewing_manaita",
                (containerId, inventory, player, heldStack, world) -> {
                    MPBrewingPortable.MPBrewingStandBlockEntity blockEntity = new MPBrewingPortable.MPBrewingStandBlockEntity(inventory.player, heldStack);
                    return blockEntity.createMenu(containerId, inventory, player);
                });
    }
}
