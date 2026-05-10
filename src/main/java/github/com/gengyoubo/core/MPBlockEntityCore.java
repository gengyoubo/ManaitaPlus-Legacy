package github.com.gengyoubo.core;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import github.com.gengyoubo.block.entity.MPBrewingStandBlockEntity;
import github.com.gengyoubo.block.entity.MPCraftingBlockEntity;
import github.com.gengyoubo.block.entity.MPFurnaceBlockEntity;

import static github.com.gengyoubo.MPG.register;

public class MPBlockEntityCore {
    public static final BlockEntityType<MPFurnaceBlockEntity> FURNACE_BLOCK_ENTITY =
            register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "furnace_block_entity", buildType(BlockEntityType.Builder.of(MPFurnaceBlockEntity::new, MPBlockCore.FurnaceBlock)));
    public static final BlockEntityType<MPBrewingStandBlockEntity> BREWING_BLOCK_ENTITY =
            register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "brewing_block_entity", buildType(BlockEntityType.Builder.of(MPBrewingStandBlockEntity::new, MPBlockCore.BrewingBlock)));
    public static final BlockEntityType<MPCraftingBlockEntity> CRAFTING_BLOCK_ENTITY =
            register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "crafting_entity", buildType(BlockEntityType.Builder.of(MPCraftingBlockEntity::new, MPBlockCore.CraftingBlock)));

    private static <T extends net.minecraft.world.level.block.entity.BlockEntity> BlockEntityType<T> buildType(BlockEntityType.Builder<T> builder) {
        return builder.build(null);
    }
}
