package github.com.gengyoubo.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import github.com.gengyoubo.MPGConfig;
import github.com.gengyoubo.core.MPBlockEntityCore;
import github.com.gengyoubo.menu.MPFurnaceMenu;

public class MPFurnaceBlockEntity extends AbstractFurnaceBlockEntity implements ExtendedScreenHandlerFactory {
    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{2, 1};
    private static final int[] SLOTS_FOR_SIDES = new int[]{1};
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheck;

    public MPFurnaceBlockEntity(BlockPos p_155545_, BlockState p_155546_) {
        super(MPBlockEntityCore.FURNACE_BLOCK_ENTITY.get(), p_155545_, p_155546_, RecipeType.SMELTING);
        this.quickCheck = RecipeManager.createCheck(RecipeType.SMELTING);
    }

    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.furnace_manaita");
    }

    protected @NotNull AbstractContainerMenu createMenu(int p_59293_, @NotNull Inventory p_59294_) {
        return new MPFurnaceMenu(p_59293_, p_59294_, this, this.dataAccess);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(this.worldPosition);
    }

    @Override
    public int getMaxStackSize() {
        return Integer.MAX_VALUE;
    }

    public void load(@NotNull CompoundTag p_155025_) {
        super.load(p_155025_);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(p_155025_, this.items);
        CompoundTag compoundtag = p_155025_.getCompound("RecipesUsed");

        for(String s : compoundtag.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
        }

    }

    protected void saveAdditional(@NotNull CompoundTag p_187452_) {
        super.saveAdditional(p_187452_);
        ContainerHelper.saveAllItems(p_187452_, this.items);
        CompoundTag compoundtag = new CompoundTag();
        this.recipesUsed.forEach((p_187449_, p_187450_) -> compoundtag.putInt(p_187449_.toString(), p_187450_));
        p_187452_.put("RecipesUsed", compoundtag);
    }


    public static void serverTick(Level p_155014_, BlockPos p_155015_, BlockState p_155016_, MPFurnaceBlockEntity entity) {
        boolean flag1 = false;

        if (!entity.items.get(0).isEmpty()) {
            Recipe<?> recipe = entity.quickCheck.getRecipeFor(entity, p_155014_).orElse(null);
            while (entity.canBurn(p_155014_.registryAccess(), recipe, entity.items)) {
                if (entity.burn(p_155014_.registryAccess(), recipe, entity.items)) {
                    entity.setRecipeUsed(recipe);
                }
                flag1 = true;
            }
        }


        if (flag1) {
            setChanged(p_155014_, p_155015_, p_155016_);
        }
    }

    private boolean canBurn(RegistryAccess p_266924_, @Nullable Recipe<?> p_155006_, NonNullList<ItemStack> p_155007_) {
        return MPGFurnaceLogicHelper.canBurn(p_266924_, p_155006_, p_155007_, this);
    }

    private boolean burn(RegistryAccess p_266740_, @Nullable Recipe<?> p_266780_, NonNullList<ItemStack> p_267073_) {
        if (p_266780_ != null && this.canBurn(p_266740_, p_266780_, p_267073_)) {
            ItemStack itemstack = p_267073_.get(0);
            ItemStack itemstack1 = MPGFurnaceLogicHelper.assembleResult(p_266780_, this, p_266740_);
            ItemStack itemstack2 = p_267073_.get(2);
            if (itemstack2.isEmpty()) {
                ItemStack copy = itemstack1.copy();
                copy.setCount(copy.getCount() * MPGConfig.furnace_doubling_value);
                p_267073_.set(2, copy);
            } else if (itemstack2.is(itemstack1.getItem())) {
                itemstack2.grow(itemstack1.getCount() * 64);
            }

            itemstack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    protected int getBurnDuration(@NotNull ItemStack p_58343_) {
        return 0;
    }

    public int @NotNull [] getSlotsForFace(@NotNull Direction p_58363_) {
        if (p_58363_ == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return p_58363_ == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
        }
    }

    public boolean canPlaceItemThroughFace(int p_58336_, @NotNull ItemStack p_58337_, @Nullable Direction p_58338_) {
        return this.canPlaceItem(p_58336_, p_58337_);
    }

    public void setItem(int p_58333_, ItemStack p_58334_) {
        ItemStack itemstack = this.items.get(p_58333_);
        boolean flag = !p_58334_.isEmpty() && ItemStack.isSameItemSameTags(itemstack, p_58334_);
        this.items.set(p_58333_, p_58334_);

        if (p_58333_ == 0 && !flag) {
            this.setChanged();
        }

    }

    public boolean stillValid(@NotNull Player p_58340_) {
        return true;
    }

    public boolean canPlaceItem(int p_58389_, @NotNull ItemStack p_58390_) {
        return p_58389_ != 2;
    }

    public void setRecipeUsed(@Nullable Recipe<?> p_58345_) {
        if (p_58345_ != null) {
            ResourceLocation resourcelocation = p_58345_.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
        }

    }

    public void awardUsedRecipesAndPopExperience(@NotNull ServerPlayer p_155004_) {
        MPGFurnaceLogicHelper.awardUsedRecipesAndPopExperience(p_155004_, this.items, this.recipesUsed);
    }

    public @NotNull java.util.List<Recipe<?>> getRecipesToAwardAndPopExperience(@NotNull ServerLevel p_154996_, @NotNull Vec3 p_154997_) {
        return MPGFurnaceLogicHelper.getRecipesToAwardAndPopExperience(p_154996_, p_154997_, this.recipesUsed);
    }

}

