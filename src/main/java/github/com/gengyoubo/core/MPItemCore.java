package github.com.gengyoubo.core;

import github.com.gengyoubo.compat.MPTrinketsCompat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import github.com.gengyoubo.item.MPBowItem;
import github.com.gengyoubo.item.MPGodSwordItem;
import github.com.gengyoubo.item.MPHookItem;
import github.com.gengyoubo.item.MPSourceItem;
import github.com.gengyoubo.item.MPSwordItem;
import github.com.gengyoubo.item.armor.MPArmor;
import github.com.gengyoubo.item.portable.MPBrewingPortable;
import github.com.gengyoubo.item.portable.MPCraftingPortable;
import github.com.gengyoubo.item.portable.MPFurnacePortable;
import github.com.gengyoubo.item.tool.MPAxeItem;
import github.com.gengyoubo.item.tool.MPHoeItem;
import github.com.gengyoubo.item.tool.MPPaxelItem;
import github.com.gengyoubo.item.tool.MPPickaxeItem;
import github.com.gengyoubo.item.tool.MPShearsItem;
import github.com.gengyoubo.item.tool.MPShovelItem;

import static github.com.gengyoubo.MPG.register;

public class MPItemCore {
    public static final Item ManaitaSword = register(BuiltInRegistries.ITEM, "manaita_sword", new MPSwordItem());
    public static final Item ManaitaSwordGod = register(BuiltInRegistries.ITEM, "manaita_sword_god", new MPGodSwordItem());

    public static final Item ManaitaBow = register(BuiltInRegistries.ITEM, "manaita_bow", new MPBowItem());

    public static final Item ManaitaAxe = register(BuiltInRegistries.ITEM, "manaita_axe", new MPAxeItem());
    public static final Item ManaitaHoe = register(BuiltInRegistries.ITEM, "manaita_hoe", new MPHoeItem());
    public static final Item ManaitaPaxel = register(BuiltInRegistries.ITEM, "manaita_paxel", new MPPaxelItem());
    public static final Item ManaitaPickaxe = register(BuiltInRegistries.ITEM, "manaita_pickaxe", new MPPickaxeItem());
    public static final Item ManaitaShears = register(BuiltInRegistries.ITEM, "manaita_shears", new MPShearsItem());
    public static final Item ManaitaShovel = register(BuiltInRegistries.ITEM, "manaita_shovel", new MPShovelItem());
    public static final Item ManaitaHelmet = register(BuiltInRegistries.ITEM, "manaita_helmet", new MPArmor.Helmet());
    public static final Item ManaitaChestplate = register(BuiltInRegistries.ITEM, "manaita_chestplate", new MPArmor.Chestplate());
    public static final Item ManaitaLeggings = register(BuiltInRegistries.ITEM, "manaita_leggings", new MPArmor.Leggings());
    public static final Item ManaitaBoots = register(BuiltInRegistries.ITEM, "manaita_boots", new MPArmor.Boots());

    public static final Item ManaitaSource = register(BuiltInRegistries.ITEM, "manaita_source", new MPSourceItem());
    public static final Item ManaitaHook = register(BuiltInRegistries.ITEM, "manaita_hook", new MPHookItem());
    public static final Item ManaitaCraftingRing = register(BuiltInRegistries.ITEM, "manaita_crafting_ring", MPTrinketsCompat.createRingItem("item.ringCrafting.", "CRAFTING"));
    public static final Item ManaitaFurnaceRing = register(BuiltInRegistries.ITEM, "manaita_furnace_ring", MPTrinketsCompat.createRingItem("item.ringFurnace.", "FURNACE"));
    public static final Item ManaitaBrewingRing = register(BuiltInRegistries.ITEM, "manaita_brewing_ring", MPTrinketsCompat.createRingItem("item.ringBrewing.", "BREWING"));

    public static final Item ManaitaCraftingPortable = register(BuiltInRegistries.ITEM, "manaita_crafting_portable", new MPCraftingPortable());
    public static final Item ManaitaFurnacePortable = register(BuiltInRegistries.ITEM, "manaita_furnace_portable", new MPFurnacePortable());
    public static final Item ManaitaBrewingPortable = register(BuiltInRegistries.ITEM, "manaita_brewing_portable", new MPBrewingPortable());
}
