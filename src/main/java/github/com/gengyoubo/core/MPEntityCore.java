package github.com.gengyoubo.core;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static github.com.gengyoubo.MPG.ENTITY_TYPES;

public class MPEntityCore {
    @SuppressWarnings("unchecked")
    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, Supplier<EntityType<T>> supplier) {
        return (RegistryObject<EntityType<T>>) (RegistryObject<?>) ENTITY_TYPES.register(name, (Supplier<EntityType<?>>) (Supplier<?>) supplier);
    }

}

