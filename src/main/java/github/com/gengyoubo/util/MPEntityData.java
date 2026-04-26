package github.com.gengyoubo.util;

import github.com.gengyoubo.core.MPSynchedDataCore;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;

public enum MPEntityData {
    manaita,
    death,
    remove;

    public static final String KEY = "manaita_plus_general_type";
    public static final EntityDataAccessor<Integer> TYPE = MPSynchedDataCore.get();

    private final String tagName;
    private final int flag;

    MPEntityData() {
        this.tagName = name();
        this.flag = 1 << ordinal();
    }

    public void add(Entity entity) {
        if (entity == null) {
            return;
        }

        entity.addTag(tagName);
        if (entity.getEntityData().hasItem(TYPE)) {
            entity.getEntityData().set(TYPE, entity.getEntityData().get(TYPE) | flag);
        }
    }

    public void remove(Entity entity) {
        if (entity == null) {
            return;
        }

        entity.removeTag(tagName);
        if (entity.getEntityData().hasItem(TYPE)) {
            entity.getEntityData().set(TYPE, entity.getEntityData().get(TYPE) & ~flag);
        }
    }

    public boolean accept(Entity entity) {
        if (entity == null) {
            return false;
        }

        return entity.getTags().contains(tagName)
                || (entity.getEntityData().hasItem(TYPE) && (entity.getEntityData().get(TYPE) & flag) != 0);
    }

    public int getFlag() {
        return flag;
    }
}
