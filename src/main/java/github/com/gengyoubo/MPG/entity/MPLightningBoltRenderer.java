package github.com.gengyoubo.MPG.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class MPLightningBoltRenderer extends EntityRenderer<MPGLightningBolt> {
    public MPLightningBoltRenderer(EntityRendererProvider.Context p_174286_) {
        super(p_174286_);
    }

    @Override
    public void render(MPGLightningBolt bolt,
                       float entityYaw,
                       float partialTicks,
                       @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource,
                       int packedLight) {
        float[] xOffsets = new float[8];
        float[] zOffsets = new float[8];

        float xOffset = 0.0F;
        float zOffset = 0.0F;

        RandomSource baseRandom = RandomSource.create(bolt.seed);

        for (int i = 7; i >= 0; i--) {
            xOffsets[i] = xOffset;
            zOffsets[i] = zOffset;

            xOffset += baseRandom.nextInt(11) - 5;
            zOffset += baseRandom.nextInt(11) - 5;
        }

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.lightning());
        Matrix4f matrix = poseStack.last().pose();

        Random colorRandom = new Random(bolt.seed);

        for (int layer = 0; layer < 4; layer++) {
            RandomSource branchRandom = RandomSource.create(bolt.seed);

            for (int branch = 0; branch < 3; branch++) {
                int start = branch == 0 ? 7 : 7 - branch;
                int end = branch == 0 ? 0 : start - 2;

                float prevX = xOffsets[start] - xOffset;
                float prevZ = zOffsets[start] - zOffset;

                for (int segment = start; segment >= end; segment--) {
                    float currentX = prevX;
                    float currentZ = prevZ;

                    int randomRange = branch == 0 ? 11 : 31;
                    int randomOffset = branch == 0 ? 5 : 15;

                    prevX += branchRandom.nextInt(randomRange) - randomOffset;
                    prevZ += branchRandom.nextInt(randomRange) - randomOffset;

                    float startWidth = 0.1F + layer * 0.2F;
                    float endWidth = startWidth;

                    if (branch == 0) {
                        startWidth *= segment * 0.1F + 1.0F;
                        endWidth *= (segment - 1.0F) * 0.1F + 1.0F;
                    }

                    float red = colorRandom.nextFloat();
                    float green = colorRandom.nextFloat();
                    float blue = colorRandom.nextFloat();
                    float alpha = 0.375F;

                    quad(matrix, consumer, prevX, prevZ, segment, currentX, currentZ,
                            red, green, blue, alpha, startWidth, endWidth,
                            false, false, true, false);

                    quad(matrix, consumer, prevX, prevZ, segment, currentX, currentZ,
                            red, green, blue, alpha, startWidth, endWidth,
                            true, false, true, true);

                    quad(matrix, consumer, prevX, prevZ, segment, currentX, currentZ,
                            red, green, blue, alpha, startWidth, endWidth,
                            true, true, false, true);

                    quad(matrix, consumer, prevX, prevZ, segment, currentX, currentZ,
                            red, green, blue, alpha, startWidth, endWidth,
                            false, true, false, false);
                }
            }
        }
    }


    private static void quad(Matrix4f p_253966_, VertexConsumer p_115274_, float p_115275_, float p_115276_, int p_115277_, float p_115278_, float p_115279_, float p_115280_, float p_115281_, float p_115282_,float a, float p_115283_, float p_115284_, boolean p_115285_, boolean p_115286_, boolean p_115287_, boolean p_115288_) {
        p_115274_.vertex(p_253966_, p_115275_ + (p_115285_ ? p_115284_ : -p_115284_), (float)(p_115277_ * 16), p_115276_ + (p_115286_ ? p_115284_ : -p_115284_)).color(p_115280_, p_115281_, p_115282_, a).endVertex();
        p_115274_.vertex(p_253966_, p_115278_ + (p_115285_ ? p_115283_ : -p_115283_), (float)((p_115277_ + 1) * 16), p_115279_ + (p_115286_ ? p_115283_ : -p_115283_)).color(p_115280_, p_115281_, p_115282_, a).endVertex();
        p_115274_.vertex(p_253966_, p_115278_ + (p_115287_ ? p_115283_ : -p_115283_), (float)((p_115277_ + 1) * 16), p_115279_ + (p_115288_ ? p_115283_ : -p_115283_)).color(p_115280_, p_115281_, p_115282_, a).endVertex();
        p_115274_.vertex(p_253966_, p_115275_ + (p_115287_ ? p_115284_ : -p_115284_), (float)(p_115277_ * 16), p_115276_ + (p_115288_ ? p_115284_ : -p_115284_)).color(p_115280_, p_115281_, p_115282_, a).endVertex();
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull MPGLightningBolt p_115264_) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
