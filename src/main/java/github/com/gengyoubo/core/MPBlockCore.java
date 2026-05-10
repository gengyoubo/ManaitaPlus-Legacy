package github.com.gengyoubo.core;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import github.com.gengyoubo.block.MPBrewingStandBlock;
import github.com.gengyoubo.block.MPFurnaceBlock;
import github.com.gengyoubo.block.MPCraftingBlock;
import github.com.gengyoubo.block.MPHookBlock;
import github.com.gengyoubo.block.item.MPBrewingBlockItem;
import github.com.gengyoubo.block.item.MPFurnaceBlockItem;
import github.com.gengyoubo.block.item.MPCraftingBlockItem;
import github.com.gengyoubo.block.item.MPHookBlockItem;

import static github.com.gengyoubo.MPG.register;

public class MPBlockCore {
    public static final Block CraftingBlock = register(BuiltInRegistries.BLOCK, "block_crafting_manaita", new MPCraftingBlock());
    public static final Item CraftingBlockItem = register(BuiltInRegistries.ITEM, "block_crafting_manaita", new MPCraftingBlockItem());

    public static final Block FurnaceBlock = register(BuiltInRegistries.BLOCK, "block_furnace_manaita", new MPFurnaceBlock());
    public static final Item FurnaceBlockItem = register(BuiltInRegistries.ITEM, "block_furnace_manaita", new MPFurnaceBlockItem());

    public static final Block BrewingBlock = register(BuiltInRegistries.BLOCK, "block_brewing_manaita", new MPBrewingStandBlock());
    public static final Item BrewingBlockItem = register(BuiltInRegistries.ITEM, "block_brewing_manaita", new MPBrewingBlockItem());

    public static final Block HookBlock = register(BuiltInRegistries.BLOCK, "block_hook_manaita", new MPHookBlock());
    public static final Item HookBlockItem = register(BuiltInRegistries.ITEM, "block_hook_manaita", new MPHookBlockItem());
}

