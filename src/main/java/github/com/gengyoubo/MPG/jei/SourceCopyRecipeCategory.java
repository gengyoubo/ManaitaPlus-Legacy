package github.com.gengyoubo.MPG.jei;

import github.com.gengyoubo.MPG.core.MPGItemCore;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SourceCopyRecipeCategory implements IRecipeCategory<SourceCopyJeiRecipe> {
    public static final RecipeType<SourceCopyJeiRecipe> TYPE =
            RecipeType.create("manaita_plus_general", "source_copying", SourceCopyJeiRecipe.class);
    private static final int ARROW_X = 69;
    private static final int ARROW_Y = 22;
    private static final int SHAPELESS_ICON_X = 65;
    private static final int SHAPELESS_ICON_Y = 6;
    private static final int OUTPUT_SLOT_X = 99;
    private static final int OUTPUT_SLOT_Y = 17;
    private static final int WIDTH = 126;
    private static final int HEIGHT = 78;

    private final IDrawable slot;
    private final IDrawable outputSlot;
    private final IDrawable arrow;
    private final IDrawable icon;

    public SourceCopyRecipeCategory(IGuiHelper guiHelper) {
        this.slot = guiHelper.getSlotDrawable();
        this.outputSlot = guiHelper.getOutputSlot();
        this.arrow = guiHelper.getRecipeArrow();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(MPGItemCore.ManaitaSource.get()));
    }

    @Override
    public @NotNull RecipeType<SourceCopyJeiRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.manaita_plus_general.source_copying");
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull SourceCopyJeiRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.setShapeless(SHAPELESS_ICON_X, SHAPELESS_ICON_Y);
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 5).addItemStack(recipe.source());
        builder.addSlot(RecipeIngredientRole.INPUT, 23, 5).addItemStacks(recipe.inputs());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 22).addItemStacks(recipe.outputs());
    }

    @Override
    public void draw(@NotNull SourceCopyJeiRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                slot.draw(guiGraphics, 4 + x * 18, 4 + y * 18);
            }
        }
        outputSlot.draw(guiGraphics, OUTPUT_SLOT_X, OUTPUT_SLOT_Y);
        arrow.draw(guiGraphics, ARROW_X, ARROW_Y);
        guiGraphics.drawString(
                net.minecraft.client.Minecraft.getInstance().font,
                Component.translatable("jei.manaita_plus_general.source_copying.result", recipe.multiplier()),
                4,
                62,
                0x404040,
                false
        );
    }
}
