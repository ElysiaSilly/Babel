package com.elysiasilly.babel.api.client.hud;

import com.elysiasilly.babel.api.common.item.cycleable.CycleBlockItem;
import com.elysiasilly.babel.api.common.item.cycleable.DefinedBlockState;
import com.elysiasilly.babel.util.MathUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class CycleHudElement {

    private final CycleBlockItem item;
    private final List<Element> elements = new ArrayList<>();
    private int tick = 0;
    private int pPosition = 0;

    public CycleHudElement(CycleBlockItem item) {
        this.item = item;
        for(DefinedBlockState state : item.blocks()) {
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
        int index = elements().indexOf(element);
        return index + 1;
    }

    public int position() {
        return item.getIndex();
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
        private final DefinedBlockState state;

        private Vec3 rotation = Vec3.ZERO, pRotation = Vec3.ZERO;
        private float position = 0, pPosition = 0, size = 0, pSize = 0, opacity = 1;

        public Element(DefinedBlockState state, CycleHudElement element) {
            this.state = state;
            this.element = element;
        }

        public DefinedBlockState state() {
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
            Player player = mc.player;

            MultiBufferSource multiBufferSource = guiGraphics.bufferSource();
            PoseStack poseStack = guiGraphics.pose();

            poseStack.pushPose();

            float offsetX = (float) (guiGraphics.guiWidth() / 2) + (position() * 20);
            float offsetY = 16;

            poseStack.translate(offsetX, offsetY, 0);

            this.size = Mth.lerp(element().idle() ? 0.05f : 0.1f, this.size, element().idle() ? 0 : selected() ? 16 : 10);
            float offset = this.size * .5f;


            poseStack.last().pose().translate(-offset, -offset, 0);

            ///

            /*
            this.opacity = Mth.lerp(.1f, this.opacity, element().idle() ? 0 : 1);

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.opacity);

             */

            ///

            /*
            Vec3 angle = player.getLookAngle();
            Vec3 rot = new Vec3(Math.toDegrees(angle.x), Math.toDegrees(angle.y), Math.toDegrees(angle.z));
            Vec3 drag = rot.subtract(this.pRotation).multiply(10, 10, 10);

            this.rotation = this.rotation.lerp(drag, drag.equals(Vec3.ZERO) ? 0.01f : .05f);

            poseStack.rotateAround(Axis.YP.rotationDegrees(-(float) (this.rotation.x + this.rotation.z) + 50), offset, offset, offset);
            poseStack.rotateAround(Axis.XP.rotationDegrees(-(float) this.rotation.y + 22.5f), offset, offset, offset);
            poseStack.rotateAround(Axis.ZP.rotationDegrees(-(float) this.rotation.y + 22.5f), offset, offset, offset);
             */

            poseStack.rotateAround(Axis.YP.rotationDegrees(50), offset, offset, offset);
            poseStack.rotateAround(Axis.XP.rotationDegrees(22.5f), offset, offset, offset);
            poseStack.rotateAround(Axis.ZP.rotationDegrees(22.5f), offset, offset, offset);

            poseStack.scale(this.size, this.size, this.size);
            if(this.size != 0) mc.getBlockRenderer().renderSingleBlock(state.block().defaultBlockState(), poseStack, multiBufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);

            RenderSystem.disableBlend();

            //this.pRotation = rot;

            poseStack.popPose();
        }
    }
}
