package com.elysiasilly.babel.client.screen;

import com.elysiasilly.babel.util.MCUtil;
import com.elysiasilly.babel.util.resource.RGBA;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;

public class BabelScreenUtil {

    public static void drawItemWithDecor(ItemStack stack, PoseStack pose, Vec2 pos, float depth, Vec2 offset, float scale) {
        drawItem(stack, pose, pos, depth, offset, scale);
        drawItemDecor(stack, pose, pos, depth, offset, scale);
    }

    // todo : lighting for flat models is broken
    private static void drawItem(ItemStack stack, PoseStack pose, Vec2 pos, float depth, Vec2 offset, float scale) {
        if(MCUtil.Item.isEmpty(stack)) return;

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
        if(MCUtil.Item.isEmpty(stack)) return;

        pose.pushPose();


        pose.popPose();
    }

    public static void drawBlock(BlockState state, PoseStack pose, Vec2 pos, float depth, Vec2 offset, float scale) {

        pose.pushPose();

        pose.popPose();
    }

    public static void fill(WidgetBounds bounds, VertexConsumer consumer, Matrix4f matrix4f, RGBA rgba) {
        fill(consumer, matrix4f, bounds.globalStart, bounds.globalEnd, bounds.depth, rgba);
    }

    public static void fill(VertexConsumer consumer, Matrix4f matrix4f, Vec2 min, Vec2 max, float z, RGBA rgba) {
        consumer.addVertex(matrix4f, min.x, min.y, z).setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha);
        consumer.addVertex(matrix4f, min.x, max.y, z).setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha);
        consumer.addVertex(matrix4f, max.x, max.y, z).setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha);
        consumer.addVertex(matrix4f, max.x, min.y, z).setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha);
    }

    public static void blit() {}
}
