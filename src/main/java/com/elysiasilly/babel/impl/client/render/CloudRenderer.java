package com.elysiasilly.babel.impl.client.render;

import com.elysiasilly.babel.util.resource.RGBA;
import com.elysiasilly.babel.util.resource.UV;
import com.elysiasilly.babel.util.utils.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class CloudRenderer {

    static boolean init = true;

    public static void renderCloud(LevelRenderer instance, PoseStack poseStack, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Vec3 cam, VertexBuffer cloudBuffer) {

        //if(init) {
            init = false;
            cloudBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
            cloudBuffer.bind();
            cloudBuffer.upload(buildClouds(cam));
            VertexBuffer.unbind();
        //}

        float f = Minecraft.getInstance().level.effects().getCloudHeight();
        double d1 = (double)(((float) partialTick) * 0.03F);
        double d2 = (cam.x + d1) / 12.0;
        double d3 = (double)(f - (float)cam.y + 0.33F);
        double d4 = cam.z / 12.0 + 0.33F;
        d2 -= (double)(Mth.floor(d2 / 2048.0) * 2048);
        d4 -= (double)(Mth.floor(d4 / 2048.0) * 2048);
        float f3 = (float)(d2 - (double)Mth.floor(d2));
        float f4 = (float)(d3 / 4.0 - (double)Mth.floor(d3 / 4.0)) * 4.0F;
        float f5 = (float)(d4 - (double)Mth.floor(d4));

        poseStack.pushPose();
        poseStack.mulPose(frustumMatrix);
        poseStack.scale(12.0F, 1.0F, 12.0F);
        poseStack.translate(-f3, f4, -f5);

        //if(cloudBuffer != null) {
            //System.out.println("hey");

            cloudBuffer.bind();

            RenderType renderType = RenderType.solid();

            renderType.setupRenderState();
            ShaderInstance shaderinstance = RenderSystem.getShader();

            cloudBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderinstance);
            renderType.clearRenderState();
            VertexBuffer.unbind();
        //}
        poseStack.popPose();
    }

    public static MeshData buildClouds(Vec3 cam) {
        Tesselator tesselator = Tesselator.getInstance();

        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);

        drawPlane(builder, RGBA.BLACK, new Vec3(-100, -100, -100), new Vec3(100, 100, 100));

        return builder.buildOrThrow();
    }

    public static void drawPlane(BufferBuilder builder, RGBA rgba, Vec3 start, Vec3 end) {
        drawPlane(builder, rgba, start, end, new Vec3(end.x, end.y, start.z), new Vec3(start.x, start.y, end.z));
    }

    public static void drawPlane(BufferBuilder builder, RGBA rgba, Vec3 a, Vec3 b, Vec3 c, Vec3 d) {
        builder.addVertex((float) c.x, (float) c.y, (float) c.z)
                .setUv(0, 0)
                .setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha)
                .setNormal(0, 0, 0);

        builder.addVertex((float) a.x, (float) a.y, (float) a.z)
                .setUv(0, 0)
                .setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha)
                .setNormal(0, 0, 0);

        builder.addVertex((float) d.x, (float) d.y, (float) d.z)
                .setUv(0, 0)
                .setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha)
                .setNormal(0, 0, 0);

        builder.addVertex((float) b.x, (float) b.y, (float) b.z)
                .setUv(0, 0)
                .setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha)
                .setNormal(0, 0, 0);
    }
}
