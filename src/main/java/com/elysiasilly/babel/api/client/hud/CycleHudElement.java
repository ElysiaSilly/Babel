package com.elysiasilly.babel.api.client.hud;

import com.elysiasilly.babel.api.common.item.cycleable.CycleBlockItem;
import com.elysiasilly.babel.api.common.item.cycleable.PredefinedBlockState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Vector2f;

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
            elements().add(new Element(this));
        }
        if(item().canRandom()) elements().add(new Element(this));
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

    public PredefinedBlockState block(int index) {
        return item().block(index);
    }

    public int position() {
        return item.index();
    }

    public boolean selected(Element element) {
        return position() == element.index();
    }

    public boolean idle() {
        return this.tick > 400;
    }

    public void render(GuiGraphics guiGraphics) {
        if(this.pPosition != position()) tick = 0;
        tick++;

        float x = (float) (guiGraphics.guiWidth() / 2);
        float y = (float) (guiGraphics.guiHeight() / 2) + 16;

        for(Element element : elements()) {
            guiGraphics.pose().pushPose();
            element.render(guiGraphics, Minecraft.getInstance(),guiGraphics.bufferSource(), guiGraphics.pose(), new Vector2f(x + (element.position() * 20), y), new Vector2f(x, y));
            guiGraphics.pose().popPose();
        }

        this.pPosition = position();
    }

    public static class Element {

        private final CycleHudElement element;

        private Vector2f pPos;

        private float scale = 0, textScale = 0;

        public Element(CycleHudElement element) {
            this.element = element;
        }

        public PredefinedBlockState state() {
            return element().block(index());
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

        public boolean idle() {
            return element().idle();
        }

        public boolean selected() {
            return element().selected(this);
        }

        public boolean random() {
            return index() >= element().item().size();
        }

        public void render(GuiGraphics guiGraphics, Minecraft mc, MultiBufferSource multiBufferSource, PoseStack poseStack, Vector2f pos, Vector2f centrePos) {

            if(selected()) poseStack.translate(0, 0, 400);

            poseStack.pushPose();

            if(this.pPos == null) this.pPos = centrePos;

            this.pPos.lerp(element().idle() ? centrePos : pos, element().idle() ? 0.1f : 0.05f);

            poseStack.translate(this.pPos.x, this.pPos.y, 0);

            this.scale = Mth.lerp(element().idle() ? 0.05f : 0.1f, this.scale, element().idle() ? selected() ? 10 : 0 : selected() ? 16 : 10);
            float offset = this.scale * .5f;

            poseStack.last().pose().translate(-offset, offset, 0);

            poseStack.rotateAround(Axis.YP.rotationDegrees(45), offset, offset, offset);
            poseStack.rotateAround(Axis.XP.rotationDegrees(-22.5f), offset, offset, offset);
            poseStack.rotateAround(Axis.ZP.rotationDegrees(-22.5f), offset, offset, offset);

            poseStack.scale(this.scale, -this.scale, this.scale);

            if(this.scale != 0 || selected()) {
                mc.getBlockRenderer().renderSingleBlock(element().item().block(index()).get(), poseStack, multiBufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
                if(random()) {
                    poseStack.popPose();
                    this.textScale = Mth.lerp(element().idle() ? 0.05f : 0.1f, this.textScale, element().idle() && !selected() ? 0 : 1);
                    poseStack.last().pose().translate(this.pPos.x + 10 - mc.font.width("R"), this.pPos.y + 3, 200);
                    poseStack.scale(this.textScale, this.textScale, this.textScale);

                    guiGraphics.drawString(mc.font, "R", 0, 0, 16777215, true);
                }
            }
        }
    }
}
