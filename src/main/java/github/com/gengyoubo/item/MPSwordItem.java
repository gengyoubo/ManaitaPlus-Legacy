package github.com.gengyoubo.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import github.com.gengyoubo.item.data.IMPDoubling;
import github.com.gengyoubo.item.data.IMPKey;
import github.com.gengyoubo.util.MPText;

import java.util.List;
import java.util.Objects;

public class MPSwordItem extends SwordItem implements IMPKey, IMPDoubling {
    public MPSwordItem() {
        super(new ItemManaitaSwordTier(), 0, 0, new Item.Properties().fireResistant());
    }

    @Override
    public void inventoryTick(ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean selected) {
        stack.setPopTime(0);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        if (attacker instanceof Player player) {
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
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.literal(MPText.manaita_mode.formatting(Component.translatable("mode.manaita_sword").getString() + ":" + getSweep(stack))));
        tooltip.add(Component.empty());
        tooltip.add(Component.literal(MPText.manaita_infinity.formatting(Component.translatable("info.attack").getString())));
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.literal(MPText.manaita_infinity.formatting(Component.translatable("item.manaita_sword.name").getString()));
    }

    public static int getSweep(ItemStack itemStack) {
        if (!itemStack.hasTag()) {
            return 1;
        }
        int sweep = Objects.requireNonNull(itemStack.getTag()).getInt("Sweep");
        return Math.max(1, sweep);
    }

    public static void setSweep(ItemStack itemStack, int sweep) {
        itemStack.getOrCreateTag().putInt("Sweep", Math.max(1, sweep));
    }

    @Override
    public void onManaitaKeyPress(ItemStack itemStack) {
        setSweep(itemStack, (getSweep(itemStack) % 10) + 1);
    }

    @Override
    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {
        onManaitaKeyPress(itemStack);
        player.displayClientMessage(Component.literal(MPText.manaita_mode.formatting(String.format("[%s] %s: %d", Component.translatable("item.manaita_sword.name").getString(), Component.translatable("mode.manaita_sword").getString(), getSweep(itemStack)))), true);
    }

    public void onDestroyed(ItemEntity itemEntity) {
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
        public int getLevel() {
            return Integer.MAX_VALUE;
        }

        @Override
        public int getEnchantmentValue() {
            return 0;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(Items.NETHERITE_INGOT);
        }

    }
}

