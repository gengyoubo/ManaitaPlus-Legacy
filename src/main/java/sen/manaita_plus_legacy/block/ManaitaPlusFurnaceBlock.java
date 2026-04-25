package sen.manaita_plus_legacy.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sen.manaita_plus_legacy.block.data.ManaitaPlusLegacyBlockData;
import sen.manaita_plus_legacy.block.entity.ManaitaPlusFurnaceBlockEntity;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyBlockEntityCore;

import javax.annotation.Nullable;
import java.util.List;

import static sen.manaita_plus_legacy.block.ManaitaPlusBrewingStandBlock.getItemStacks;

@SuppressWarnings("deprecation")
public class ManaitaPlusFurnaceBlock extends AbstractFurnaceBlock {
    public ManaitaPlusFurnaceBlock() {
        super(BlockBehaviour.Properties.of().noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(ManaitaPlusLegacyBlockData.HOOK, 8).setValue(FACING, Direction.NORTH).setValue(ManaitaPlusLegacyBlockData.WALL,Direction.DOWN).setValue(LIT, Boolean.FALSE).setValue(ManaitaPlusLegacyBlockData.TYPES,0));
    }

    protected void openContainer(Level p_53631_, @NotNull BlockPos p_53632_, @NotNull Player p_53633_) {
        BlockEntity blockentity = p_53631_.getBlockEntity(p_53632_);
        if (blockentity instanceof ManaitaPlusFurnaceBlockEntity) {
            p_53633_.openMenu((MenuProvider) blockentity);
            p_53633_.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    public @NotNull RenderShape getRenderShape(@NotNull BlockState p_48727_) {
        return RenderShape.INVISIBLE;
    }


    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState p_287732_, LootParams.@NotNull Builder p_287596_) {
        return getItemStacks(p_287732_);
    }

    public @NotNull BlockState getStateForPlacement(BlockPlaceContext p_48689_) {
        return this.defaultBlockState().setValue(FACING, p_48689_.getHorizontalDirection().getOpposite()).setValue(ManaitaPlusLegacyBlockData.WALL,p_48689_.getClickedFace().getOpposite()).setValue(ManaitaPlusLegacyBlockData.FACING, p_48689_.getHorizontalDirection().getOpposite());
    }


    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, @NotNull BlockGetter p_60556_, @NotNull BlockPos p_60557_, @NotNull CollisionContext p_60558_) {
        Direction wall = p_60555_.getValue(ManaitaPlusLegacyBlockData.WALL);
        Direction facing = p_60555_.getValue(ManaitaPlusLegacyBlockData.FACING);
        boolean hasHook = p_60555_.getValue(ManaitaPlusLegacyBlockData.HOOK) != 8;
        return ManaitaPlusLegacyBlockData.getWallMountedShape(wall, facing, hasHook);
    }

    public @NotNull BlockState mirror(BlockState p_154351_, Mirror p_154352_) {
        return p_154351_.setValue(FACING, p_154352_.mirror(p_154351_.getValue(FACING)));
    }

    public BlockEntity newBlockEntity(@NotNull BlockPos p_153277_, @NotNull BlockState p_153278_) {
        return new ManaitaPlusFurnaceBlockEntity(p_153277_, p_153278_);
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_48725_) {
        p_48725_.add(FACING, LIT, ManaitaPlusLegacyBlockData.TYPES, ManaitaPlusLegacyBlockData.WALL, ManaitaPlusLegacyBlockData.HOOK);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153273_, @NotNull BlockState p_153274_, @NotNull BlockEntityType<T> p_153275_) {
        return p_153273_.isClientSide ? null : createTickerHelper(p_153275_, ManaitaPlusLegacyBlockEntityCore.FURNACE_BLOCK_ENTITY.get(), ManaitaPlusFurnaceBlockEntity::serverTick);
    }
}
