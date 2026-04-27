package github.com.gengyoubo.item.armor;

import com.google.common.collect.Lists;
import github.com.gengyoubo.item.data.IMPGKey;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import github.com.gengyoubo.util.MPText;
import github.com.gengyoubo.util.MPUtils;

import java.util.List;
import java.util.Objects;

public class MPGArmor extends ArmorItem {
    protected MPGArmor(Type type) {
        super(new ManaitaArmorMaterial(), type, new Item.Properties().fireResistant());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.literal(MPText.manaita_infinity.formatting(I18n.get("info.armor"))));
    }

    static class ManaitaArmorMaterial implements ArmorMaterial {
        @Override
        public int getDurabilityForType(@NotNull Type type) {
            return -1;
        }

        @Override
        public int getDefenseForType(@NotNull Type type) {
            return 0;
        }

        @Override
        public int getEnchantmentValue() {
            return 0;
        }

        @Override
        public @NotNull SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_TURTLE;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }

        @Override
        public @NotNull String getName() {
            return "manaita_armor";
        }

        @Override
        public float getToughness() {
            return 0;
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    }

    public static class Helmet extends MPGArmor implements IMPGKey {
        public Helmet() {
            super(Type.HELMET);
        }

        @Override
        public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
            tooltip.add(Component.literal(
                    MPText.manaita_mode.formatting(
                            I18n.get("mode.nightvision") + ": " + (getNightVision(stack) ? I18n.get("info.on") : I18n.get("info.off")))));
            super.appendHoverText(stack, level, tooltip, flag);
        }

        @Override
        public @NotNull Component getName(@NotNull ItemStack stack) {
            return Component.literal(I18n.get("item.helmet.name"));
        }

        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "manaita_plus_general:textures/models/armor/manaita_armor_layer_1.png";
        }

        @Override
        public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean selected) {
            if (slot == 3 && entity instanceof Player player) {
                player.setAirSupply(300);
                FoodData foodData = player.getFoodData();
                if (foodData.getFoodLevel() < 20) {
                    foodData.setFoodLevel(20);
                }
                if (foodData.getSaturationLevel() < 20) {
                    foodData.setSaturation(20);
                }
                foodData.setExhaustion(0);
                if (getNightVision(stack)) {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0, false, false));
                }
            }
        }

        public static boolean getNightVision(ItemStack itemStack) {
            return itemStack.getOrCreateTag().getBoolean("NightVision");
        }

        @Override
        public void onManaitaKeyPress(ItemStack itemStack) {
            itemStack.getOrCreateTag().putBoolean("NightVision", !getNightVision(itemStack));
        }

        @Override
        public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {
            onManaitaKeyPress(itemStack);
            MPUtils.chat(player, Component.literal(
                    MPText.manaita_mode.formatting(String.format("[%s] %s: %s",
                            I18n.get("item.helmet.name"), I18n.get("mode.nightvision"),
                            getNightVision(itemStack) ? I18n.get("info.on") : I18n.get("info.off")))));
        }
    }

    public static class Chestplate extends MPGArmor {
        public Chestplate() {
            super(Type.CHESTPLATE);
        }

        @Override
        public @NotNull Component getName(@NotNull ItemStack stack) {
            return Component.literal(I18n.get("item.chestplate.name"));
        }

        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "manaita_plus_general:textures/models/armor/manaita_armor_layer_1.png";
        }

        @Override
        public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean selected) {
            if (slot == 2 && entity instanceof Player player) {
                List<MobEffect> badEffects = Lists.newArrayList();
                for (MobEffectInstance effect : player.getActiveEffects()) {
                    if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                        badEffects.add(effect.getEffect());
                    }
                }
                for (MobEffect effect : badEffects) {
                    player.removeEffect(effect);
                }
            }
        }
    }

    public static class Leggings extends MPGArmor implements IMPGKey {
        public Leggings() {
            super(Type.LEGGINGS);
        }

        @Override
        public @NotNull Component getName(@NotNull ItemStack stack) {
            return Component.literal(I18n.get("item.leggings.name"));
        }

        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "manaita_plus_general:textures/models/armor/manaita_armor_layer_2.png";
        }

        @Override
        public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean selected) {
            if (slot == 1 && entity instanceof Player player) {
                player.setRemainingFireTicks(0);
                if (getInvisibility(stack)) {
                    player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 400, 0, false, false));
                    player.setInvisible(true);
                } else {
                    player.setInvisible(false);
                }
            }
        }

        @Override
        public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
            tooltip.add(Component.literal(
                    MPText.manaita_mode.formatting(
                            I18n.get("mode.invisibility") + ": " + (getInvisibility(stack) ? I18n.get("info.on") : I18n.get("info.off")))));
            super.appendHoverText(stack, level, tooltip, flag);
        }

        public static boolean getInvisibility(ItemStack itemStack) {
            return itemStack.getOrCreateTag().getBoolean("Invisibility");
        }

        @Override
        public void onManaitaKeyPress(ItemStack itemStack) {
            itemStack.getOrCreateTag().putBoolean("Invisibility", !getInvisibility(itemStack));
        }

        @Override
        public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {
            onManaitaKeyPress(itemStack);
            MPUtils.chat(player, Component.literal(
                    MPText.manaita_mode.formatting(String.format("[%s] %s: %s",
                            I18n.get("item.leggings.name"), I18n.get("mode.invisibility"),
                            getInvisibility(itemStack) ? I18n.get("info.on") : I18n.get("info.off")))));
        }
    }

    public static class Boots extends MPGArmor implements IMPGKey {
        public Boots() {
            super(Type.BOOTS);
        }

        @Override
        public @NotNull Component getName(@NotNull ItemStack stack) {
            return Component.literal(I18n.get("item.boots.name"));
        }

        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "manaita_plus_general:textures/models/armor/manaita_armor_layer_2.png";
        }

        @Override
        public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean selected) {
            if (slot == 0 && entity instanceof Player player) {
                player.getAbilities().mayfly = true;
                int speed = getSpeed(stack);
                float baseSpeed = 0.1F * speed;
                Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(baseSpeed);
                player.getAbilities().setWalkingSpeed(baseSpeed);
                player.getAbilities().setFlyingSpeed(baseSpeed / 2.0F);
            }
        }

        public static int getSpeed(ItemStack itemStack) {
            return Math.max(itemStack.getOrCreateTag().getInt("Speed"), 1);
        }

        @Override
        public void onManaitaKeyPress(ItemStack itemStack) {
            int next = Math.max(1, itemStack.getOrCreateTag().getInt("Speed") + 1) % 10;
            itemStack.getOrCreateTag().putInt("Speed", next == 0 ? 1 : next);
        }

        @Override
        public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {
            onManaitaKeyPress(itemStack);
            MPUtils.chat(player, Component.literal(
                    MPText.manaita_mode.formatting(String.format("[%s] %s: %d",
                            I18n.get("item.boots.name"), I18n.get("mode.speed"), getSpeed(itemStack)))));
        }
    }
}
