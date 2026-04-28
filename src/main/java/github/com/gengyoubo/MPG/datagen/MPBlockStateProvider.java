package github.com.gengyoubo.MPG.datagen;

import github.com.gengyoubo.MPG.MPG;
import github.com.gengyoubo.MPG.core.MPGBlockCore;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import github.com.gengyoubo.MPG.block.data.MPGBlockData;
import github.com.gengyoubo.MPG.util.MPUtils;

public class MPBlockStateProvider extends BlockStateProvider {

    public MPBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MPG.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        VariantBlockStateBuilder builder;

        horizontalBlock(MPGBlockCore.HookBlock.get(), state -> {
            String typeName = MPUtils.getTypes1(state.getValue(MPGBlockData.TYPES) + 1);
            Direction direction1 = state.getValue(MPGBlockData.FACING);
            ModelBuilder<BlockModelBuilder>.ElementBuilder modelFile = models().getBuilder("block/hook/hook_block_" + typeName  + "_" + direction1.getName())
                    .parent(models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER + "/cube_all")))
                    .texture("all", modLoc("block/hook/fixed_hook_" + typeName))
                    .element();
            modelFile.from(7, 12, 14)
                    .to(9, 14, 16);
            return modelFile.allFaces((direction, faceBuilder) ->
                            faceBuilder.texture("#all").uvs(5, 5, 10, 10))
                    .end();
        });

        builder = getVariantBuilder(MPGBlockCore.CraftingBlock.get());

        for (int i = 0; i <= 8; i++) {
            if(i == 0) {
                ModelFile modelFile =  models().getBuilder("crafting_manaita_model")
                        .parent(models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER  + "/cube_all")))
                        .texture("particle", modLoc("block/crafting_manaita"));

                builder.partialState()
                        .with(MPGBlockData.TYPES, i)
                        .addModels(new ConfiguredModel(modelFile));
                continue;
            }
            String typeName = MPUtils.getTypes1(i);
            ModelFile modelFile =  models().getBuilder("block/crafting/model/crafting_manaita_" + typeName)
                    .parent(models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER  + "/cube_all")))
                    .texture("particle", modLoc("block/crafting/crafting_manaita_" + MPUtils.getTypes1(i)));

            builder.partialState()
                    .with(MPGBlockData.TYPES, i)
                    .addModels(new ConfiguredModel(modelFile));
        }

        builder = getVariantBuilder(MPGBlockCore.FurnaceBlock.get());

        for (int i = 0; i <= 8; i++) {
            if(i == 0) {
                ModelFile modelFile =  models().getBuilder("furnace_manaita_model")
                        .parent(models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER  + "/cube_all")))
                        .texture("particle", modLoc("block/furnace_manaita"));

                builder.partialState()
                        .with(MPGBlockData.TYPES, i)
                        .addModels(new ConfiguredModel(modelFile));
                continue;
            }
            String typeName = MPUtils.getTypes1(i);
            ModelFile modelFile =  models().getBuilder("block/furnace/model/furnace_manaita_" + typeName)
                    .parent(models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER  + "/cube_all")))
                    .texture("particle", modLoc("block/furnace/furnace_manaita_" + MPUtils.getTypes1(i)));

            builder.partialState()
                    .with(MPGBlockData.TYPES, i)
                    .addModels(new ConfiguredModel(modelFile));
        }
        builder = getVariantBuilder(MPGBlockCore.BrewingBlock.get());

        for (int i = 0; i <= 8; i++) {
            if(i == 0) {
                ModelFile modelFile =  models().getBuilder("brewing_manaita_model")
                        .parent(models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER  + "/cube_all")))
                        .texture("particle", modLoc("block/brewing_manaita"));

                builder.partialState()
                        .with(MPGBlockData.TYPES, i)
                        .addModels(new ConfiguredModel(modelFile));
                continue;
            }
            String typeName = MPUtils.getTypes1(i);
            ModelFile modelFile =  models().getBuilder("block/brewing/model/brewing_manaita_" + typeName)
                    .parent(models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER  + "/cube_all")))
                    .texture("particle", modLoc("block/brewing/brewing_manaita_" + MPUtils.getTypes1(i)));

            builder.partialState()
                    .with(MPGBlockData.TYPES, i)
                    .addModels(new ConfiguredModel(modelFile));
        }
    }
}
