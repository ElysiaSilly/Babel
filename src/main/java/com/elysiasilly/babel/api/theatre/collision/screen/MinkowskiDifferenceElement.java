package com.elysiasilly.babel.api.theatre.collision.screen;

import com.elysiasilly.babel.api.client.screen.neo.BabelElement;
import com.elysiasilly.babel.api.client.screen.neo.BabelScreen;
import com.elysiasilly.babel.api.theatre.collision.GJK;
import com.elysiasilly.babel.api.theatre.collision.MeshCollider;
import com.elysiasilly.babel.util.UtilsRender;
import com.elysiasilly.babel.util.type.RGBA;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.joml.Vector3d;

public class MinkowskiDifferenceElement extends BabelElement {

    private final PhysicsElement first, second;
    private MeshCollider minkowskiDifference = null;

    public MinkowskiDifferenceElement(BabelScreen screen, MeshCollider collider, PhysicsElement a, PhysicsElement b) {
        super(new Vector3d(screen.screenCentre(), 0), screen, collider);
        this.first = a; this.second = b;
    }

    @Override
    public void tick() {
        super.tick();

        this.minkowskiDifference = GJK.minkowskiDifference(this.first.collider(), this.second.collider());
    }

    @Override
    public void render(GuiGraphics guiGraphics, MultiBufferSource bufferSource, PoseStack poseStack) {
        if(this.minkowskiDifference == null) return;

        Vector3d[] vertices = this.minkowskiDifference.getCached();

        poseStack.pushPose();

        poseStack.translate(screenCentre().x, screenCentre().y, 0);

        for(Vector3d vertexFirst : vertices) {

            for(Vector3d vertexSecond : vertices) {
                BabelElement.drawLine(bufferSource.getBuffer(RenderType.gui()), poseStack.last().pose(), vertexFirst, vertexSecond, .5f, new RGBA(1, 1, 1, .3f));
            }

            poseStack.pushPose();

            poseStack.translate(vertexFirst.x - 1, vertexFirst.y - 1, vertexFirst.z);

            UtilsRender.drawCube(bufferSource.getBuffer(RenderType.gui()), poseStack.last().pose(), LightTexture.FULL_BRIGHT, RGBA.WHITE, UtilsRender.Cube.cube(), 2);

            poseStack.popPose();
        }

        poseStack.popPose();
    }
}
