package com.elysiasilly.babel.util;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.model.geometry.StandaloneGeometryBakingContext;
import net.neoforged.neoforge.client.model.obj.ObjLoader;
import net.neoforged.neoforge.client.model.obj.ObjModel;
import net.neoforged.neoforge.client.model.renderable.CompositeRenderable;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RenderUtil {

    public static void drawCube(VertexConsumer consumer, Matrix4f matrix4f, float size, int packedLight, boolean centred, boolean cull, RGBA rgba) {
        // up
        drawPlane(consumer, matrix4f, packedLight, rgba, new Vec3(0, size, 0), new Vec3(size, size, size), new Vec3(size, size, 0), new Vec3(0, size, size));

        rgba.alpha = ((int) (rgba.alpha * .8));

        // down
        drawPlane(consumer, matrix4f, packedLight, rgba, new Vec3(0, 0, size), new Vec3(size, 0, 0), new Vec3(size, 0, size), new Vec3(0, 0, 0));
        rgba.alpha = ((int) (rgba.alpha * .8));

        // north
        drawPlane(consumer, matrix4f, packedLight, rgba, new Vec3(0, 0, 0), new Vec3(size, size, 0), new Vec3(size, 0, 0), new Vec3(0, size, 0));
        rgba.alpha = ((int) (rgba.alpha * .8));

        // south
        drawPlane(consumer, matrix4f, packedLight, rgba, new Vec3(0, size, size), new Vec3(size, 0, size), new Vec3(size, size, size), new Vec3(0, 0, size));
        rgba.alpha = ((int) (rgba.alpha * .8));

        // east
        drawPlane(consumer, matrix4f, packedLight, rgba, new Vec3(size, 0, 0), new Vec3(size, size, size), new Vec3(size, 0, size), new Vec3(size, size, 0));
        rgba.alpha = ((int) (rgba.alpha * .8));

        // west
        drawPlane(consumer, matrix4f, packedLight, rgba, new Vec3(0, size, 0), new Vec3(0, 0, size), new Vec3(0, size, size), new Vec3(0, 0, 0));
    }

    public static void drawLine(MultiBufferSource multiBufferSource, PoseStack.Pose stack, RGBA rgba, Vec3 start, Vec3 end) {
        VertexConsumer consumer = multiBufferSource.getBuffer(RenderType.debugLineStrip(50));

        Vector3f i = new Vector3f((float) start.x, (float) start.y, (float) start.z);

        Vector3f f = new Vector3f((float) end.x, (float) end.y, (float) end.z);

        Vector3f normal = i.sub(f).normalize();

        consumer.addVertex(stack, (float) start.x, (float) start.y, (float) start.z).setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha).setNormal(stack, normal.x, normal.y, normal.z);
        consumer.addVertex(stack, (float) end.x, (float) end.y, (float) end.z).setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha).setNormal(stack, normal.x, normal.y, normal.z);
    }

    public static void drawLineThatIsActuallyARectangle(VertexConsumer consumer, Matrix4f matrix4f, Vec3 start, Vec3 end, float girth, RGBA rgba) {

        Vec2 vector = new Vec2((float) (start.x - end.x), (float) (start.y - end.y));
        Vec2 perpendicular = new Vec2((vector.y), -vector.x);
        double length = Math.sqrt(perpendicular.x * perpendicular.x + perpendicular.y * perpendicular.y);
        Vec2 normalize = new Vec2((float) (perpendicular.x / length), (float) (perpendicular.y / length));

        drawPlane(
                consumer, matrix4f, 100, rgba,
                new Vec3(start.x + normalize.x * girth / 2, start.y + normalize.y * girth / 2, 0),
                new Vec3(end.x - normalize.x * girth / 2, end.y - normalize.y * girth / 2, 0),
                new Vec3(start.x - normalize.x * girth / 2, start.y - normalize.y * girth / 2, 0),
                new Vec3(end.x + normalize.x * girth / 2, end.y + normalize.y * girth / 2, 0)
        );
    }

    public static void drawOutlineRectangle(VertexConsumer consumer, Matrix4f matrix4f, Vec3 start, Vec3 end, float girth, RGBA rgba) {
        Vec3 topRight = new Vec3(start.x, start.y, 0);
        Vec3 topLeft = new Vec3(start.x, end.y, 0);
        Vec3 bottomRight = new Vec3(end.x, start.y, 0);
        Vec3 bottomLeft = new Vec3(end.x, end.y, 0);

        RenderUtil.drawLineThatIsActuallyARectangle(consumer, matrix4f, topRight, topLeft, girth, rgba);
        RenderUtil.drawLineThatIsActuallyARectangle(consumer, matrix4f, topLeft, bottomLeft, girth, rgba);
        RenderUtil.drawLineThatIsActuallyARectangle(consumer, matrix4f, bottomLeft, bottomRight, girth, rgba);
        RenderUtil.drawLineThatIsActuallyARectangle(consumer, matrix4f, bottomRight, topRight, girth, rgba);
    }

    public static void drawPlane(VertexConsumer consumer, Matrix4f matrix4f, int packedLight, RGBA rgba, Vec3 start, Vec3 end, Vec3 offset, boolean centred) {

        //startX = centred ? startX / 2 : startX;
        //startY = centred ? startY / 2 : startY;
        //startZ = centred ? startZ / 2 : startZ;

        //endX = centred ? endX / 2 : endX;
        //endY = centred ? endY / 2 : endY;
        //endZ = centred ? endZ / 2 : endZ;

        // TODO : STUPID ^

        start.add(offset);
        end.add(offset);

        drawPlane(consumer, matrix4f, packedLight, rgba, start, end, new Vec3(end.x, end.y, start.z), new Vec3(start.x, start.y, end.z));
    }

    /*
    public static void renderPlaneWithTextureDimensions(
            VertexConsumer consumer, Matrix4f matrix4f, int packedLight, int translucency, ResourceLocation resourceLocation,
            float offsetX, float offsetY, float offsetZ,
            boolean centred
    ) {

        NativeImage image;

        //try { image = ImageUtils.returnNativeImage(resourceLocation);
        //} catch (IOException e) { throw new RuntimeException(e);}

        int height = 0; //ImageUtils.getHeight(image); // TODO : fix
        int width = 0; //ImageUtils.getWidth(image);

        renderPlane(consumer, matrix4f, packedLight, translucency, 0, 0, 0, (float) width / 16, 0, (float) height / 16, offsetX, offsetY, offsetZ, centred);
    }
     */

    private static void drawPlane(VertexConsumer consumer, Matrix4f matrix4f, int packedLight, RGBA rgba, Vec3 a, Vec3 b, Vec3 c, Vec3 d) {

        consumer.addVertex(matrix4f, (float) c.x, (float) c.y, (float) c.z)
                .setUv(1, 0)
                .setLight(packedLight)
                .setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha)
                .setUv1(0, 0)
                .setUv2(1, 1)
                .setNormal(0, 1, 0);

        consumer.addVertex(matrix4f, (float) a.x, (float) a.y, (float) a.z)
                .setUv(0, 0)
                .setLight(packedLight)
                .setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha)
                .setUv1(0, 0)
                .setUv2(1, 1)
                .setNormal(0, 1, 0);

        consumer.addVertex(matrix4f, (float) d.x, (float) d.y, (float) d.z)
                .setUv(0, 1)
                .setLight(packedLight)
                .setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha)
                .setUv1(0, 0)
                .setUv2(1, 1)
                .setNormal(0, 1, 0);

        consumer.addVertex(matrix4f, (float) b.x,   (float) b.y,   (float) b.z)
                .setUv(1, 1)
                .setLight(packedLight)
                .setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha)
                .setUv1(0, 0)
                .setUv2(0, 0)
                .setNormal(0, 1, 0);
    }

    public static void setUniform(ShaderInstance instance, String name, Object value) {
        Uniform uniform = instance.getUniform(name);
        if(uniform == null) return;

        switch(value) {
            case Float f -> uniform.set(f);
            case Integer i -> uniform.set(i);
            default -> throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    public static boolean stage(RenderLevelStageEvent event, RenderLevelStageEvent.Stage stage) {
        return event.getStage() != stage;
    }

    public static class ObjRenderer {
        private final CompositeRenderable MODEL;

        public ObjRenderer(ResourceLocation modelLocation, boolean automaticCulling, boolean shadeQuads, boolean flipV, boolean emissiveAmbient, @Nullable String mtlOverride) {
            modelLocation = ResourceLocation.parse(String.format("%s:models/%s.obj", modelLocation.getNamespace(), modelLocation.getPath()));

            ObjModel model = ObjLoader.INSTANCE.loadModel(new ObjModel.ModelSettings(modelLocation, automaticCulling, shadeQuads, flipV, emissiveAmbient, mtlOverride));
            this.MODEL = model.bakeRenderable(StandaloneGeometryBakingContext.create(modelLocation));
        }

        public void render(PoseStack poseStack, MultiBufferSource bufferSource, RenderType renderType, int packedLight) {
            MODEL.render(poseStack, bufferSource, t -> renderType, packedLight, OverlayTexture.NO_OVERLAY, 0, CompositeRenderable.Transforms.EMPTY);
        }
    }
}
