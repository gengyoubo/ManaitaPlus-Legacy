package github.com.gengyoubo;

import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import github.com.gengyoubo.compat.MPTrinketsCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.GsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import github.com.gengyoubo.core.*;
import github.com.gengyoubo.network.MPNetworking;
import github.com.gengyoubo.util.MPNBTData;

public class MPG implements ModInitializer {
    public static final String MODID = "manaita_plus_general";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    private static final int MAX_MANAITA_TYPE = 8;
    private static final int MAX_HOOK_TYPE = 7;
    public static CreativeModeTab MANAITA_PLUS_TAB;

    @Override
    public void onInitialize() {
        initFabric();
    }

    public static void initFabric() {
        MPGConfig.load();

        forceLoadRegistryHolders();
        MPSynchedDataCore.init();
        registerResourceConditions();
        MPNetworking.initServer();

        registerCreativeTab();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static <V, T extends V> T register(Registry<V> registry, String path, T value) {
        return Registry.register(registry, id(path), value);
    }

    private static void registerCreativeTab() {
        MANAITA_PLUS_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id("manaita_plus_tab"), FabricItemGroup.builder()
                .icon(MPBlockCore.CraftingBlockItem::getDefaultInstance)
                .title(Component.translatable("itemGroup.ManaitaPlusTab"))
                .displayItems(MPG::fillCreativeTab)
                .build());
    }

    private static void fillCreativeTab(CreativeModeTab.ItemDisplayParameters context, CreativeModeTab.Output entries) {
        acceptTypedItems(entries, MAX_MANAITA_TYPE,
                MPBlockCore.CraftingBlockItem,
                MPBlockCore.FurnaceBlockItem,
                MPBlockCore.BrewingBlockItem
        );
        acceptMPGType(MPBlockCore.HookBlockItem, entries, MAX_HOOK_TYPE);

        acceptTypedItems(entries, MAX_MANAITA_TYPE,
                MPItemCore.ManaitaCraftingPortable,
                MPItemCore.ManaitaFurnacePortable,
                MPItemCore.ManaitaBrewingPortable
        );

        acceptItems(entries,
                MPItemCore.ManaitaSword,
                MPItemCore.ManaitaSwordGod,
                MPItemCore.ManaitaBow,
                MPItemCore.ManaitaAxe,
                MPItemCore.ManaitaHoe,
                MPItemCore.ManaitaPaxel,
                MPItemCore.ManaitaPickaxe,
                MPItemCore.ManaitaShears,
                MPItemCore.ManaitaShovel,
                MPItemCore.ManaitaHelmet,
                MPItemCore.ManaitaChestplate,
                MPItemCore.ManaitaLeggings,
                MPItemCore.ManaitaBoots,
                MPItemCore.ManaitaSource
        );

        if (MPTrinketsCompat.isLoaded()) {
            acceptTypedItems(entries, MAX_MANAITA_TYPE,
                    MPItemCore.ManaitaCraftingRing,
                    MPItemCore.ManaitaFurnaceRing,
                    MPItemCore.ManaitaBrewingRing
            );
        }
    }

    private static void registerResourceConditions() {
        ResourceConditions.register(new ResourceLocation(MODID, "easy_mode"), MPG::matchesEasyMode);
        ResourceConditions.register(new ResourceLocation(MODID, "trinkets_loaded"), MPG::isTrinketsLoadedCondition);
    }

    private static boolean matchesEasyMode(JsonObject json) {
        return GsonHelper.getAsBoolean(json, "value", true) == MPGConfig.easy_mode_value;
    }

    private static boolean isTrinketsLoadedCondition(JsonObject json) {
        return MPTrinketsCompat.isLoaded();
    }

    private static void forceLoadRegistryHolders() {
        forceInit(MPBlockCore.class);
        forceInit(MPItemCore.class);
        forceInit(MPBlockEntityCore.class);
        forceInit(MPMenuCore.class);
        forceInit(MPRecipeSerializerCore.class);
    }

    private static void forceInit(Class<?> type) {
        try {
            Class.forName(type.getName(), true, type.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to initialize registry holder: " + type.getName(), e);
        }
    }

    private static void acceptTypedItems(CreativeModeTab.Output entries, int maxType, Item... items) {
        for (Item item : items) {
            acceptMPGType(item, entries, maxType);
        }
    }

    private static void acceptItems(CreativeModeTab.Output entries, Item... items) {
        for (Item item : items) {
            entries.accept(item);
        }
    }

    private static void acceptMPGType(Item item, CreativeModeTab.Output entries, int maxType) {
        for (int type = 0; type <= maxType; type++) {
            ItemStack stack = new ItemStack(item);
            stack.getOrCreateTag().putInt(MPNBTData.ItemType, type);
            entries.accept(stack);
        }
    }
}
