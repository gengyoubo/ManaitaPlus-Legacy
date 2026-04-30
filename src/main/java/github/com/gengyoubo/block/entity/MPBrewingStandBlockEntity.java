package github.com.gengyoubo.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import github.com.gengyoubo.MPGConfig;
import github.com.gengyoubo.block.MPBrewingStandBlock;
import github.com.gengyoubo.menu.MPBrewingStandMenu;
import github.com.gengyoubo.core.MPBlockEntityCore;

import java.util.Arrays;

public class MPBrewingStandBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, ExtendedScreenHandlerFactory {
    private static final int[] SLOTS_FOR_UP = new int[]{3};
    private static final int[] SLOTS_FOR_DOWN = new int[]{0, 1, 2, 3};
    private static final int[] SLOTS_FOR_SIDES = new int[]{0, 1, 2, 4};
    private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
    int brewTime;
    private boolean[] lastPotionCount;
    int fuel;
    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int p_59038_) {
            return switch (p_59038_) {
                case 0 -> MPBrewingStandBlockEntity.this.brewTime;
                case 1 -> MPBrewingStandBlockEntity.this.fuel;
                default -> 0;
            };
        }

        public void set(int p_59040_, int p_59041_) {
            switch (p_59040_) {
                case 0:
                    MPBrewingStandBlockEntity.this.brewTime = p_59041_;
                    break;
                case 1:
                    MPBrewingStandBlockEntity.this.fuel = p_59041_;
            }

        }

        public int getCount() {
            return 2;
        }
    };

    public MPBrewingStandBlockEntity(BlockPos p_155283_, BlockState p_155284_) {
        super(MPBlockEntityCore.BREWING_BLOCK_ENTITY.get(), p_155283_, p_155284_);
    }

    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.brewing_manaita");
    }

    public int getContainerSize() {
        return this.items.size();
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public static void serverTick(Level p_155286_, BlockPos p_155287_, BlockState p_155288_, MPBrewingStandBlockEntity p_155289_) {
        if (isBrewable(p_155289_.items)) {
            doBrew(p_155286_, p_155287_, p_155289_.items);
            setChanged(p_155286_, p_155287_, p_155288_);
        }

        boolean[] aboolean = p_155289_.getPotionBits();
        if (!Arrays.equals(aboolean, p_155289_.lastPotionCount)) {
            p_155289_.lastPotionCount = aboolean;
            BlockState blockstate = p_155288_;
            if (!(p_155288_.getBlock() instanceof MPBrewingStandBlock)) {
                return;
            }

            for (int i = 0; i < MPBrewingStandBlock.HAS_BOTTLE.length; ++i) {
                blockstate = blockstate.setValue(MPBrewingStandBlock.HAS_BOTTLE[i], aboolean[i]);
            }

            p_155286_.setBlock(p_155287_, blockstate, 2);
        }
    }

    private boolean[] getPotionBits() {
        boolean[] aboolean = new boolean[3];

        for(int i = 0; i < 3; ++i) {
            if (!this.items.get(i).isEmpty()) {
                aboolean[i] = true;
            }
        }

        return aboolean;
    }

    private static boolean isBrewable(NonNullList<ItemStack> p_155295_) {
        ItemStack itemstack = p_155295_.get(3);
        return !itemstack.isEmpty()
                && (!p_155295_.get(0).isEmpty() || !p_155295_.get(1).isEmpty() || !p_155295_.get(2).isEmpty());
    }

    private static void doBrew(Level p_155291_, BlockPos p_155292_, NonNullList<ItemStack> p_155293_) {
        for (int slotsForSide : SLOTS_FOR_SIDES) {
            ItemStack itemStack = p_155293_.get(slotsForSide);
            if (!itemStack.isEmpty()) {
                itemStack.setCount(itemStack.getCount() * MPGConfig.brewing_doubling_value);
            }
        }
        MPGBrewingLogicHelper.finishBrew(p_155291_, p_155292_.getX(), p_155292_.getY(), p_155292_.getZ(), p_155293_, 3);
        p_155291_.levelEvent(1035, p_155292_, 0);
    }

    public void load(@NotNull CompoundTag p_155297_) {
        super.load(p_155297_);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(p_155297_, this.items);
    }

    protected void saveAdditional(@NotNull CompoundTag p_187484_) {
        super.saveAdditional(p_187484_);
        ContainerHelper.saveAllItems(p_187484_, this.items);
    }

    public @NotNull ItemStack getItem(int p_58985_) {
        return p_58985_ >= 0 && p_58985_ < this.items.size() ? this.items.get(p_58985_) : ItemStack.EMPTY;
    }

    public @NotNull ItemStack removeItem(int p_58987_, int p_58988_) {
        return ContainerHelper.removeItem(this.items, p_58987_, p_58988_);
    }

    public @NotNull ItemStack removeItemNoUpdate(int p_59015_) {
        return ContainerHelper.takeItem(this.items, p_59015_);
    }

    public void setItem(int p_58993_, @NotNull ItemStack p_58994_) {
        if (p_58993_ >= 0 && p_58993_ < this.items.size()) {
            this.items.set(p_58993_, p_58994_);
        }

    }

    public boolean stillValid(@NotNull Player p_59000_) {
        return Container.stillValidBlockEntity(this, p_59000_);
    }

    public boolean canPlaceItem(int p_59017_, @NotNull ItemStack p_59018_) {
        return MPGBrewingLogicHelper.canPlaceItem(p_59017_, p_59018_, this.getItem(p_59017_));
    }

    public int @NotNull [] getSlotsForFace(@NotNull Direction p_59010_) {
        if (p_59010_ == Direction.UP) {
            return SLOTS_FOR_UP;
        } else {
            return p_59010_ == Direction.DOWN ? SLOTS_FOR_DOWN : SLOTS_FOR_SIDES;
        }
    }

    public boolean canPlaceItemThroughFace(int p_58996_, @NotNull ItemStack p_58997_, @Nullable Direction p_58998_) {
        return this.canPlaceItem(p_58996_, p_58997_);
    }

    public boolean canTakeItemThroughFace(int p_59020_, @NotNull ItemStack p_59021_, @NotNull Direction p_59022_) {
        return p_59020_ != 3 || p_59021_.is(Items.GLASS_BOTTLE);
    }

    public void clearContent() {
        this.items.clear();
    }

    protected @NotNull AbstractContainerMenu createMenu(int p_58990_, @NotNull Inventory p_58991_) {
        return new MPBrewingStandMenu(p_58990_, p_58991_, this, this.dataAccess);
    }

    @Override
    public void writeScreenOpeningData(net.minecraft.server.level.ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(this.worldPosition);
    }

}


