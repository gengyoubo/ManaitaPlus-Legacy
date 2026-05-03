package github.com.gengyoubo.MPG.item.portable;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.jetbrains.annotations.NotNull;
import github.com.gengyoubo.MPG.block.entity.MPGBrewingLogicHelper;
import github.com.gengyoubo.MPG.core.MPGBlockEntityCore;
import github.com.gengyoubo.MPG.core.MPGBlockCore;
import github.com.gengyoubo.MPG.menu.MPGBrewingStandMenu;
import github.com.gengyoubo.MPG.util.MPGItemStackData;

import java.util.Arrays;

public class MPGBrewingPortable extends MPGPortableItem {
    public MPGBrewingPortable() {
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
        private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
        private boolean[] lastPotionCount;
        private Item ingredient;
        int brewTime;
        int fuel;
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
            super(MPGBlockEntityCore.BREWING_BLOCK_ENTITY.get(), player.blockPosition(), MPGBlockCore.BrewingBlock.get().defaultBlockState());
            this.player = player;
            this.stack = stack;
            CompoundTag tag = MPGItemStackData.getTag(stack);
            if (tag != null) {
                load(tag);
            }
        }

        protected @NotNull net.minecraft.network.chat.Component getDefaultName() {
            return net.minecraft.network.chat.Component.translatable("container.brewing_manaita");
        }

        public int getContainerSize() {
            return this.items.size();
        }

        protected @NotNull NonNullList<ItemStack> getItems() {
            return this.items;
        }

        protected void setItems(@NotNull NonNullList<ItemStack> items) {
            this.items = items;
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
            if (ingredient.isEmpty() || !PotionBrewing.isIngredient(ingredient)) {
                return false;
            }
            for (int i = 0; i < 3; i++) {
                ItemStack input = items.get(i);
                if (!input.isEmpty() && PotionBrewing.hasMix(input, ingredient)) {
                    return true;
                }
            }
            return false;
        }

        private void doBrew(Level level, NonNullList<ItemStack> items) {
            if (net.minecraftforge.event.ForgeEventFactory.onPotionAttemptBrew(items)) return;
            ItemStack ingredient = items.get(3);
            PotionBrewing potionBrewing = new PotionBrewing();
            for (int i = 0; i < 3; i++) {
                ItemStack brewed = potionBrewing.mix(ingredient, items.get(i));
                if (!brewed.isEmpty()) {
                    brewed.setCount(Math.max(1, brewed.getCount() * 64));
                }
                items.set(i, brewed);
            }
            MPGBrewingLogicHelper.finishBrew(level, player.getX(), player.getY(), player.getZ(), items, 3);
        }

        public void load(@NotNull CompoundTag tag) {
            super.load(tag);
            this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(tag, this.items);
            this.brewTime = tag.getShort("BrewTime");
            if (this.brewTime > 0) {
                this.ingredient = this.items.get(3).getItem();
            }
            this.fuel = tag.getByte("Fuel");
        }

        protected void saveAdditional(@NotNull CompoundTag tag) {
            super.saveAdditional(tag);
            tag.putShort("BrewTime", (short) this.brewTime);
            ContainerHelper.saveAllItems(tag, this.items);
            tag.putByte("Fuel", (byte) this.fuel);
        }

        public boolean isEmpty() {
            for (ItemStack itemStack : this.items) {
                if (!itemStack.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        public boolean stillValid(@NotNull Player player) {
            return true;
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
                if (this.fuel <= 0 && this.items.get(4).is(net.minecraft.world.item.Items.BLAZE_POWDER)) {
                    this.fuel = 20;
                    this.items.get(4).shrink(1);
                }
                if (isBrewable(this.items) && this.fuel > 0) {
                    this.fuel--;
                    this.brewTime = 400;
                    this.ingredient = this.items.get(3).getItem();
                    doBrew(player.level(), this.items);
                }

                boolean[] bits = this.getPotionBits();
                if (!Arrays.equals(bits, this.lastPotionCount)) {
                    this.lastPotionCount = bits;
                }
            }
            CompoundTag tag = MPGItemStackData.getTag(this.stack);
            if (tag == null) {
                tag = new CompoundTag();
            }
            saveAdditional(tag);
            MPGItemStackData.setTag(this.stack, tag);
        }

        public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
            return MPGBrewingLogicHelper.canPlaceItem(index, stack, this.getItem(index));
        }

        public void clearContent() {
            this.items = NonNullList.withSize(5, ItemStack.EMPTY);
        }

        public @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory) {
            return new MPGBrewingStandMenu(containerId, inventory, this, this.dataAccess);
        }
    }
}
