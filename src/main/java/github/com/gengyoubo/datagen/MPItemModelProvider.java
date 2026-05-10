package github.com.gengyoubo.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import github.com.gengyoubo.MPG;
import github.com.gengyoubo.util.MPNBTData;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MPItemModelProvider implements DataProvider {
    private static final String PREDICATE = MPG.MODID + ":" + MPNBTData.Type;
    private static final String[] TYPE_TEXTURE_SUFFIXES = {
            "wooden",
            "stone",
            "iron",
            "gold",
            "diamond",
            "emerald",
            "redstone",
            "netherite"
    };

    private final FabricDataOutput output;

    public MPItemModelProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        generateHookItem(futures, cache);
        generateTypedBlockItem(futures, cache, "crafting", "block_crafting_manaita", "crafting_manaita", "crafting_manaita");
        generateTypedBlockItem(futures, cache, "furnace", "block_furnace_manaita", "furnace_manaita", "furnace_manaita");
        generateTypedBlockItem(futures, cache, "brewing", "block_brewing_manaita", "brewing_manaita", "brewing_manaita");

        generateNumberedTypeItem(futures, cache, "crafting", "manaita_crafting_portable", "portable_crafting_manaita");
        generateNumberedTypeItem(futures, cache, "furnace", "manaita_furnace_portable", "portable_furnace_manaita");
        generateNumberedTypeItem(futures, cache, "brewing", "manaita_brewing_portable", "portable_brewing_manaita");
        generateNumberedTypeItem(futures, cache, "crafting", "manaita_crafting_ring", "manaita_crafting_ring");
        generateNumberedTypeItem(futures, cache, "furnace", "manaita_furnace_ring", "manaita_furnace_ring");
        generateNumberedTypeItem(futures, cache, "brewing", "manaita_brewing_ring", "manaita_brewing_ring");

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "ManaitaPlusGeneral item models";
    }

    private void generateHookItem(List<CompletableFuture<?>> futures, CachedOutput cache) {
        JsonObject model = generatedModel("block/hook/fixed_hook_wooden");
        JsonArray overrides = new JsonArray();
        for (int type = 1; type <= 8; type++) {
            overrides.add(override(type, modelId("block/hook/fixed_hook_" + hookTypeName(type))));
        }
        model.add("overrides", overrides);
        futures.add(save(cache, "item/block_hook_manaita", model));
        futures.add(save(cache, "item/tool/block_hook_manaita", model));
    }

    private void generateTypedBlockItem(List<CompletableFuture<?>> futures, CachedOutput cache, String folder,
                                        String itemName, String variantPrefix, String texturePrefix) {
        String rootModel = "item/" + itemName;
        String linkedModel = "item/" + folder + "/" + itemName;
        String baseTexture = "block/" + texturePrefix;
        String variantModelPrefix = "item/" + folder + "/" + variantPrefix + ".";
        JsonObject root = typedRootModel(baseTexture, variantModelPrefix);

        futures.add(save(cache, rootModel, root));
        futures.add(save(cache, linkedModel, root));

        for (int type = 1; type <= 8; type++) {
            String typeName = TYPE_TEXTURE_SUFFIXES[type - 1];
            futures.add(save(cache, variantModelPrefix + type,
                    layeredGeneratedModel(baseTexture, "block/" + folder + "/" + texturePrefix + "_" + typeName)));
        }
    }

    private void generateNumberedTypeItem(List<CompletableFuture<?>> futures, CachedOutput cache, String folder,
                                          String itemName, String variantPrefix) {
        String rootModel = "item/" + itemName;
        String linkedModel = "item/" + folder + "/" + itemName;
        String baseTexture = "item/" + folder + "/" + variantPrefix + ".0";
        String variantModelPrefix = "item/" + folder + "/" + variantPrefix + ".";
        JsonObject root = typedRootModel(baseTexture, variantModelPrefix);

        futures.add(save(cache, rootModel, root));
        futures.add(save(cache, linkedModel, root));

        for (int type = 1; type <= 8; type++) {
            futures.add(save(cache, variantModelPrefix + type,
                    generatedModel("item/" + folder + "/" + variantPrefix + "." + type)));
        }
    }

    private JsonObject typedRootModel(String baseTexture, String variantModelPrefix) {
        JsonObject model = generatedModel(baseTexture);
        JsonArray overrides = new JsonArray();

        for (int type = 1; type <= 8; type++) {
            overrides.add(override(type, modelId(variantModelPrefix + type)));
        }

        model.add("overrides", overrides);
        return model;
    }

    private JsonObject generatedModel(String layer0) {
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", modelId(layer0).toString());

        JsonObject model = new JsonObject();
        model.addProperty("parent", "minecraft:item/generated");
        model.add("textures", textures);
        return model;
    }

    private JsonObject layeredGeneratedModel(String layer0, String layer1) {
        JsonObject model = generatedModel(layer0);
        model.getAsJsonObject("textures").addProperty("layer1", modelId(layer1).toString());
        return model;
    }

    private JsonObject override(int type, ResourceLocation modelId) {
        JsonObject predicate = new JsonObject();
        predicate.addProperty(PREDICATE, type / 10.0F - 0.05F);

        JsonObject override = new JsonObject();
        override.add("predicate", predicate);
        override.addProperty("model", modelId.toString());
        return override;
    }

    private String hookTypeName(int type) {
        return switch (type) {
            case 1 -> "stone";
            case 2 -> "iron";
            case 3 -> "gold";
            case 4 -> "diamond";
            case 5 -> "emerald";
            case 6 -> "redstone";
            default -> "netherite";
        };
    }

    private ResourceLocation modelId(String path) {
        return new ResourceLocation(MPG.MODID, path);
    }

    private CompletableFuture<?> save(CachedOutput cache, String modelPath, JsonObject json) {
        Path path = output.getOutputFolder()
                .resolve("assets")
                .resolve(MPG.MODID)
                .resolve("models")
                .resolve(modelPath + ".json");
        return DataProvider.saveStable(cache, json, path);
    }
}
