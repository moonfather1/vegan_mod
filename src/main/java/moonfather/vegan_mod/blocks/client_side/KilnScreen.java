package moonfather.vegan_mod.blocks.client_side;

import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.blocks.KilnMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KilnScreen extends AbstractContainerScreen<KilnMenu>
{
    public KilnScreen(KilnMenu menu, Inventory playerInventory, Component title) { super(menu, playerInventory, title); }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a)
    {
        super.extractBackground(graphics, mouseX, mouseY, a); // renders gray shading in the back, then calls renderBg
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, BG_LOCATION, x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        if (this.getMenu().getProgressPosition() > 0)
        {
            int yLine = 14 - (Mth.ceil(13f /*fire is 14x14*/ * this.menu.getProgressPosition() / this.menu.getProgressTarget()) + 1);
            yLine = 14; // just give me the whole fire;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, LIT_PROGRESS_SPRITE, 14, 14, 0, 14 - yLine, x + 56, y + 36 + 14 - yLine, 14, yLine);
            /// ////////////
            int xSize = Mth.ceil(24.0F * this.menu.getProgressPosition() / this.menu.getProgressTarget()); // arrow is 24
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BURN_PROGRESS_SPRITE, 24, 16, 0, 0, x + 79, y + 34, xSize, 16);
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY)
    {
        super.extractTooltip(graphics, mouseX, mouseY);
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
                graphics.setComponentTooltipForNextFrame(this.font, list, mouseX, mouseY);
            }
        }
        //graphics.setComponentTooltipForNextFrame(this.font, tooltipCustomizationsBrief, mouseX, mouseY);
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

    private static final Identifier BG_LOCATION = Identifier.fromNamespaceAndPath(VeganMod.MODID, "textures/gui/kiln.png");
    private static final Identifier LIT_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/furnace/lit_progress");
    private static final Identifier BURN_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/furnace/burn_progress");
}
