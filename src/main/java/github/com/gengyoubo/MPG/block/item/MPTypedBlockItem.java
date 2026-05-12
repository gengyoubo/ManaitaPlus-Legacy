package github.com.gengyoubo.MPG.block.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;
import github.com.gengyoubo.MPG.block.MPHookBlock;
import github.com.gengyoubo.MPG.block.data.MPGBlockData;
import github.com.gengyoubo.MPG.util.MPGNBTData;
import github.com.gengyoubo.MPG.util.MPUtils;

public abstract class MPTypedBlockItem extends BlockItem {
    private final String translationPrefix;
    private final Class<? extends Block> typedBlockClass;

    protected MPTypedBlockItem(Block block, Properties properties, String translationPrefix, Class<? extends Block> typedBlockClass) {
        super(block, properties);
        this.translationPrefix = translationPrefix;
        this.typedBlockClass = typedBlockClass;
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        return Component.literal(I18n.get(
                translationPrefix + MPUtils.getTypes(stack.getOrCreateTag().getInt(MPGNBTData.ItemType)) + "name"));
    }

    @Override
    public @NotNull InteractionResult place(BlockPlaceContext context) {
        Level level = context.getLevel();

        if (!this.getBlock().isEnabled(level.enabledFeatures()) || !context.canPlace()) {
            return InteractionResult.FAIL;
        }

        BlockPlaceContext placementContext = this.updatePlacementContext(context);
        if (placementContext == null) {
            return InteractionResult.FAIL;
        }

        BlockState placementState = this.getPlacementState(placementContext);
        if (placementState == null) {
            return InteractionResult.FAIL;
        }

        BlockPos pos = placementContext.getClickedPos();
        Direction clickedFace = placementContext.getClickedFace();
        Player player = placementContext.getPlayer();
        ItemStack stack = placementContext.getItemInHand();

        BlockPos relativePos = pos.relative(clickedFace.getOpposite());
        BlockState relativeState = level.getBlockState(relativePos);

        if (relativeState.getBlock() instanceof MPHookBlock) {
            placementState = placementState
                    .setValue(MPGBlockData.HOOK, relativeState.getValue(MPGBlockData.TYPES))
                    .setValue(MPGBlockData.WALL, relativeState.getValue(MPGBlockData.FACING))
                    .setValue(MPGBlockData.FACING, relativeState.getValue(MPGBlockData.FACING));
            pos = relativePos;
        } else {
            CollisionContext collisionContext = player == null
                    ? CollisionContext.empty()
                    : CollisionContext.of(player);

            boolean isVerticalFace = clickedFace == Direction.UP || clickedFace == Direction.DOWN;
            boolean unobstructed = level.isUnobstructed(relativeState, pos, collisionContext);

            if (!isVerticalFace || !unobstructed) {
                return InteractionResult.FAIL;
            }
        }

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
        SoundEvent placeSound = player != null
                ? this.getPlaceSound(placedState, level, pos, player)
                : soundType.getPlaceSound();

        level.playSound(
                player,
                pos,
                placeSound,
                SoundSource.BLOCKS,
                (soundType.getVolume() + 1.0F) / 2.0F,
                soundType.getPitch() * 0.8F
        );

        level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, placedState));

        if (player == null || !player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected boolean canPlace(@NotNull BlockPlaceContext context, @NotNull BlockState state) {
        return !this.mustSurvive() || state.canSurvive(context.getLevel(), context.getClickedPos());
    }

    private BlockState updateBlockStateFromTag(BlockPos pos, Level level, ItemStack stack, BlockState state) {
        if (typedBlockClass.isInstance(state.getBlock()) && stack.getTag() != null) {
            BlockState typedState = state.setValue(MPGBlockData.TYPES, stack.getTag().getInt(MPGNBTData.ItemType));
            level.setBlock(pos, typedState, 2);
            return typedState;
        }

        return state;
    }
}
