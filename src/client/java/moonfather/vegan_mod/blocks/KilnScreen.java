package moonfather.vegan_mod.blocks;

import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.blocks.KilnMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class KilnScreen extends AbstractContainerScreen<KilnMenu>
{
    public KilnScreen(KilnMenu menu, Inventory playerInventory, Component title) { super(menu, playerInventory, title); }



    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)
    {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(BG_LOCATION, x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        if (this.getMenu().getProgressPosition() > 0)
        {
            int yLine = 14 - (Mth.ceil(13f /*fire is 14x14*/ * this.menu.getProgressPosition() / this.menu.getProgressTarget()) + 1);
            yLine = 14; // just give me the whole fire;
            guiGraphics.blitSprite(LIT_PROGRESS_SPRITE, 14, 14, 0, 14 - yLine, x + 56, y + 36 + 14 - yLine, 14, yLine);
            /// ////////////
            int xSize = Mth.ceil(24.0F * this.menu.getProgressPosition() / this.menu.getProgressTarget()); // arrow is 24
            guiGraphics.blitSprite(BURN_PROGRESS_SPRITE, 24, 16, 0, 0, x + 79, y + 34, xSize, 16);
        }
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y)
    {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && ! this.hoveredSlot.hasItem() && this.hoveredSlot.index == KilnMenu.SLOT_BYPRODUCT)
        {
            if (this.minecraft.screen != null)
            {
                List<Component> list;
                if (this.getMenu().getOilAmount() < 250)
                {
                    list = tooltipByproduct0;
                    if (tooltipByproduct0.isEmpty())
                    {
                        initTooltip(tooltipByproduct0, "0");
                    }
                }
                else if (this.getMenu().getOilAmount() < 500)
                {
                    list = tooltipByproduct1;
                    if (tooltipByproduct1.isEmpty())
                    {
                        initTooltip(tooltipByproduct1, "1");
                    }
                }
                else
                {
                    list = tooltipByproduct2;
                    if (tooltipByproduct2.isEmpty())
                    {
                        initTooltip(tooltipByproduct2, "2");
                    }
                }
                guiGraphics.renderTooltip(this.font, list, Optional.empty(), x, y);
            }
        }
        super.renderTooltip(guiGraphics, x, y);
    }

    private static void initTooltip(List<Component> list, String suffix)
    {
        list.add(title);
        Arrays.stream(Language.getInstance().getOrDefault(BYPRODUCT_SLOT_TEXT_KEY + suffix)
                        .split("\n"))
                .forEach(text -> list.add(Component.literal(text).withStyle(ChatFormatting.DARK_GRAY)));
    }
    private static final String BYPRODUCT_SLOT_TEXT_KEY = "message.vegan_mod.kiln_byproduct_slot_";

    private static final List<Component> tooltipByproduct0 = new ArrayList<>(15), tooltipByproduct1 = new ArrayList<>(15), tooltipByproduct2 = new ArrayList<>(15);
    private static final Component title = Component.translatable(BYPRODUCT_SLOT_TEXT_KEY + "T").withColor(0xffaaa5a5);

    private static final ResourceLocation BG_LOCATION = ResourceLocation.fromNamespaceAndPath(VeganMod.MOD_ID, "textures/gui/kiln.png");
    private static final ResourceLocation LIT_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/lit_progress");
    private static final ResourceLocation BURN_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");
}
