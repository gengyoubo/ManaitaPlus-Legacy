package github.com.gengyoubo.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import github.com.gengyoubo.block.data.MPBlockData;
import github.com.gengyoubo.core.MPBlockCore;
import github.com.gengyoubo.util.MPNBTData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractRenderMPBlockEntity<T extends BlockEntity> implements BlockEntityRenderer<T> {
    private final ItemStack displayStack;
    private final BlockState hookBlockTemplate;

    protected AbstractRenderMPBlockEntity(ItemStack displayStack) {
        this.displayStack = displayStack;
        this.hookBlockTemplate = MPBlockCore.HookBlock.defaultBlockState();
        this.displayStack.setTag(new CompoundTag());
    }

    @Override
    public void render(T blockEntity, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState blockState = blockEntity.getBlockState();
        Direction direction = blockState.getValue(MPBlockData.FACING);
        Direction wall = blockState.getValue(MPBlockData.WALL);

        poseStack.pushPose();
        applyWallTransform(poseStack, wall, direction);
        renderMainBlockItem(blockEntity, blockState, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();

        renderHook(blockState, poseStack, bufferSource, packedLight, packedOverlay);
    }

    private void renderMainBlockItem(T blockEntity, BlockState blockState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        displayStack.getOrCreateTag().putInt(MPNBTData.ItemType, blockState.getValue(MPBlockData.TYPES));
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BakedModel bakedModel = itemRenderer.getModel(displayStack, blockEntity.getLevel(), null, 0);
        itemRenderer.render(displayStack, ItemDisplayContext.FIXED, true, poseStack, bufferSource, packedLight, packedOverlay, bakedModel);
    }

    private void renderHook(BlockState blockState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        int hookType = blockState.getValue(MPBlockData.HOOK);
        if (hookType == 8) {
            return;
        }

        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BlockState hookBlock = hookBlockTemplate
                .setValue(MPBlockData.FACING, blockState.getValue(MPBlockData.FACING))
                .setValue(MPBlockData.TYPES, hookType);
        blockRenderer.renderSingleBlock(hookBlock, poseStack, bufferSource, packedLight, packedOverlay);
    }

    private static void applyWallTransform(PoseStack poseStack, Direction wall, Direction direction) {
        float x = 0.5F;
        float y = 0.5F;
        float z = 0.5F;

        switch (wall) {
            case NORTH -> z = 0.0F;
            case SOUTH -> z = 1.0F;
            case WEST -> x = 0.0F;
            case EAST -> x = 1.0F;
            case UP -> y = 1.0F;
            case DOWN -> y = 0.0F;
        }

        poseStack.translate(x, y, z);

        switch (wall) {
            case NORTH -> poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
            case SOUTH -> poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            case WEST, EAST -> {
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            }
            case UP, DOWN -> applyVerticalFacing(poseStack, direction);
        }
    }

    private static void applyVerticalFacing(PoseStack poseStack, Direction direction) {
        switch (direction) {
            case NORTH -> poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

            case SOUTH -> poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));

            case WEST, EAST -> {
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

                poseStack.mulPose(
                        Axis.ZP.rotationDegrees(direction == Direction.WEST ? 90.0F : -90.0F)
                );
            }
        }
    }
}
