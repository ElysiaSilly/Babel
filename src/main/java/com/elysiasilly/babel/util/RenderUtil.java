package com.elysiasilly.babel.util;

import com.elysiasilly.babel.util.resource.RGBA;
import com.elysiasilly.babel.util.resource.UV;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RenderUtil {

    /// cubes

    public record Cube(boolean up, boolean down, boolean east, boolean west, boolean north, boolean south) {}

    public static final Cube FULL_CUBE = new Cube(true, true, true, true, true, true);

    //

    public static void drawCube(VertexConsumer consumer, Matrix4f matrix4f, int packedLight, RGBA rgba, Cube sides, Vec3 start, Vec3 end) {
        drawCube(consumer, matrix4f, packedLight, rgba, sides, start, end, new UV());
    }

    public static void drawCubeSideUV(VertexConsumer consumer, Matrix4f matrix4f, int packedLight, RGBA rgba, Cube sides, Vec3 start, Vec3 end, UV sideUV, UV endUV) {

    }

    public static void drawCube(VertexConsumer consumer, Matrix4f matrix4f, int packedLight, RGBA rgba, Cube sides, Vec3 start, Vec3 end, UV uv) {
        if(sides.up) {
            drawPlane(consumer, matrix4f, packedLight, rgba,
                    new Vec3(start.x, end.y, start.z),
                    new Vec3(end.x, end.y, end.z),
                    new Vec3(end.x, end.y, start.z),
                    new Vec3(start.x, end.y, end.z),
                    uv
            );
        }

        if(sides.down) {
            drawPlane(consumer, matrix4f, packedLight, rgba,
                    new Vec3(start.x, start.y, end.z),
                    new Vec3(end.x, start.y, start.z),
                    new Vec3(end.x, start.y, end.z),
                    new Vec3(start.x, start.y, start.z),
                    uv
            );
        }

        if(sides.north) {
            drawPlane(consumer, matrix4f, packedLight, rgba,
                    new Vec3(start.x, start.y, start.z),
                    new Vec3(end.x, end.y, start.z),
                    new Vec3(end.x, start.y, start.z),
                    new Vec3(start.x, end.y, start.z),
                    uv
            );
        }

        if(sides.south) {
            drawPlane(consumer, matrix4f, packedLight, rgba,
                    new Vec3(start.x, end.y, end.z),
                    new Vec3(end.x, start.y, end.z),
                    new Vec3(end.x, end.y, end.z),
                    new Vec3(start.x, start.y, end.z),
                    uv
            );
        }

        if(sides.east) {
            drawPlane(consumer, matrix4f, packedLight, rgba,
                    new Vec3(end.x, start.y, start.z),
                    new Vec3(end.x, end.y, end.z),
                    new Vec3(end.x, start.y, end.z),
                    new Vec3(end.x, end.y, start.z),
                    uv
            );
        }

        if(sides.west) {
            drawPlane(consumer, matrix4f, packedLight, rgba,
                    new Vec3(start.x, end.y, start.z),
                    new Vec3(start.x, start.y, end.z),
                    new Vec3(start.x, end.y, end.z),
                    new Vec3(start.x, start.y, start.z),
                    uv
            );
        }
    }

    public static void drawCube(VertexConsumer consumer, Matrix4f matrix4f, int packedLight, RGBA rgba, Cube sides, float size) {
        drawCube(consumer, matrix4f, packedLight, rgba, sides, new Vec3(0, 0, 0), new Vec3(size, size, size));
    }

    /// lines

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

    /// planes

    public static void drawPlane(VertexConsumer consumer, Matrix4f matrix4f, int packedLight, RGBA rgba, Vec3 start, Vec3 end, UV uv) {
        drawPlane(consumer, matrix4f, packedLight, rgba, start, end, new Vec3(end.x, end.y, start.z), new Vec3(start.x, start.y, end.z), uv);
    }

    public static void drawPlane(VertexConsumer consumer, Matrix4f matrix4f, int packedLight, RGBA rgba, Vec3 start, Vec3 end) {
        drawPlane(consumer, matrix4f, packedLight, rgba, start, end, new Vec3(end.x, end.y, start.z), new Vec3(start.x, start.y, end.z), new UV());
    }

    public static void drawPlane(VertexConsumer consumer, Matrix4f matrix4f, int packedLight, RGBA rgba, Vec3 a, Vec3 b, Vec3 c, Vec3 d) {
        drawPlane(consumer, matrix4f, packedLight, rgba, a, b, c, d, new UV());
    }

    public static void drawPlane(VertexConsumer consumer, Matrix4f matrix4f, int packedLight, RGBA rgba, Vec3 a, Vec3 b, Vec3 c, Vec3 d, UV uv) {
        consumer.addVertex(matrix4f, (float) c.x, (float) c.y, (float) c.z)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv(uv.startU(), uv.startV())
                .setLight(packedLight)
                .setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha)
                .setUv1(0, 0) // ?
                .setUv2(1, 1) // ?
                .setNormal(0, 1, 0); // ?

        consumer.addVertex(matrix4f, (float) a.x, (float) a.y, (float) a.z)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv(uv.startU(), uv.endV())
                .setLight(packedLight)
                .setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha)
                .setUv1(0, 0) // ?
                .setUv2(1, 1) // ?
                .setNormal(0, 1, 0); // ?

        consumer.addVertex(matrix4f, (float) d.x, (float) d.y, (float) d.z)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv(uv.endU(), uv.endV())
                .setLight(packedLight)
                .setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha)
                .setUv1(0, 0) // ?
                .setUv2(1, 1) // ?
                .setNormal(0, 1, 0); // ?

        consumer.addVertex(matrix4f, (float) b.x,   (float) b.y,   (float) b.z)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv(uv.endU(), uv.startV())
                .setLight(packedLight)
                .setColor(rgba.red, rgba.green, rgba.blue, rgba.alpha)
                .setUv1(0, 0) // ?
                .setUv2(0, 0) // ?
                .setNormal(0, 1, 0); // ?
    }
}
