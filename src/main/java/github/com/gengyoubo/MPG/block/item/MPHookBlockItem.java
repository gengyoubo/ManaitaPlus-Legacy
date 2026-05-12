package github.com.gengyoubo.MPG.block.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import github.com.gengyoubo.MPG.block.MPHookBlock;
import github.com.gengyoubo.MPG.block.data.MPGBlockData;
import github.com.gengyoubo.MPG.util.MPGNBTData;

import static github.com.gengyoubo.MPG.core.MPGBlockCore.HookBlock;

public class MPHookBlockItem extends BlockItem {
    public MPHookBlockItem() {
        super(HookBlock.get(), new Properties().fireResistant());
    }

    @Override
    public @NotNull Component getName(ItemStack p_41458_) {
        return Component.translatable("tile.fixed_hook." + p_41458_.getOrCreateTag().getInt(MPGNBTData.ItemType) + ".name");
    }

    @Override
    public @NotNull InteractionResult place(@NotNull BlockPlaceContext context) {
        BlockPlaceContext placementContext = this.updatePlacementContext(context);
        if (
                !this.getBlock().isEnabled(context.getLevel().enabledFeatures())
                        || !context.canPlace()
                        ||placementContext == null
        ) {
            return InteractionResult.FAIL;
        }
        BlockState placementState = this.getPlacementState(placementContext);
        Direction clickedFace = placementContext.getClickedFace();
        if (placementState == null||clickedFace == Direction.UP || clickedFace == Direction.DOWN) {
            return InteractionResult.FAIL;
        }
        BlockPos pos = placementContext.getClickedPos();
        Level level = placementContext.getLevel();
        Player player = placementContext.getPlayer();
        ItemStack stack = placementContext.getItemInHand();

        if (!level.setBlock(pos, placementState, 11)) {
            return InteractionResult.FAIL;
        }

        BlockState placedState = level.getBlockState(pos);

        if (placedState.is(placementState.getBlock())) {
            placedState = this.updateBlockStateFromTag(pos, level, stack, placedState);
            this.updateCustomBlockEntityTag(pos, level, player, stack, placedState);
            placedState.getBlock().setPlacedBy(level, pos, placedState, player, stack);

            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, stack);
            }
        }

        SoundType soundType = placedState.getSoundType(level, pos, player);

        if (player != null) {
            level.playSound(
                    player,
                    pos,
                    this.getPlaceSound(placedState, level, pos, player),
                    SoundSource.BLOCKS,
                    (soundType.getVolume() + 1.0F) / 2.0F,
                    soundType.getPitch() * 0.8F
            );
        }

        level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, placedState));

        if (player == null || !player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private BlockState updateBlockStateFromTag(BlockPos pos, Level level, ItemStack p_40605_, BlockState p_40606_) {
        if (p_40606_.getBlock() instanceof MPHookBlock && p_40605_.getTag() != null) {
            BlockState manaitaType = p_40606_.setValue(MPGBlockData.TYPES, p_40605_.getTag().getInt(MPGNBTData.ItemType));
            level.setBlock(pos, manaitaType, 2);
            return manaitaType;
        }

        return p_40606_;
    }
}
