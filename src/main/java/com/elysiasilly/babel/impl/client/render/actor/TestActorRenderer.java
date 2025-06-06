package com.elysiasilly.babel.impl.client.render.actor;

import com.elysiasilly.babel.api.theatre.actor.render.ActorRenderer;
import com.elysiasilly.babel.impl.client.BBShaders;
import com.elysiasilly.babel.impl.common.actor.TestActor;
import com.elysiasilly.babel.util.UtilsRender;
import com.elysiasilly.babel.util.type.RGBA;
import com.elysiasilly.babel.util.type.UV;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public class TestActorRenderer implements ActorRenderer<TestActor> {

    public TestActorRenderer() {}

    @Override
    public void render(TestActor actor, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {

        LevelRenderer.renderVoxelShape(
                poseStack,
                multiBufferSource.getBuffer(RenderType.lines()),
                actor.collisionShape(),
                0,
                0,
                0,
                0,
                0,
                0,
                .5f,
                false
        );

        if(actor.fluid == null || actor.fluid.isEmpty()) return;

        Vec3 end =  actor.startPos.subtract(actor.endPos);;
        end = new Vec3(Math.abs(end.x), Math.abs(end.y), Math.abs(end.z));

        end = new Vec3(end.x, (end.y / actor.getVolume()) * actor.fluid.getAmount(), end.z);

        IClientFluidTypeExtensions fluidExtensions = IClientFluidTypeExtensions.of(actor.fluid.getFluidType());

        UV stillUV = new UV(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidExtensions.getStillTexture()));
        UV flowingUV = new UV(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidExtensions.getFlowingTexture()));

        UtilsRender.drawCube(
                multiBufferSource.getBuffer(BBShaders.TILING_SOLID.getType()), // TODO
                poseStack.last().pose(),
                packedLight,
                RGBA.NULL,
                UtilsRender.Cube.cubePillarUV(flowingUV, stillUV),
                Vec3.ZERO,
                end
        );
    }

    public static void temp() {
        //RGBA rgba = Conversions.Col.rgbaA(fluidExtensions.getTintColor());

        //if fluid is water, use biome water color
        //if(actor.fluid.is(Fluids.WATER)) {
        //    rgba = Conversions.Col.rgbaA(BiomeColors.getAverageWaterColor(actor.level(), Conversions.Vec.blockPos(Conversions.Vec.toMojang(actor.getPos()))));
        //}
    }
}
