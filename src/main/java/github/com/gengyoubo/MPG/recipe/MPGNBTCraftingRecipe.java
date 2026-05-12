package github.com.gengyoubo.MPG.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import github.com.gengyoubo.MPG.core.MPGRecipeSerializerCore;
import github.com.gengyoubo.MPG.recipe.ingredient.MPGNBTIngredient;
import github.com.gengyoubo.MPG.util.MPGNBTData;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.IShapedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class MPGNBTCraftingRecipe implements CraftingRecipe, IShapedRecipe<CraftingContainer> {
    private final ResourceLocation id;
    private final String group;
    private final CraftingBookCategory category;
    private final int width;
    private final int height;
    private final IngredientSpec[] ingredients;
    private final ItemStack result;
    private final boolean showNotification;

    public MPGNBTCraftingRecipe(ResourceLocation id, String group, CraftingBookCategory category, int width, int height, IngredientSpec[] ingredients, ItemStack result, boolean showNotification) {
        this.id = id;
        this.group = group;
        this.category = category;
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
        this.result = result;
        this.showNotification = showNotification;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return MPGRecipeSerializerCore.NBTCraftingRecipe.get();
    }

    @Override
    public @NotNull String getGroup() {
        return group;
    }

    @Override
    public @NotNull CraftingBookCategory category() {
        return category;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.withSize(width * height, Ingredient.EMPTY);
        for (int i = 0; i < ingredients.length; i++) {
            list.set(i, ingredients[i].toDisplayIngredient());
        }
        return list;
    }

    @Override
    public boolean showNotification() {
        return showNotification;
    }

    @Override
    public int getRecipeWidth() {
        return width;
    }

    @Override
    public int getRecipeHeight() {
        return height;
    }

    public @NotNull List<List<ItemStack>> getDisplayIngredients() {
        return Arrays.stream(ingredients)
                .map(IngredientSpec::getDisplayStacks)
                .toList();
    }

    public @NotNull ItemStack getDisplayResult() {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int gridWidth, int gridHeight) {
        return gridWidth >= width && gridHeight >= height;
    }

    @Override
    public boolean matches(CraftingContainer container, @NotNull Level level) {
        for (int x = 0; x <= container.getWidth() - width; ++x) {
            for (int y = 0; y <= container.getHeight() - height; ++y) {
                if (matches(container, x, y, true) || matches(container, x, y, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matches(CraftingContainer container, int offsetX, int offsetY, boolean mirrored) {
        for (int x = 0; x < container.getWidth(); ++x) {
            for (int y = 0; y < container.getHeight(); ++y) {
                int patternX = x - offsetX;
                int patternY = y - offsetY;
                IngredientSpec expected = IngredientSpec.EMPTY;
                if (patternX >= 0 && patternY >= 0 && patternX < width && patternY < height) {
                    expected = mirrored
                            ? ingredients[width - patternX - 1 + patternY * width]
                            : ingredients[patternX + patternY * width];
                }

                if (!expected.test(container.getItem(x + y * container.getWidth()))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer container, @NotNull RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean isIncomplete() {
        return Arrays.stream(ingredients)
                .map(IngredientSpec::toDisplayIngredient)
                .filter(ingredient -> !ingredient.isEmpty())
                .anyMatch(ForgeHooks::hasNoElements);
    }

    public static class Serializer implements RecipeSerializer<MPGNBTCraftingRecipe> {
        private static final MPGNBTIngredient EMPTY_NBT_INGREDIENT = new MPGNBTIngredient(Stream.empty());

        @Override
        public @NotNull MPGNBTCraftingRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            CraftingBookCategory category = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
            String[] pattern = shrink(patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
            if (pattern.length == 0) {
                throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
            }
            int width = pattern[0].length();
            int height = pattern.length;
            Map<Character, IngredientSpec> key = keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            IngredientSpec[] ingredients = dissolvePattern(pattern, key, width, height);
            ItemStack result = resultFromJson(GsonHelper.getAsJsonObject(json, "result"));
            boolean showNotification = GsonHelper.getAsBoolean(json, "show_notification", true);
            return new MPGNBTCraftingRecipe(id, group, category, width, height, ingredients, result, showNotification);
        }

        @Override
        public @NotNull MPGNBTCraftingRecipe fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
            int width = buf.readVarInt();
            int height = buf.readVarInt();
            String group = buf.readUtf();
            CraftingBookCategory category = buf.readEnum(CraftingBookCategory.class);
            IngredientSpec[] ingredients = new IngredientSpec[width * height];
            for (int i = 0; i < ingredients.length; i++) {
                ingredients[i] = IngredientSpec.fromNetwork(buf);
            }
            ItemStack result = buf.readItem();
            boolean showNotification = buf.readBoolean();
            return new MPGNBTCraftingRecipe(id, group, category, width, height, ingredients, result, showNotification);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, MPGNBTCraftingRecipe recipe) {
            buf.writeVarInt(recipe.width);
            buf.writeVarInt(recipe.height);
            buf.writeUtf(recipe.group);
            buf.writeEnum(recipe.category);
            for (IngredientSpec ingredient : recipe.ingredients) {
                ingredient.toNetwork(buf);
            }
            buf.writeItem(recipe.result);
            buf.writeBoolean(recipe.showNotification);
        }

        private static ItemStack resultFromJson(JsonObject json) {
            ItemStack stack = new ItemStack(ShapedRecipe.itemFromJson(json), GsonHelper.getAsInt(json, "count", 1));
            int type = readType(json);
            if (type >= 0) {
                stack.getOrCreateTag().putInt(MPGNBTData.ItemType, type);
            } else if (json.has("nbt")) {
                JsonObject nbt = GsonHelper.getAsJsonObject(json, "nbt");
                type = readType(nbt);
                if (type >= 0) {
                    stack.getOrCreateTag().putInt(MPGNBTData.ItemType, type);
                }
            }
            return stack;
        }

        private static int readType(JsonObject json) {
            if (json.has(MPGNBTData.ItemType)) {
                return GsonHelper.getAsInt(json, MPGNBTData.ItemType);
            }
            if (json.has("ManaitaPlusLegacyType")) {
                return GsonHelper.getAsInt(json, "ManaitaPlusLegacyType");
            }
            if (json.has("ManaitaType")) {
                return GsonHelper.getAsInt(json, "ManaitaType");
            }
            return -1;
        }

        private static IngredientSpec[] dissolvePattern(String[] pattern, Map<Character, IngredientSpec> key, int width, int height) {
            IngredientSpec[] ingredients = new IngredientSpec[width * height];
            Arrays.fill(ingredients, IngredientSpec.EMPTY);
            Map<Character, IngredientSpec> remaining = new HashMap<>(key);
            remaining.remove(' ');

            for (int y = 0; y < pattern.length; ++y) {
                for (int x = 0; x < pattern[y].length(); ++x) {
                    char symbol = pattern[y].charAt(x);
                    IngredientSpec ingredient = key.get(symbol);
                    if (ingredient == null) {
                        throw new JsonSyntaxException("Pattern references symbol '" + symbol + "' but it's not defined in the key");
                    }
                    remaining.remove(symbol);
                    ingredients[x + width * y] = ingredient;
                }
            }

            if (!remaining.isEmpty()) {
                throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + remaining.keySet());
            }
            return ingredients;
        }

        private static Map<Character, IngredientSpec> keyFromJson(JsonObject json) {
            Map<Character, IngredientSpec> map = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                if (entry.getKey().length() != 1) {
                    throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol");
                }
                char symbol = entry.getKey().charAt(0);
                if (symbol == ' ') {
                    throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
                }
                map.put(symbol, IngredientSpec.fromJson(GsonHelper.convertToJsonObject(entry.getValue(), "key")));
            }
            map.put(' ', IngredientSpec.EMPTY);
            return map;
        }

        private static String[] patternFromJson(JsonArray json) {
            String[] pattern = new String[json.size()];
            if (pattern.length == 0) {
                throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
            }
            if (pattern.length > 3) {
                throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
            }

            for (int i = 0; i < pattern.length; ++i) {
                String row = GsonHelper.convertToString(json.get(i), "pattern[" + i + "]");
                if (row.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                }
                if (i > 0 && pattern[0].length() != row.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }
                pattern[i] = row;
            }
            return pattern;
        }

        private static String[] shrink(String... pattern) {
            int firstColumn = Integer.MAX_VALUE;
            int lastColumn = 0;
            int leadingEmptyRows = 0;
            int trailingEmptyRows = 0;

            for (int row = 0; row < pattern.length; ++row) {
                String line = pattern[row];
                firstColumn = Math.min(firstColumn, firstNonSpace(line));
                int last = lastNonSpace(line);
                lastColumn = Math.max(lastColumn, last);
                if (last < 0) {
                    if (leadingEmptyRows == row) {
                        ++leadingEmptyRows;
                    }
                    ++trailingEmptyRows;
                } else {
                    trailingEmptyRows = 0;
                }
            }

            if (pattern.length == trailingEmptyRows) {
                return new String[0];
            }

            String[] shrunk = new String[pattern.length - trailingEmptyRows - leadingEmptyRows];
            for (int i = 0; i < shrunk.length; ++i) {
                shrunk[i] = pattern[i + leadingEmptyRows].substring(firstColumn, lastColumn + 1);
            }
            return shrunk;
        }

        private static int firstNonSpace(String line) {
            int i = 0;
            while (i < line.length() && line.charAt(i) == ' ') {
                i++;
            }
            return i;
        }

        private static int lastNonSpace(String line) {
            int i = line.length() - 1;
            while (i >= 0 && line.charAt(i) == ' ') {
                --i;
            }
            return i;
        }

        public static MPGNBTIngredient fromValues(Stream<? extends Ingredient.Value> values) {
            MPGNBTIngredient ingredient = new MPGNBTIngredient(values);
            return ingredient.isEmpty() ? EMPTY_NBT_INGREDIENT : ingredient;
        }

        public static Ingredient.Value valueFromJson(JsonObject json) {
            if (json.has("item")) {
                Item item = ShapedRecipe.itemFromJson(json);
                ItemStack stack = new ItemStack(item);
                if (json.has("type")) {
                    stack.getOrCreateTag().putInt(MPGNBTData.ItemType, GsonHelper.getAsInt(json, "type"));
                }
                return new Ingredient.ItemValue(stack);
            }
            if (json.has("tag")) {
                ResourceLocation tagId = new ResourceLocation(GsonHelper.getAsString(json, "tag"));
                return new Ingredient.TagValue(TagKey.create(Registries.ITEM, tagId));
            }
            throw new JsonParseException("An ingredient entry needs an item or tag");
        }
    }

    private record IngredientSpec(Ingredient ingredient, int requiredType) {
        private static final IngredientSpec EMPTY = new IngredientSpec(Ingredient.EMPTY, Integer.MIN_VALUE);

        private boolean test(ItemStack stack) {
            if (this == EMPTY) {
                return stack.isEmpty();
            }
            if (!ingredient.test(stack)) {
                return false;
            }
            if (requiredType == Integer.MIN_VALUE) {
                return true;
            }
            if (!stack.hasTag()) {
                return requiredType == 0;
            }
            return Objects.requireNonNull(stack.getTag()).getInt(MPGNBTData.ItemType) == requiredType;
        }

        private void toNetwork(FriendlyByteBuf buf) {
            ingredient.toNetwork(buf);
            buf.writeInt(requiredType);
        }

        private static IngredientSpec fromNetwork(FriendlyByteBuf buf) {
            return new IngredientSpec(Ingredient.fromNetwork(buf), buf.readInt());
        }

        private static IngredientSpec fromJson(JsonObject json) {
            int requiredType = Integer.MIN_VALUE;
            if (json.has("type")) {
                requiredType = GsonHelper.getAsInt(json, "type");
            }
            JsonObject ingredientJson = json.deepCopy();
            ingredientJson.remove("type");
            Ingredient ingredient = Ingredient.fromJson(ingredientJson);
            return new IngredientSpec(ingredient, requiredType);
        }

        private List<ItemStack> getDisplayStacks() {
            if (this == EMPTY) {
                return List.of();
            }
            return Arrays.stream(ingredient.getItems())
                    .map(ItemStack::copy)
                    .peek(stack -> {
                        if (requiredType >= 0) {
                            stack.getOrCreateTag().putInt(MPGNBTData.ItemType, requiredType);
                        }
                    })
                    .toList();
        }

        private Ingredient toDisplayIngredient() {
            if (this == EMPTY) {
                return Ingredient.EMPTY;
            }
            ItemStack[] stacks = getDisplayStacks().toArray(ItemStack[]::new);
            return Ingredient.of(stacks);
        }
    }
}
