package net.lade.lademod.gui.screen;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import net.lade.lademod.LadeMod;
import net.lade.lademod.gui.GeoGenMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.data.AtlasProvider;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.AtlasManager;
import net.minecraft.client.resources.model.Material;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static net.minecraft.network.chat.Component.translatable;

public class GeoGenScreen extends AbstractContainerScreen<GeoGenMenu> {

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(LadeMod.MODID, "textures/gui/geogen.png");

    int capacity = 16000;
    int tankHeight = 69;
    int tankWidth = 34;
    int tank1X = 8;
    int tank2X = 44;
    int tank3X = 134;
    int tankY = 7;
    int tankBottomY = 76;


    public GeoGenScreen(GeoGenMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);

    }

    @Override
    protected void init() {
        super.init();
        imageHeight = 256;
        imageWidth = 256;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, title.getString(), 85, 6, 0x404040);
    }
    RenderPipeline pipeline = RenderPipelines.GUI_TEXTURED;

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int mX, int mY) {
        FluidStack in1Fluid = menu.getClientFluid(0);
        FluidStack in2Fluid = menu.getClientFluid(1);
        FluidStack outFluid = menu.getClientFluid(2);

        int in1FluidAmount = menu.getClientFluidAmount(0);
        int in2FluidAmount = menu.getClientFluidAmount(1);
        int outFluidAmount = menu.getClientFluidAmount(2);


        int fluid1Height = calcHeight(in1FluidAmount, capacity, tankHeight);
        int fluid2Height = calcHeight(in2FluidAmount, capacity, tankHeight);
        int fluid3Height = calcHeight(outFluidAmount, capacity, tankHeight);

        int x = this.leftPos;
        int y = this.topPos;

        guiGraphics.blit(pipeline, TEXTURE, x, y, 0,0, imageWidth, imageHeight,imageWidth, imageHeight);

        drawFluid(in1Fluid, guiGraphics, x + tank1X, y + tankBottomY - fluid1Height, tankWidth, fluid1Height);
        drawFluid(in2Fluid, guiGraphics, x + tank2X, y + tankBottomY - fluid2Height, tankWidth, fluid2Height);
        drawFluid(outFluid, guiGraphics, x + tank3X, y + tankBottomY - fluid3Height, tankWidth, fluid3Height);

    }

    private void drawFluid(FluidStack fluidStack, GuiGraphics guiGraphics, int x, int y, int width, int height) {
        if (fluidStack.isEmpty()) return;
        IClientFluidTypeExtensions clientExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation fluidTextureLocation = clientExtensions.getStillTexture(fluidStack);

        TextureAtlasSprite sprite1 = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS).getSprite(fluidTextureLocation);

        int color = clientExtensions.getTintColor();

        float r = (float) (color >> 16 & 0xFF) / 255.0F;
        float g = (float) (color >> 8 & 0xFF) / 255.0F;
        float b = (float) (color & 0xFF) / 255.0F;
        float a = (float) (color >> 24 & 0xFF) / 255.0F;
        Color myColor = new Color(r, g, b, a);


        final int TILE_SIZE = 16;
        int drawnHeight = 0;
        while (drawnHeight < height) {
            int tileHeight = Math.min(height - drawnHeight, TILE_SIZE);
            int drawY = y + height - drawnHeight - tileHeight;

            guiGraphics.blitSprite(pipeline, sprite1, x, drawY ,width, tileHeight, myColor.getRGB());

            drawnHeight += tileHeight;
        }

    }

    private int calcHeight(int amount, int cap, int maxHeight) {
        return Math.round((((float) amount / cap) * maxHeight));
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        List<ClientTooltipComponent> tooltip = new ArrayList<>();
        int fluid1InMilliBuckets = menu.getClientFluidAmount(0);
        int fluid2InMilliBuckets = menu.getClientFluidAmount(1);
        int fluid3InMilliBuckets = menu.getClientFluidAmount(2);

        int tank1X = this.getGuiLeft() + this.tank1X;
        int tank2X = this.getGuiLeft() + this.tank2X;
        int tank3X = this.getGuiLeft() + this.tank3X;
        int tankY = this.getGuiTop() + this.tankY;
        boolean isHover = false;

        ClientTooltipPositioner positioner = (screenWidth, screenHeight, tooltipWidth, tooltipHeight, mouseX2, mouseY2) ->
                new Vector2i(mouseX + 15, mouseY);


        if (mouseX >= tank1X && mouseX < tank1X + tankWidth &&
                mouseY >= tankY && mouseY < tankY + tankHeight) {
            tooltip.add(ClientTooltipComponent.create(translatable("tooltip.lademod.geogen.fluid1", fluid1InMilliBuckets).withStyle(ChatFormatting.BLUE).getVisualOrderText()));
            tooltip.add(ClientTooltipComponent.create(translatable("tooltip.lademod.geogen.capacity", capacity).withStyle(ChatFormatting.BLUE).getVisualOrderText()));
            isHover = true;
        }
        if (mouseX >= tank2X && mouseX < tank2X + tankWidth &&
                mouseY >= tankY && mouseY < tankY + tankHeight) {
            tooltip.add(ClientTooltipComponent.create(translatable("tooltip.lademod.geogen.fluid2", fluid2InMilliBuckets).withStyle(ChatFormatting.GOLD).getVisualOrderText()));
            tooltip.add(ClientTooltipComponent.create(translatable("tooltip.lademod.geogen.capacity", capacity).withStyle(ChatFormatting.GOLD).getVisualOrderText()));
            isHover = true;
        }
        if (mouseX >= tank3X && mouseX < tank3X + tankWidth &&
                mouseY >= tankY && mouseY < tankY + tankHeight) {
            tooltip.add(ClientTooltipComponent.create(translatable("tooltip.lademod.geogen.fluid3", fluid3InMilliBuckets).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText()));
            tooltip.add(ClientTooltipComponent.create(translatable("tooltip.lademod.geogen.capacity", capacity).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText()));
            isHover = true;
        }


        if (isHover) {
            guiGraphics.renderTooltip(this.font, tooltip, mouseX, mouseY, positioner, null);
        }

    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mX, int mY, float v) {
        super.render(guiGraphics, mX, mY, v);
        renderTooltip(guiGraphics, mX, mY);
    }
}
