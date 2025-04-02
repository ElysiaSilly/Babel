package com.elysiasilly.babel.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT) @SuppressWarnings("all")
public abstract class BabelBERenderer<B extends BlockEntity> implements BlockEntityRenderer<B> {

    public final BlockEntityRendererProvider.Context context;

    public BabelBERenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public abstract void render(B be, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay);

    @Override
    public boolean shouldRender(B blockEntity, Vec3 cameraPos) {
        return BlockEntityRenderer.super.shouldRender(blockEntity, cameraPos);
    }

    @Override
    public boolean shouldRenderOffScreen(B blockEntity) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(blockEntity);
    }

    @Override
    public AABB getRenderBoundingBox(B blockEntity) {
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
    }
}
