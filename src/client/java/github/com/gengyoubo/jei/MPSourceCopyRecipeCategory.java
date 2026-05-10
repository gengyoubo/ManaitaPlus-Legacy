package github.com.gengyoubo.jei;

import github.com.gengyoubo.MPG;
import github.com.gengyoubo.core.MPItemCore;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MPSourceCopyRecipeCategory implements IRecipeCategory<MPSourceCopyJeiRecipe> {
    public static final RecipeType<MPSourceCopyJeiRecipe> TYPE =
            RecipeType.create("manaita_plus_general", "source_copying", MPSourceCopyJeiRecipe.class);
    private static final int GRID_X = 1;
    private static final int GRID_Y = 1;
    private static final int SLOT_SIZE = 18;
    private static final int ARROW_X = 66;
    private static final int ARROW_Y = 20;
    private static final int OUTPUT_X = 96;
    private static final int OUTPUT_Y = 19;
    private static final int RESULT_COUNT_X = 101;
    private static final int RESULT_COUNT_Y = 7;
    private static final int SHAPELESS_X = 58;
    private static final int SHAPELESS_Y = 1;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public MPSourceCopyRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(118, 58);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(MPItemCore.ManaitaSource));
        this.arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public @NotNull RecipeType<MPSourceCopyJeiRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.manaita_plus_general.source_copying");
    }

    @Override
    @SuppressWarnings("removal")
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MPSourceCopyJeiRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.setShapeless(SHAPELESS_X, SHAPELESS_Y);
        addCraftingGridSlot(builder, 0, 0, new ItemStack(MPItemCore.ManaitaSource));
        IRecipeSlotBuilder inputSlot = builder.addSlot(RecipeIngredientRole.INPUT, GRID_X + SLOT_SIZE, GRID_Y)
                .addItemStacks(recipe.inputs())
                .setStandardSlotBackground();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if ((x == 0 && y == 0) || (x == 1 && y == 0)) {
                    continue;
                }
                addEmptyCraftingGridSlot(builder, x, y);
            }
        }
        IRecipeSlotBuilder outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                .addItemStacks(recipe.outputs())
                .setOutputSlotBackground();
        builder.createFocusLink(inputSlot, outputSlot);
    }

    @Override
    public void draw(MPSourceCopyJeiRecipe recipe, @NotNull mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, ARROW_X, ARROW_Y);
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }
        guiGraphics.drawString(minecraft.font, Component.literal("x" + recipe.multiplier()), RESULT_COUNT_X, RESULT_COUNT_Y, 0x404040, false);
    }

    private static void addCraftingGridSlot(IRecipeLayoutBuilder builder, int x, int y, ItemStack stack) {
        builder.addSlot(RecipeIngredientRole.INPUT, GRID_X + x * SLOT_SIZE, GRID_Y + y * SLOT_SIZE)
                .addItemStack(stack)
                .setStandardSlotBackground();
    }

    private static void addEmptyCraftingGridSlot(IRecipeLayoutBuilder builder, int x, int y) {
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, GRID_X + x * SLOT_SIZE, GRID_Y + y * SLOT_SIZE)
                .setStandardSlotBackground();
    }

    @Override
    public ResourceLocation getRegistryName(MPSourceCopyJeiRecipe recipe) {
        return new ResourceLocation(MPG.MODID, "source_copying");
    }
}
