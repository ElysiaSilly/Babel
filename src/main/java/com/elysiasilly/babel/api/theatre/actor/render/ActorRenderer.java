package com.elysiasilly.babel.api.theatre.actor.render;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.util.UtilsFormatting;
import com.elysiasilly.babel.util.type.RGBA;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;

// wip
public interface ActorRenderer<A extends Actor> {

    /// future plans:
    /// instanced rendering :c
    /// babel model loader

    default void renderInternal(A actor, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        render(actor.self(), partialTick, poseStack,multiBufferSource, packedLight);
        renderDebug(actor.self(), partialTick, poseStack,multiBufferSource, packedLight);
    }



    default void renderDebug(A actor, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        poseStack.pushPose();

        if(Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {

            Player player = Minecraft.getInstance().player;

            if(player != null) {

                AABB aabb = actor.collisionShape().bounds();

                LevelRenderer.renderLineBox(
                        poseStack,
                        bufferSource.getBuffer(RenderType.lines()),
                        aabb.minX,
                        aabb.minY,
                        aabb.minZ,
                        aabb.maxX,
                        aabb.maxY,
                        aabb.maxZ,
                        1.0F,
                        1.0F,
                        1.0F,
                        1.0F
                );

                Vec3 playerPos = player.getEyePosition();
                Vector3d pos = actor.pos();

                double angle = 180 / Math.PI * Math.atan2(pos.z - playerPos.z, pos.x - playerPos.x) - 90;

                poseStack.mulPose(Axis.YP.rotationDegrees((float) -angle));

                drawText(actor, poseStack, bufferSource);
            }
        }

        poseStack.popPose();
    }

    private void drawText(A actor, PoseStack poseStack, MultiBufferSource bufferSource) {
        poseStack.pushPose();

        List<Component> components = gatherDebugText(actor);

        int index = components.size();
        for(Component component : components) {

            poseStack.pushPose();

            String text = component.getString();


            float scale = .5f;
            poseStack.scale(((scale / 16) / width(text)) * width(text), ((scale / 16) / height()) * height(), -(scale / 16));

            poseStack.translate(0, 8f + (float) (actor.collisionShape().bounds().maxY * (10 * index) + 40), 0);

            poseStack.mulPose(Axis.ZP.rotationDegrees(180));

            poseStack.translate(-width(text) / 2f, (-height() / 2f) + .5, .001);

            Minecraft.getInstance().font.drawInBatch(
                    text,
                    0, 0,
                    RGBA.WHITE.abgr(),
                    true,
                    poseStack.last().pose(), bufferSource,
                    Font.DisplayMode.NORMAL,
                    0, LightTexture.FULL_BRIGHT
            );

            poseStack.popPose();

            index--;
        }

        poseStack.popPose();
    }

    private static int width(String string) {
        return Minecraft.getInstance().font.width(string);
    }

    private static int height() {
        return Minecraft.getInstance().font.lineHeight;
    }

    void render(A actor, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight);

    default List<Component> gatherDebugText(A actor) {
        List<Component> components = new ArrayList<>();

        components.add(Component.literal(actor.uuid().toString()));
        components.add(Component.literal(UtilsFormatting.vector3d(actor.pos())));

        return components;
    }


    // TODO

    default boolean shouldRenderOffScreen(A actor) {
        return false;
    }

    default int getViewDistance() {
        return 64;
    }

    default boolean shouldRender(A actor, Vec3 cameraPos) {
        return true;
    }

    default AABB getRenderBoundingBox(A actor){
        return null;
    }
}
