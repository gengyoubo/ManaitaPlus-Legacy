package github.com.gengyoubo.core;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;

import java.lang.reflect.Field;

public class MPGSynchedDataCore {
    public static int i = -1;

    @SuppressWarnings("unchecked")
    public static void init() {
        try {
            Field declaredField = SynchedEntityData.class.getDeclaredField("ENTITY_ID_POOL");
            declaredField.setAccessible(true);
            Object2IntMap<Class<? extends Entity>> classObject2IntMap = (Object2IntMap<Class<? extends Entity>>) declaredField.get(SynchedEntityData.class);
            if (classObject2IntMap.isEmpty()) {
                i = 1;
                classObject2IntMap.put(Entity.class, 1);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace(System.err);
        }
    }

    public static EntityDataAccessor<Integer> get() {
        return EntityDataSerializers.INT.createAccessor(i);
    }
}
