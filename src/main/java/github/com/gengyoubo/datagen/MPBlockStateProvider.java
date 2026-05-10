package github.com.gengyoubo.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import github.com.gengyoubo.MPG;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MPBlockStateProvider implements DataProvider {
    private static final String[] HOOK_TYPE_MODELS = {
            "wooden",
            "stone",
            "iron",
            "gold",
            "diamond",
            "emerald",
            "redstone",
            "netherite",
            "netherite"
    };

    private static final String[] HORIZONTAL_FACINGS = {"north", "east", "south", "west"};
    private static final int[] HORIZONTAL_Y_ROTATIONS = {0, 90, 180, 270};

    private final FabricDataOutput output;

    public MPBlockStateProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public @NotNull CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        futures.add(save(cache, "block_hook_manaita", hookBlockState()));
        futures.add(save(cache, "block_crafting_manaita", typedMultipartState("block/block_crafting_manaita", "item/crafting/crafting_manaita.")));
        futures.add(save(cache, "block_furnace_manaita", typedMultipartState("block/block_furnace_manaita", "item/furnace/furnace_manaita.")));
        futures.add(save(cache, "block_brewing_manaita", typedMultipartState("block/block_brewing_manaita", "item/brewing/brewing_manaita.")));

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public @NotNull String getName() {
        return "ManaitaPlusGeneral block states";
    }

    private JsonObject hookBlockState() {
        JsonObject variants = new JsonObject();

        for (int type = 0; type <= 8; type++) {
            ResourceLocation model = modelId("block/hook/fixed_hook_" + HOOK_TYPE_MODELS[type]);

            for (int i = 0; i < HORIZONTAL_FACINGS.length; i++) {
                JsonObject variant = new JsonObject();
                variant.addProperty("model", model.toString());
                if (HORIZONTAL_Y_ROTATIONS[i] != 0) {
                    variant.addProperty("y", HORIZONTAL_Y_ROTATIONS[i]);
                }
                variants.add("facing=" + HORIZONTAL_FACINGS[i] + ",types=" + type, variant);
            }
        }

        JsonObject root = new JsonObject();
        root.add("variants", variants);
        return root;
    }

    private JsonObject typedMultipartState(String baseModelPath, String typedModelPrefix) {
        JsonArray multipart = new JsonArray();
        multipart.add(multipartPart(0, modelId(baseModelPath)));

        for (int type = 1; type <= 8; type++) {
            multipart.add(multipartPart(type, modelId(typedModelPrefix + type)));
        }

        JsonObject root = new JsonObject();
        root.add("multipart", multipart);
        return root;
    }

    private JsonObject multipartPart(int type, ResourceLocation model) {
        JsonObject when = new JsonObject();
        when.addProperty("types", String.valueOf(type));

        JsonObject apply = new JsonObject();
        apply.addProperty("model", model.toString());

        JsonObject part = new JsonObject();
        part.add("when", when);
        part.add("apply", apply);
        return part;
    }

    private ResourceLocation modelId(String path) {
        return new ResourceLocation(MPG.MODID, path);
    }

    private CompletableFuture<?> save(CachedOutput cache, String blockStateName, JsonObject json) {
        Path path = output.getOutputFolder()
                .resolve("assets")
                .resolve(MPG.MODID)
                .resolve("blockstates")
                .resolve(blockStateName + ".json");
        return DataProvider.saveStable(cache, json, path);
    }
}
