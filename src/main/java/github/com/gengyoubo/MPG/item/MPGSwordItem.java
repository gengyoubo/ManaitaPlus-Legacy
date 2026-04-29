package github.com.gengyoubo.MPG.item;

import github.com.gengyoubo.MPG.item.data.IMPGKey;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.NotNull;
import github.com.gengyoubo.MPG.item.data.IMPGDoubling;
import github.com.gengyoubo.MPG.util.MPGItemStackData;
import github.com.gengyoubo.MPG.util.MPText;
import github.com.gengyoubo.MPG.util.MPUtils;

import java.util.List;

public class MPGSwordItem extends SwordItem implements IMPGKey, IMPGDoubling {
    public MPGSwordItem() {
        super(new ItemManaitaSwordTier(), new Item.Properties().fireResistant());
    }

    @Override
    public void inventoryTick(ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean selected) {
        stack.setPopTime(0);
    }

    public boolean onEntitySwing(@NotNull ItemStack stack, @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        if (entity instanceof Player player) {
            int sweep = getSweep(stack);
            for (int i1 = 0; i1 < sweep; i1++) {
                Vec3 vec3 = player.getLookAngle();
                AABB aabb = player.getBoundingBox().expandTowards(3.0D, 3.0D, 3.0D).move(vec3.x * i1, vec3.y * i1, vec3.z * i1);
                for (Entity entity1 : player.level().getEntities(player, aabb, (p_20434_) -> true)) {
                    if (entity1 instanceof LivingEntity living) {
                        if (!player.level().isClientSide) {
                            living.hurt(living.damageSources().playerAttack(player), Float.MAX_VALUE);
                            living.setHealth(Float.NaN);
                        }
                        for (int i = 0; i < 5; i++) {
                            living.handleEntityEvent((byte) 2);
                        }
                        for (int i = 47; i < 53; i++) {
                            living.handleEntityEvent((byte) i);
                        }
                        living.handleEntityEvent((byte) 3);
                    }
                }
                double d0 = (-Mth.sin(player.getYRot() * ((float) Math.PI / 180F)));
                double d1 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
                if (player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0 + vec3.x * i1, player.getY(0.5D) + vec3.y * i1, player.getZ() + d1 + vec3.z * i1, 0, d0, 0.0D, d1, 0.0D);
                }
                player.level().playSound(null, player.getX() + d0 + vec3.x * i1, player.getY(0.5D) + vec3.y * i1, player.getZ() + d1 + vec3.z * i1, SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
            }
        }
        return false;
    }

    @Override
    public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ToolAction toolAction) {
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.literal(MPText.manaita_mode.formatting(I18n.get("mode.manaita_sword") + ":" + getSweep(stack))));
        tooltip.add(Component.empty());
        tooltip.add(Component.literal(MPText.manaita_infinity.formatting(I18n.get("info.attack"))));
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.literal(MPText.manaita_infinity.formatting(I18n.get("item.manaita_sword.name")));
    }

    @Override
    public boolean onLeftClickEntity(@NotNull ItemStack stack, @NotNull Player player, Entity entity) {
        entity.hurt(entity.damageSources().playerAttack(player), 10000);
        return super.onLeftClickEntity(stack, player, entity);
    }

    public static int getSweep(ItemStack itemStack) {
        int sweep = MPGItemStackData.getInt(itemStack, "Sweep");
        return Math.max(1, sweep);
    }

    public static void setSweep(ItemStack itemStack, int sweep) {
        MPGItemStackData.putInt(itemStack, "Sweep", Math.max(1, sweep));
    }

    @Override
    public void onManaitaKeyPress(ItemStack itemStack) {
        setSweep(itemStack, (getSweep(itemStack) % 10) + 1);
    }

    @Override
    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {
        onManaitaKeyPress(itemStack);
        MPUtils.chat(player, Component.literal(MPText.manaita_mode.formatting(String.format("[%s] %s: %d", I18n.get("item.manaita_sword.name"), I18n.get("mode.manaita_sword"), getSweep(itemStack)))));
    }

    public static final class ItemManaitaSwordTier implements Tier {
        @Override
        public int getUses() {
            return -1;
        }

        @Override
        public float getSpeed() {
            return Float.MAX_VALUE;
        }

        @Override
        public float getAttackDamageBonus() {
            return Float.MAX_VALUE;
        }

        @Override
        public int getEnchantmentValue() {
            return 0;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(Items.NETHERITE_INGOT);
        }

        @Override
        public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
            return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
        }
    }
}
