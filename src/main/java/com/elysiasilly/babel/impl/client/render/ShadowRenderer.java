package com.elysiasilly.babel.impl.client.render;

import com.elysiasilly.babel.impl.client.BBShaders;
import com.elysiasilly.babel.impl.client.shader.EntityShadowShader;
import com.elysiasilly.babel.util.UtilsMath;
import com.elysiasilly.babel.util.UtilsRender;
import com.elysiasilly.babel.util.UtilsShader;
import com.elysiasilly.babel.util.type.RGBA;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ShadowRenderer {

    // TODO move this into its own mod


    // future config stuff
    public static boolean customShadowRadius = true;


    public static EntityShadowShader shader() {
        return BBShaders.ENTITY_SHADOW;
    }

    public static ShaderInstance instance() {
        return shader().getInstance();
    }

    public static PoseStack initPoseStack() {
        PoseStack poseStack = new PoseStack();
        Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        poseStack.translate(-cam.x, -cam.y, -cam.z);
        return poseStack;
    }

    // ignore how fucked up this is ill clean it up later
    public static void renderShadow(MultiBufferSource bufferSource, Entity entity, float shadowStrengthMaybe, float partialTicks, LevelReader levelReader, float originalRadius) {

        // create a shadow radius from the entity's bounding box
        AABB bounds = entity.getBoundingBox().inflate(.05);
        double radius = customShadowRadius ? (((bounds.maxX - bounds.minX) / 2) + ((bounds.maxZ - bounds.minZ) / 2)) / 2 : originalRadius;

        int rad = 0;

        BlockPos entityBlockPos = entity.blockPosition();

        //System.out.println(LightTexture.sky(LevelRenderer.getLightColor(levelReader, entityBlockPos)));

        int blockLight = levelReader.getBrightness(LightLayer.BLOCK, entityBlockPos);
        int blockLightAbove = levelReader.getBrightness(LightLayer.BLOCK, entityBlockPos.above());

        radius = blockLight < blockLightAbove ? radius + (blockLight * 0.01) : radius - (blockLight * 0.01);

        Vec3 entityPos = entity.position();

        PoseStack poseStack = initPoseStack();

        poseStack.pushPose();

        int depth = (int) (3 + (radius * 4));

        for(int x = -rad; x <= rad; x++) {
            for(int z = -rad; z <= rad; z++) {
                for(int y = 0; y <= depth; y++) {

                    BlockPos blockPos = entity.getOnPos().offset(x, -y, z);
                    BlockState state = levelReader.getBlockState(blockPos);

                    if(state.getRenderShape() != RenderShape.INVISIBLE || !state.isSolidRender(levelReader, blockPos)) {

                        poseStack.pushPose();

                        poseStack.translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());

                        boolean breakLoop = false;

                        for(AABB aabb : state.getShape(levelReader, blockPos).toAabbs()) {
                            UtilsRender.drawPlane(bufferSource.getBuffer(shader().getType()), poseStack.last().pose(), 0, RGBA.BLACK, new Vec3(0, aabb.maxY, 0), new Vec3(1, aabb.maxY, 1));

                            if(!breakLoop) if(aabb.minX + aabb.minZ <= 0 && aabb.maxX + aabb.maxZ >= 1) breakLoop = true;

                            double distanceY = (entityPos.y - aabb.maxY) - (blockPos.getY());

                            UtilsShader.setUniform(instance(), "Mask", aabb.minZ, aabb.minX, aabb.maxZ, aabb.maxX);
                            UtilsShader.setUniform(instance(), "Radius", radius + (distanceY * .05));
                            UtilsShader.setUniform(instance(), "Opacity", (radius * 0.5) - (distanceY * .05));
                        }

                        UtilsShader.setUniform(instance(), "Rotation", -Math.toRadians(entity.getVisualRotationYInDegrees() % 360));

                        double offsetY = UtilsMath.castToRange(0, 1, 1, 0, entityPos.x - blockPos.getX());
                        double offsetZ = entityPos.z - blockPos.getZ();
                        UtilsShader.setUniform(instance(), "Offset", offsetZ, offsetY);

                        poseStack.popPose();

                        if(breakLoop) break;
                    }
                }
            }
        }
    }
}
