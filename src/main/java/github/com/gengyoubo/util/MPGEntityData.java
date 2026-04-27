package github.com.gengyoubo.util;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import github.com.gengyoubo.core.MPGSynchedDataCore;

public enum MPGEntityData {
    manaita(),
    death(),
    remove();

    public static final String cName = "manaita_plus_general_type";
    public static final EntityDataAccessor<Integer> Type = MPGSynchedDataCore.get();

    MPGEntityData() {
        flag = pow(2,ordinal());
        name = name();
    }

    public static int pow(int a, int b) {
        if(b == 0) return 1;
        int pow = 1;
        for (int i = 0; i < b; i++) {
            pow *= a;
        }
        return pow;
    }


    private final String name;
    private final int flag;

    public void add(Entity entity) {
        if (entity == null)
            return;
        entity.addTag(name);

        if (entity.getEntityData().hasItem(Type)) entity.getEntityData().set(Type, entity.getEntityData().get(Type) | flag);
        entity.getPersistentData().putInt(cName, entity.getPersistentData().getInt(cName) | flag);
    }

    public void remove(Entity entity) {
        if (entity == null) return;
        entity.removeTag(name);
        if (entity.getEntityData().hasItem(Type)) entity.getEntityData().set(Type, entity.getEntityData().get(Type) & ~flag);
        entity.getPersistentData().putInt(cName, entity.getPersistentData().getInt(cName) & ~flag);
    }


    public boolean accept(Entity entity) {
        if (entity == null) return false;
        return entity.getTags().contains(name) || (entity.getPersistentData().getInt(cName) & flag) != 0 ||
                (entity.getEntityData().hasItem(Type) && (entity.getEntityData().get(Type) & flag) != 0);
    }

    public int getFlag() {
        return flag;
    }
}
