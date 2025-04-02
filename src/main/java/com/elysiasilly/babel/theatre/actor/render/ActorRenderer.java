package com.elysiasilly.babel.theatre.actor.render;

import com.elysiasilly.babel.theatre.actor.Actor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

// wip
public interface ActorRenderer<A extends Actor<?>> {

    /// future plans:
    /// instanced rendering :c
    /// babel model loader

    void render(A actor, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight);

    default boolean shouldRenderOffScreen(A actor) {
        return false;
    }

    default int getViewDistance() {
        return 64;
    }

    default boolean shouldRender(A actor, Vec3 cameraPos) {
        return true; // todo
    }

    default AABB getRenderBoundingBox(A actor){
        return new AABB(0, 0, 0, 0, 0, 0); // todo
    }
}
