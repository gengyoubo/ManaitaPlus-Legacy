package github.com.gengyoubo.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import github.com.gengyoubo.MPGConfig;
import github.com.gengyoubo.menu.MPGFurnaceMenu;

@OnlyIn(Dist.CLIENT)
public class FurnaceManaitaScreen extends AbstractFurnaceScreen<MPGFurnaceMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/furnace.png");
    private final String doubling_text;

    public FurnaceManaitaScreen(MPGFurnaceMenu p_98776_, Inventory p_98777_, Component p_98778_) {
        super(p_98776_, new SmeltingRecipeBookComponent(), p_98777_, p_98778_, TEXTURE);
        doubling_text = MPGConfig.furnace_doubling_value + "x";
    }


    protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {
        p_281635_.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        p_281635_.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
        p_281635_.drawString(this.font, doubling_text,118, 22, 4210752);
    }

}