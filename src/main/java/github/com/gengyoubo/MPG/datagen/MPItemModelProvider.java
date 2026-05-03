package github.com.gengyoubo.MPG.datagen;

import github.com.gengyoubo.MPG.MPG;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MPItemModelProvider extends ItemModelProvider {

    public MPItemModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MPG.MODID, exFileHelper);
    }

    @Override
    protected void registerModels() {
        registerTypedModelOverrides(
                withExistingParent("hook_block_item", "item/generated")
                        .texture("layer0", modLoc("block/hook/fixed_hook_wooden")),
                "block/hook/fixed_hook_",
                8,
                true
        );

        registerTypedModelOverrides(
                withExistingParent("block_crafting_manaita", "item/generated")
                        .texture("layer0", modLoc("block/crafting_manaita")),
                "block/crafting/crafting_manaita_",
                8,
                false
        );

        registerTypedModelOverrides(
                withExistingParent("block_furnace_manaita", "item/generated")
                        .texture("layer0", modLoc("block/furnace_manaita")),
                "block/furnace/furnace_manaita_",
                8,
                false
        );

        registerTypedModelOverrides(
                withExistingParent("block_brewing_manaita", "item/generated")
                        .texture("layer0", modLoc("block/brewing_manaita")),
                "block/brewing/brewing_manaita_",
                8,
                false
        );
    }

    private void registerTypedModelOverrides(ItemModelBuilder builder, String texturePrefix, int maxType, boolean hookOffset) {
        for (int i = 1; i <= maxType; i++) {
            int type = hookOffset ? i + 1 : i;
            String typeName = getTypeName(type);
            String modelName = texturePrefix.substring(texturePrefix.lastIndexOf('/') + 1) + typeName;

            builder.override()
                    .predicate(modLoc("manaita_plus_general_type"), i)
                    .model(withExistingParent(modelName, "item/generated")
                            .texture("layer0", modLoc(texturePrefix + typeName)))
                    .end();
        }
    }

    private String getTypeName(int type) {
        return switch (type) {
            case 2 -> "stone";
            case 3 -> "iron";
            case 4 -> "gold";
            case 5 -> "diamond";
            case 6 -> "emerald";
            case 7 -> "redstone";
            case 8 -> "netherite";
            default -> "wooden";
        };
    }
}
