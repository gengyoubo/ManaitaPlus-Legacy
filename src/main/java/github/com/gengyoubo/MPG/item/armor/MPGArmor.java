package github.com.gengyoubo.MPG.item.armor;

import com.google.common.collect.Lists;
import github.com.gengyoubo.MPG.item.data.IMPGKey;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import github.com.gengyoubo.MPG.util.MPGItemStackData;
import github.com.gengyoubo.MPG.util.MPText;
import github.com.gengyoubo.MPG.util.MPUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

public class MPGArmor extends ArmorItem {
    private static final ArmorMaterial MANAITA_ARMOR_MATERIAL = new ArmorMaterial() {
        @Override
        public int getDurabilityForType(Type type) {
            return 0;
        }

        @Override
        public int getDefenseForType(Type type) {
            return 0;
        }

        @Override
        public int getEnchantmentValue() {
            return 0;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_TURTLE;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }

        @Override
        public String getName() {
            return "manaita_armor";
        }

        @Override
        public float getToughness() {
            return 0.0F;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.0F;
        }
    };

    protected MPGArmor(Type type) {
        super(MANAITA_ARMOR_MATERIAL, type, new Item.Properties().fireResistant());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.literal(MPText.manaita_infinity.formatting(I18n.get("info.armor"))));
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

        public String getArmorTexture() {
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
            return MPGItemStackData.getBoolean(itemStack, "NightVision");
        }

        @Override
        public void onManaitaKeyPress(ItemStack itemStack) {
            MPGItemStackData.putBoolean(itemStack, "NightVision", !getNightVision(itemStack));
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

        public String getArmorTexture() {
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

        public String getArmorTexture() {
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
            return MPGItemStackData.getBoolean(itemStack, "Invisibility");
        }

        @Override
        public void onManaitaKeyPress(ItemStack itemStack) {
            MPGItemStackData.putBoolean(itemStack, "Invisibility", !getInvisibility(itemStack));
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

        public String getArmorTexture() {
            return "manaita_plus_general:textures/models/armor/manaita_armor_layer_2.png";
        }

        @SuppressWarnings("deprecation")
        @Override
        public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean selected) {
            if (slot == 0 && entity instanceof Player player) {
                if (!player.getAbilities().mayfly) {
                    player.getAbilities().mayfly = true;
                    player.onUpdateAbilities();
                }
                int speed = getSpeed(stack);
                float baseSpeed = 0.1F * speed;
                Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(baseSpeed);
                player.getAbilities().setWalkingSpeed(baseSpeed);
                player.getAbilities().setFlyingSpeed(baseSpeed / 2.0F);
            }
        }

        public static int getSpeed(ItemStack itemStack) {
            return Math.max(MPGItemStackData.getInt(itemStack, "Speed"), 1);
        }

        @Override
        public void onManaitaKeyPress(ItemStack itemStack) {
            int next = Math.max(1, MPGItemStackData.getInt(itemStack, "Speed") + 1) % 10;
            MPGItemStackData.putInt(itemStack, "Speed", next == 0 ? 1 : next);
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
