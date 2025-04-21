package com.elysiasilly.babel.api.client.screen;

import com.elysiasilly.babel.util.resource.RGBA;
import com.elysiasilly.babel.util.utils.ItemStackUtil;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class BabelScreenUtil {

    public static int OVERLAY = OverlayTexture.NO_OVERLAY, LIGHT = LightTexture.FULL_BRIGHT;


    public static void drawItemWithDecor(ItemStack stack, PoseStack pose, Vec2 pos, float depth, Vec2 offset, float scale) {
        drawItem(stack, pose, pos, depth, offset, scale);
        drawItemDecor(stack, pose, pos, depth, offset, scale);
    }

    // todo : lighting for flat models is broken
    private static void drawItem(ItemStack stack, PoseStack pose, Vec2 pos, float depth, Vec2 offset, float scale) {
        if(ItemStackUtil.isInvalid(stack)) return;

        pose.pushPose();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        BakedModel bakedmodel = itemRenderer.getModel(stack, Minecraft.getInstance().level, null, 0);

        pose.translate(pos.x + offset.x, pos.y + offset.y, depth);

        pose.scale(16.0F * scale, -16.0F * scale, 16.0F * scale);
        boolean flag = !bakedmodel.usesBlockLight();

        if(flag) Lighting.setupForFlatItems();

        itemRenderer.render(stack, ItemDisplayContext.GUI, false, pose, Minecraft.getInstance().renderBuffers().bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);

        if(flag) Lighting.setupFor3DItems();

        pose.popPose();
    }

    public static void drawItemDecor(ItemStack stack, PoseStack pose, Vec2 pos, float depth, Vec2 offset, float scale) {
        if(ItemStackUtil.isInvalid(stack)) return;

        pose.pushPose();


        pose.popPose();
    }

    /// misc

    public static void drawBlock(GuiGraphics guiGraphics, BlockState state, Vector2f pos, float z, Vector3f rot, float scale) {
        drawBlock(guiGraphics.bufferSource(), guiGraphics.pose(), state, pos, z, rot, scale);
    }

    @SuppressWarnings("deprecation")
    public static void drawBlock(MultiBufferSource bufferSource, PoseStack poseStack, BlockState state, Vector2f pos, float z, Vector3f rot, float scale) {
        poseStack.pushPose();

        float offset = scale / 2;

        poseStack.translate(pos.x - offset, pos.y - offset, z - offset);

        poseStack.rotateAround(Axis.XP.rotationDegrees(-35 + rot.x), offset, offset, offset);
        poseStack.rotateAround(Axis.YP.rotationDegrees(45  + rot.y), offset, offset, offset);
        poseStack.rotateAround(Axis.ZP.rotationDegrees(0   + rot.z), offset, offset, offset);

        poseStack.scale(scale, -scale, scale);

        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, bufferSource, LIGHT, OVERLAY);

        poseStack.popPose();
    }

    ///

    public static void fill(WidgetBounds bounds, VertexConsumer consumer, Matrix4f matrix4f, RGBA rgba) {
        fill(consumer, matrix4f, bounds.globalStart, bounds.globalEnd, bounds.depth, rgba);
    }

    public static void fill(VertexConsumer consumer, Matrix4f matrix4f, Vec2 min, Vec2 max, float z, RGBA rgba) {
        consumer.addVertex(matrix4f, min.x, min.y, z).setColor(rgba.r(), rgba.g(), rgba.b(), rgba.a());
        consumer.addVertex(matrix4f, min.x, max.y, z).setColor(rgba.r(), rgba.g(), rgba.b(), rgba.a());
        consumer.addVertex(matrix4f, max.x, max.y, z).setColor(rgba.r(), rgba.g(), rgba.b(), rgba.a());
        consumer.addVertex(matrix4f, max.x, min.y, z).setColor(rgba.r(), rgba.g(), rgba.b(), rgba.a());
    }

    public static void blit() {}
}
