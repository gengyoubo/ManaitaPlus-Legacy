package github.com.gengyoubo.MPG.item.ring;

import github.com.gengyoubo.MPG.MPG;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import net.minecraftforge.fml.ModList;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Predicate;

public final class MPGCuriosHelper {
    private MPGCuriosHelper() {
    }

    public static boolean isCuriosLoaded() {
        return ModList.get().isLoaded("curios");
    }

    public static Optional<ItemStack> findFirstMatching(@NotNull Player player, Predicate<ItemStack> predicate) {
        if (!isCuriosLoaded()) {
            return Optional.empty();
        }
        try {
            Object handler = resolveCuriosHandler(player);
            if (handler == null) {
                return Optional.empty();
            }
            Method findFirstCurio = handler.getClass().getMethod("findFirstCurio", Predicate.class);
            Object slotResult = ((Optional<?>) findFirstCurio.invoke(handler, predicate)).orElse(null);
            if (slotResult == null) {
                return Optional.empty();
            }
            Method stackMethod = slotResult.getClass().getMethod("stack");
            return Optional.of((ItemStack) stackMethod.invoke(slotResult));
        } catch (ReflectiveOperationException ignored) {
            return Optional.empty();
        }
    }

    public static Optional<ItemStack> findFirstRing(@NotNull Player player) {
        return findFirstMatching(player, stack -> stack.getItem() instanceof MPGRingItem);
    }

    public static boolean tryEquipRingFromHand(@NotNull Player player, @NotNull InteractionHand hand) {
        if (!isCuriosLoaded()) {
            return false;
        }
        ItemStack heldStack = player.getItemInHand(hand);
        if (heldStack.isEmpty()) {
            return false;
        }
        try {
            Object handler = resolveCuriosHandler(player);
            if (handler == null) {
                return false;
            }
            Method getStacksHandler = handler.getClass().getMethod("getStacksHandler", String.class);
            Object ringHandler = ((Optional<?>) getStacksHandler.invoke(handler, "ring")).orElse(null);
            if (ringHandler == null) {
                return false;
            }
            Method getStacks = ringHandler.getClass().getMethod("getStacks");
            Object stacks = getStacks.invoke(ringHandler);
            Method getSlots = stacks.getClass().getMethod("getSlots");
            Method getStackInSlot = stacks.getClass().getMethod("getStackInSlot", int.class);
            Method setStackInSlot = stacks.getClass().getMethod("setStackInSlot", int.class, ItemStack.class);

            int slotCount = (int) getSlots.invoke(stacks);
            for (int slot = 0; slot < slotCount; slot++) {
                ItemStack existing = (ItemStack) getStackInSlot.invoke(stacks, slot);
                if (!existing.isEmpty()) {
                    continue;
                }
                ItemStack toEquip = heldStack.copy();
                toEquip.setCount(1);
                setStackInSlot.invoke(stacks, slot, toEquip);
                heldStack.shrink(1);
                return true;
            }
        } catch (ReflectiveOperationException ignored) {
            MPG.LOGGER.debug("Failed to equip ring into Curios slot reflectively.", ignored);
        }
        return false;
    }

    private static Object resolveCuriosHandler(LivingEntity entity) throws ReflectiveOperationException {
        Class<?> curiosApiClass = Class.forName("top.theillusivec4.curios.api.CuriosApi");
        Method getCuriosInventory = curiosApiClass.getMethod("getCuriosInventory", LivingEntity.class);
        Object lazyOptional = getCuriosInventory.invoke(null, entity);
        Method resolve = lazyOptional.getClass().getMethod("resolve");
        return ((Optional<?>) resolve.invoke(lazyOptional)).orElse(null);
    }
}
