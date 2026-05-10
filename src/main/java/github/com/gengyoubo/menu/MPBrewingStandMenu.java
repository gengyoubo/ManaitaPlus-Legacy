package github.com.gengyoubo.menu;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.jetbrains.annotations.NotNull;
import github.com.gengyoubo.core.MPMenuCore;

public class MPBrewingStandMenu extends AbstractContainerMenu {
    private final Container brewingStand;
    private final ContainerData brewingStandData;
    private final Slot ingredientSlot;
    private static final int POTION_START = 0;
    private static final int POTION_END = 3;
    private static final int INGREDIENT_SLOT = 3;
    private static final int FUEL_SLOT = 4;
    private static final int PLAYER_INV_START = 5;
    private static final int PLAYER_INV_END = 32;
    private static final int HOTBAR_START = 32;
    private static final int HOTBAR_END = 41;
    @SuppressWarnings("unused")
    public MPBrewingStandMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, new SimpleContainer(5), new SimpleContainerData(2));
    }

    public MPBrewingStandMenu(int p_39093_, Inventory p_39094_, Container p_39095_, ContainerData p_39096_) {
        super(MPMenuCore.BrewingStandManaita, p_39093_);
        checkContainerSize(p_39095_, 5);
        checkContainerDataCount(p_39096_, 2);
        this.brewingStand = p_39095_;
        this.brewingStandData = p_39096_;
        this.addSlot(new PotionSlot(p_39095_, 0, 56, 51));
        this.addSlot(new PotionSlot(p_39095_, 1, 79, 58));
        this.addSlot(new PotionSlot(p_39095_, 2, 102, 51));
        this.ingredientSlot = this.addSlot(new IngredientsSlot(p_39095_, 3, 79, 17));
        this.addSlot(new FuelSlot(p_39095_, 4, 17, 17));
        this.addDataSlots(p_39096_);

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(p_39094_, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(p_39094_, k, 8 + k * 18, 142));
        }
    }

    public boolean stillValid(@NotNull Player p_39098_) {
        return this.brewingStand.stillValid(p_39098_);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = this.slots.get(index);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        if (isContainerSlot(index)) {
            if (!this.moveItemStackTo(stack, PLAYER_INV_START, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }

            slot.onQuickCraft(stack, original);
        } else if (!moveFromInventory(stack, index)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);

        return original;
    }

    private boolean moveFromInventory(ItemStack stack, int index) {
        if (FuelSlot.mayPlaceItem(stack)) {
            return this.moveItemStackTo(
                    stack,
                    FUEL_SLOT,
                    FUEL_SLOT + 1,
                    false
            );
        }

        if (this.ingredientSlot.mayPlace(stack)) {
            return this.moveItemStackTo(
                    stack,
                    INGREDIENT_SLOT,
                    INGREDIENT_SLOT + 1,
                    false
            );
        }

        if (PotionSlot.mayPlaceItem(stack)) {
            return this.moveItemStackTo(
                    stack,
                    POTION_START,
                    POTION_END,
                    false
            );
        }

        if (index >= PLAYER_INV_START && index < PLAYER_INV_END) {
            return this.moveItemStackTo(
                    stack,
                    HOTBAR_START,
                    HOTBAR_END,
                    false
            );
        }

        if (index >= HOTBAR_START && index < HOTBAR_END) {
            return this.moveItemStackTo(
                    stack,
                    PLAYER_INV_START,
                    PLAYER_INV_END,
                    false
            );
        }

        return false;
    }

    private static boolean isContainerSlot(int index) {
        return index >= POTION_START && index < PLAYER_INV_START;
    }

    public int getFuel() {
        return this.brewingStandData.get(1);
    }

    public int getBrewingTicks() {
        return this.brewingStandData.get(0);
    }

    static class FuelSlot extends Slot {
        public FuelSlot(Container p_39105_, int p_39106_, int p_39107_, int p_39108_) {
            super(p_39105_, p_39106_, p_39107_, p_39108_);
        }

        public boolean mayPlace(@NotNull ItemStack p_39111_) {
            return mayPlaceItem(p_39111_);
        }

        public static boolean mayPlaceItem(ItemStack p_39113_) {
            return p_39113_.is(Items.BLAZE_POWDER);
        }

        public int getMaxStackSize() {
            return 64;
        }
    }

    static class IngredientsSlot extends Slot {
        public IngredientsSlot(Container p_39115_, int p_39116_, int p_39117_, int p_39118_) {
            super(p_39115_, p_39116_, p_39117_, p_39118_);
        }

        public boolean mayPlace(@NotNull ItemStack p_39121_) {
            return PotionBrewing.isIngredient(p_39121_);
        }

        public int getMaxStackSize() {
            return 64;
        }
    }

    static class PotionSlot extends Slot {
        public PotionSlot(Container p_39123_, int p_39124_, int p_39125_, int p_39126_) {
            super(p_39123_, p_39124_, p_39125_, p_39126_);
        }

        public boolean mayPlace(@NotNull ItemStack p_39132_) {
            return mayPlaceItem(p_39132_);
        }

        public int getMaxStackSize() {
            return 1;
        }

        public void onTake(@NotNull Player p_150499_, @NotNull ItemStack p_150500_) {
            Potion potion = PotionUtils.getPotion(p_150500_);
            if (p_150499_ instanceof ServerPlayer) {
                CriteriaTriggers.BREWED_POTION.trigger((ServerPlayer)p_150499_, potion);
            }

            super.onTake(p_150499_, p_150500_);
        }

        public static boolean mayPlaceItem(ItemStack p_39134_) {
            return p_39134_.is(Items.POTION) || p_39134_.is(Items.SPLASH_POTION) || p_39134_.is(Items.LINGERING_POTION) || p_39134_.is(Items.GLASS_BOTTLE);
        }
    }
}

