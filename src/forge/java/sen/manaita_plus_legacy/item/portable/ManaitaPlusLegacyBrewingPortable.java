package sen.manaita_plus_legacy.item.portable;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.jetbrains.annotations.NotNull;
import sen.manaita_plus_legacy.block.entity.ManaitaPlusLegacyBrewingLogicHelper;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyBlockEntityCore;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyBlockCore;
import sen.manaita_plus_legacy.menu.ManaitaPlusLegacyBrewingStandMenu;

import java.util.Arrays;

public class ManaitaPlusLegacyBrewingPortable extends ManaitaPlusLegacyPortableItem {
    public ManaitaPlusLegacyBrewingPortable() {
        super("item.portableBrewing.");
    }

    @Override
    protected void openPortableMenu(ServerPlayer serverPlayer, ItemStack itemInHand, Level level) {
        openPortableScreen(serverPlayer, itemInHand, level, "container.brewing_manaita",
                (containerId, inventory, player, heldStack, world) -> {
                    ManaitaPlusBrewingStandBlockEntity blockEntity = new ManaitaPlusBrewingStandBlockEntity(inventory.player, heldStack);
                    return blockEntity.createMenu(containerId, inventory, player);
                });
    }

    public static class ManaitaPlusBrewingStandBlockEntity extends BaseContainerBlockEntity {
        private static final int[] SLOTS_FOR_SIDES = new int[]{0, 1, 2, 4};
        private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
        private boolean[] lastPotionCount;
        private final Player player;
        private final ItemStack stack;

        protected final ContainerData dataAccess = new ContainerData() {
            public int get(int index) {
                return 0;
            }

            public void set(int index, int value) {
            }

            public int getCount() {
                return 2;
            }
        };

        public ManaitaPlusBrewingStandBlockEntity(Player player, ItemStack stack) {
            super(ManaitaPlusLegacyBlockEntityCore.BREWING_BLOCK_ENTITY.get(), player.blockPosition(), ManaitaPlusLegacyBlockCore.BrewingBlock.get().defaultBlockState());
            this.player = player;
            this.stack = stack;
            load(stack.getOrCreateTag());
        }

        protected @NotNull net.minecraft.network.chat.Component getDefaultName() {
            return net.minecraft.network.chat.Component.translatable("container.brewing_manaita");
        }

        public int getContainerSize() {
            return this.items.size();
        }

        public boolean isEmpty() {
            for (ItemStack itemstack : this.items) {
                if (!itemstack.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        private boolean[] getPotionBits() {
            boolean[] bits = new boolean[3];
            for (int i = 0; i < 3; ++i) {
                if (!this.items.get(i).isEmpty()) {
                    bits[i] = true;
                }
            }
            return bits;
        }

        private static boolean isBrewable(NonNullList<ItemStack> items) {
            ItemStack ingredient = items.get(3);
            return !ingredient.isEmpty() && net.minecraftforge.common.brewing.BrewingRecipeRegistry.canBrew(items, ingredient, SLOTS_FOR_SIDES);
        }

        private void doBrew(Level level, NonNullList<ItemStack> items) {
            if (net.minecraftforge.event.ForgeEventFactory.onPotionAttemptBrew(items)) return;
            ItemStack ingredient = items.get(3);

            net.minecraftforge.common.brewing.BrewingRecipeRegistry.brewPotions(items, ingredient, SLOTS_FOR_SIDES);
            for (int slot : SLOTS_FOR_SIDES) {
                ItemStack itemStack = items.get(slot);
                if (ingredient != ItemStack.EMPTY) itemStack.setCount(itemStack.getCount() * 64);
            }
            ManaitaPlusLegacyBrewingLogicHelper.finishBrew(level, player.getX(), player.getY(), player.getZ(), items, 3);
        }

        public void load(@NotNull CompoundTag tag) {
            super.load(tag);
            this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(tag, this.items);
        }

        protected void saveAdditional(@NotNull CompoundTag tag) {
            super.saveAdditional(tag);
            ContainerHelper.saveAllItems(tag, this.items);
        }

        public @NotNull ItemStack getItem(int index) {
            return index >= 0 && index < this.items.size() ? this.items.get(index) : ItemStack.EMPTY;
        }

        public @NotNull ItemStack removeItem(int index, int count) {
            return ContainerHelper.removeItem(this.items, index, count);
        }

        public @NotNull ItemStack removeItemNoUpdate(int index) {
            return ContainerHelper.takeItem(this.items, index);
        }

        public void setItem(int index, @NotNull ItemStack stack) {
            if (index >= 0 && index < this.items.size()) {
                this.items.set(index, stack);
                if (isBrewable(this.items)) {
                    doBrew(player.level(), this.items);
                }

                boolean[] bits = this.getPotionBits();
                if (!Arrays.equals(bits, this.lastPotionCount)) {
                    this.lastPotionCount = bits;
                }
            }
            saveAdditional(this.stack.getOrCreateTag());
        }

        public boolean stillValid(@NotNull Player player) {
            return true;
        }

        public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
            return ManaitaPlusLegacyBrewingLogicHelper.canPlaceItem(index, stack, this.getItem(index));
        }

        public void clearContent() {
            this.items.clear();
        }

        protected @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory) {
            return new ManaitaPlusLegacyBrewingStandMenu(containerId, inventory, this, this.dataAccess);
        }
    }
}
