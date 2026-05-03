package github.com.gengyoubo.MPG.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class MPGItemStackData {
    private MPGItemStackData() {
    }

    public static boolean hasTag(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && !tag.isEmpty();
    }

    public static @Nullable CompoundTag getTag(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null || tag.isEmpty() ? null : tag.copy();
    }

    public static void setTag(ItemStack stack, @Nullable CompoundTag tag) {
        stack.setTag(tag == null || tag.isEmpty() ? null : tag.copy());
    }

    public static void editTag(ItemStack stack, Consumer<CompoundTag> editor) {
        CompoundTag tag = getTag(stack);
        if (tag == null) {
            tag = new CompoundTag();
        }
        editor.accept(tag);
        setTag(stack, tag);
    }

    public static int getInt(ItemStack stack, String key) {
        CompoundTag tag = getTag(stack);
        return tag != null ? tag.getInt(key) : 0;
    }

    public static boolean getBoolean(ItemStack stack, String key) {
        CompoundTag tag = getTag(stack);
        return tag != null && tag.getBoolean(key);
    }

    public static void putInt(ItemStack stack, String key, int value) {
        editTag(stack, tag -> tag.putInt(key, value));
    }

    public static void putBoolean(ItemStack stack, String key, boolean value) {
        editTag(stack, tag -> tag.putBoolean(key, value));
    }
}
