package github.com.gengyoubo.block.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public final class MPGLogicHelper {
    private MPGLogicHelper() {
    }

    public static boolean canBurn(RegistryAccess registryAccess, @Nullable Recipe<?> recipe, NonNullList<ItemStack> items, WorldlyContainer container) {
        if (items.get(0).isEmpty() || recipe == null) {
            return false;
        }
        ItemStack assembled = assembleResult(recipe, container, registryAccess);
        if (assembled.isEmpty()) {
            return false;
        }
        ItemStack output = items.get(2);
        return output.isEmpty() || ItemStack.isSameItem(output, assembled);
    }

    @SuppressWarnings("unchecked")
    public static ItemStack assembleResult(Recipe<?> recipe, WorldlyContainer container, RegistryAccess registryAccess) {
        return ((Recipe<WorldlyContainer>) recipe).assemble(container, registryAccess);
    }

    public static void awardUsedRecipesAndPopExperience(ServerPlayer player, NonNullList<ItemStack> items, Object2IntMap<ResourceLocation> recipesUsed) {
        List<Recipe<?>> recipes = getRecipesToAwardAndPopExperience(player.serverLevel(), player.position(), recipesUsed);
        player.awardRecipes(recipes);
        for (Recipe<?> recipe : recipes) {
            if (recipe != null) {
                player.triggerRecipeCrafted(recipe, items);
            }
        }
        recipesUsed.clear();
    }

    public static List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 pos, Object2IntMap<ResourceLocation> recipesUsed) {
        List<Recipe<?>> recipes = Lists.newArrayList();
        for (Object2IntMap.Entry<ResourceLocation> entry : recipesUsed.object2IntEntrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent(recipe -> {
                recipes.add(recipe);
                createExperience(level, pos, entry.getIntValue(), ((AbstractCookingRecipe) recipe).getExperience());
            });
        }
        return recipes;
    }

    private static void createExperience(ServerLevel level, Vec3 pos, int craftedCount, float xpPerItem) {
        int totalXp = Mth.floor((float) craftedCount * xpPerItem);
        float fractional = Mth.frac((float) craftedCount * xpPerItem);
        if (fractional != 0.0F && Math.random() < (double) fractional) {
            ++totalXp;
        }
        ExperienceOrb.award(level, pos, totalXp * 64);
    }
}
