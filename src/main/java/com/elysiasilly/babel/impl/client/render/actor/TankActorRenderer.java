package com.elysiasilly.babel.impl.client.render.actor;

import com.elysiasilly.babel.api.theatre.actor.render.ActorRenderer;
import com.elysiasilly.babel.impl.common.actor.TankActor;
import com.elysiasilly.babel.util.resource.RGBA;
import com.elysiasilly.babel.util.utils.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class TankActorRenderer implements ActorRenderer<TankActor> {

    @Override
    public void render(TankActor actor, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        poseStack.mulPose(Axis.YP.rotationDegrees(actor.getRot()));

        RenderUtil.drawVoxelShape(multiBufferSource, poseStack, actor.getCollisionBox(), false);
    }
}
