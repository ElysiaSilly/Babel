package com.elysiasilly.babel.api.client.hud;

import com.elysiasilly.babel.api.common.item.cycleable.CycleBlockItem;
import com.elysiasilly.babel.api.common.item.cycleable.PredefinedBlockState;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class CycleHudElement {

    private final CycleBlockItem item;
    private final List<Element> elements = new ArrayList<>();
    private int tick = 0;
    private int pPosition = 0;

    public CycleHudElement(CycleBlockItem item) {
        this.item = item;
        for(PredefinedBlockState state : item.blocks()) {
            elements.add(new Element(state, this));
        }
    }

    public List<Element> elements() {
        return this.elements;
    }

    public CycleBlockItem item() {
        return this.item;
    }

    public int index(Element element) {
        return elements().indexOf(element);
    }

    public int position() {
        return item.index();
    }

    public boolean selected(Element element) {
        return position() == element.index();
    }

    public boolean idle() {
        return this.tick > 1000;
    }

    public void render(GuiGraphics guiGraphics) {
        if(this.pPosition != position()) tick = 0;
        tick++;
        for(Element element : elements()) {
            guiGraphics.pose().pushPose();
            element.render(guiGraphics);
            guiGraphics.pose().popPose();
        }
        this.pPosition = position();
    }

    public static class Element {

        private final CycleHudElement element;
        private final PredefinedBlockState state;

        private float size = 0;

        public Element(PredefinedBlockState state, CycleHudElement element) {
            this.state = state;
            this.element = element;
        }

        public PredefinedBlockState state() {
            return this.state;
        }

        public CycleHudElement element() {
            return this.element;
        }

        public int index() {
            return element().index(this);
        }

        public int position() {
            return index() - element().position();
        }

        public boolean selected() {
            return element().selected(this);
        }

        public void render(GuiGraphics guiGraphics) {

            Minecraft mc = Minecraft.getInstance();

            MultiBufferSource multiBufferSource = guiGraphics.bufferSource();
            PoseStack poseStack = guiGraphics.pose();

            poseStack.pushPose();

            float offsetX = (float) (guiGraphics.guiWidth() / 2) + (position() * 20);
            float offsetY = 16;

            poseStack.translate(offsetX, offsetY, 0);

            this.size = Mth.lerp(element().idle() ? 0.05f : 0.1f, this.size, element().idle() ? 0 : selected() ? 16 : 10);
            float offset = this.size * .5f;


            poseStack.last().pose().translate(-offset, offset, 0);

            poseStack.rotateAround(Axis.YP.rotationDegrees(45), offset, offset, offset);
            poseStack.rotateAround(Axis.XP.rotationDegrees(-22.5f), offset, offset, offset);
            poseStack.rotateAround(Axis.ZP.rotationDegrees(-22.5f), offset, offset, offset);

            poseStack.scale(this.size, -this.size, this.size);


            if(this.size != 0) mc.getBlockRenderer().renderSingleBlock(state.get(), poseStack, multiBufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);

            RenderSystem.disableBlend();

            poseStack.popPose();
        }
    }
}
