package com.elysiasilly.babel.api.dbi.hud;

import com.elysiasilly.babel.api.client.screen.BabelScreenUtil;
import com.elysiasilly.babel.api.dbi.DynamicBlockItemComponent;
import com.elysiasilly.babel.api.dbi.DynamicBlockState;
import com.elysiasilly.babel.core.registry.BBComponents;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class DBIHudRenderer implements LayeredDraw.Layer {

    public static final DBIHudRenderer INSTANCE = new DBIHudRenderer();

    private final List<Element> elements = new ArrayList<>();
    private ItemStack stack;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();

        if(mc.player != null) {
            ItemStack stack = mc.player.getMainHandItem();
            if(stack.has(BBComponents.DYNAMIC_BLOCK_ITEM)) {

                if(stack != stack()) {
                    stack(stack);
                    elements().clear();

                    DynamicBlockItemComponent component = component();

                    int index = 0;
                    for(DynamicBlockState state : component.entries()) {
                        elements().add(new Element(this, index));
                        index++;
                    }
                }

                renderInternal(guiGraphics, deltaTracker);
            } else if(stack() != null) {
                stack(null);
                elements().clear();
            }
        }
    }

    public void renderInternal(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        for(Element element : elements()) {
            guiGraphics.pose().pushPose();
            element.render(guiGraphics, Minecraft.getInstance(), guiGraphics.bufferSource(), guiGraphics.pose(), deltaTracker);
            guiGraphics.pose();
        }

    }

    public DynamicBlockItemComponent component() {
        return stack().get(BBComponents.DYNAMIC_BLOCK_ITEM);
    }

    public List<Element> elements() {
        return this.elements;
    }

    public void stack(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack stack() {
        return this.stack;
    }

    public static class Element {

        private final DBIHudRenderer parent;
        private final int index;

        private Vector2f pos;

        public Element(DBIHudRenderer parent, int index) {
            this.parent = parent;
            this.index = index;
        }


        public void render(GuiGraphics guiGraphics, Minecraft mc, MultiBufferSource multiBufferSource, PoseStack poseStack, DeltaTracker deltaTracker) {

            float f = deltaTracker.getGameTimeDeltaPartialTick(false) * Minecraft.getInstance().player.tickCount;


            float angleX = 0.1f * (Minecraft.getInstance().player.tickCount + deltaTracker.getGameTimeDeltaPartialTick(false)) * 90;

            Vector2f pos = new Vector2f((guiGraphics.guiWidth() / 2f) + ((index - parent.component().index()) * 24), (guiGraphics.guiHeight() / 2f) + 30);

            if(this.pos == null) this.pos = pos;

            //System.out.println(angleX);

            this.pos.lerp(pos, .2f);

            Vector3f rot = new Vector3f(0, 0, 0);

            BabelScreenUtil.drawBlock(guiGraphics, parent.component().entries().get(index).state(), this.pos, 200, rot, 10);

        }
    }
}
