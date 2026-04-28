package github.com.gengyoubo.MPG.datagen;

import github.com.gengyoubo.MPG.MPG;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MPItemModelProvider extends ItemModelProvider {

    public MPItemModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MPG.MODID, exFileHelper);
    }

    @Override
    protected void registerModels() {
        var builder = withExistingParent("hook_block_item", "item/generated")
                .texture("layer0", modLoc("block/hook/fixed_hook_wooden"));

        // 涓轰笉鍚岀被鍨嬫壒閲忔坊鍔爋verride
        for (int i = 1; i <= 8; i++) {
            String typeName = getTypeName(i + 1); // 鑾峰彇绫诲瀷鍚嶇О
            builder.override()
                    .predicate(modLoc("manaita_plus_general_type"), i)
                    .model(withExistingParent("block/hook/fixed_hook_" + typeName, "item/generated")
                            .texture("layer0", modLoc("block/hook/fixed_hook_" + typeName)));
        }

        builder = withExistingParent("block_crafting_manaita", "item/generated")
                .texture("layer0", modLoc("block/crafting_manaita"));

        // 涓轰笉鍚岀被鍨嬫壒閲忔坊鍔爋verride
        for (int i = 1; i <= 8; i++) {
            String typeName = getTypeName(i); // 鑾峰彇绫诲瀷鍚嶇О
            builder.override()
                    .predicate(modLoc("manaita_plus_general_type"), i)
                    .model(withExistingParent("crafting_manaita_" + typeName, "item/generated")
                            .texture("layer0", modLoc("block/crafting/crafting_manaita_" + typeName)));
        }

        builder = withExistingParent("block_furnace_manaita", "item/generated")
                .texture("layer0", modLoc("block/furnace_manaita"));

        // 涓轰笉鍚岀被鍨嬫壒閲忔坊鍔爋verride
        for (int i = 1; i <= 8; i++) {
            String typeName = getTypeName(i); // 鑾峰彇绫诲瀷鍚嶇О
            builder.override()
                    .predicate(modLoc("manaita_plus_general_type"), i)
                    .model(withExistingParent("furnace_manaita_" + typeName, "item/generated")
                            .texture("layer0", modLoc("block/furnace/furnace_manaita_" + typeName)));
        }

        builder = withExistingParent("block_brewing_manaita", "item/generated")
                .texture("layer0", modLoc("block/brewing_manaita"));

        // 涓轰笉鍚岀被鍨嬫壒閲忔坊鍔爋verride
        for (int i = 1; i <= 8; i++) {
            String typeName = getTypeName(i); // 鑾峰彇绫诲瀷鍚嶇О
            builder.override()
                    .predicate(modLoc("manaita_plus_general_type"), i)
                    .model(withExistingParent("brewing_manaita_" + typeName, "item/generated")
                            .texture("layer0", modLoc("block/brewing/brewing_manaita_" + typeName)));
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
