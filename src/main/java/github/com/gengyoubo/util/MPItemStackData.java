package github.com.gengyoubo.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MPItemStackData {
    private MPItemStackData() {
    }

    public static @NotNull CompoundTag getOrCreateTag(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
    }

    public static @Nullable CompoundTag getTag(ItemStack stack) {
        net.minecraft.world.item.component.CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? null : data.copyTag();
    }

    public static boolean hasTag(ItemStack stack) {
        CompoundTag tag = getTag(stack);
        return tag != null && !tag.isEmpty();
    }

    public static void setTag(ItemStack stack, @Nullable CompoundTag tag) {
        if (tag == null || tag.isEmpty()) {
            stack.remove(DataComponents.CUSTOM_DATA);
        } else {
            stack.set(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(tag));
        }
    }

    public static int getInt(ItemStack stack, String key) {
        CompoundTag tag = getTag(stack);
        return tag == null ? 0 : tag.getInt(key);
    }

    public static void putInt(ItemStack stack, String key, int value) {
        CompoundTag tag = getOrCreateTag(stack);
        tag.putInt(key, value);
        setTag(stack, tag);
    }

    public static boolean getBoolean(ItemStack stack, String key) {
        CompoundTag tag = getTag(stack);
        return tag != null && tag.getBoolean(key);
    }

    public static void putBoolean(ItemStack stack, String key, boolean value) {
        CompoundTag tag = getOrCreateTag(stack);
        tag.putBoolean(key, value);
        setTag(stack, tag);
    }
}

