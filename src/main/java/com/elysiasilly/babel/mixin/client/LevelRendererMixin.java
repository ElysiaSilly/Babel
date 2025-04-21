package com.elysiasilly.babel.mixin.client;

import com.elysiasilly.babel.api.client.IDepthRenderTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin implements IDepthRenderTarget {

    @Shadow @Final
    private Minecraft minecraft;

    @Unique
    private RenderTarget babel$depthRenderTarget;

    @Unique
    private void babel$copyDepthBuffer() {
        if(babel$depthRenderTarget == null) {
            babel$depthRenderTarget = new TextureTarget(this.minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight(), true, Minecraft.ON_OSX);
        }
        RenderTarget main = minecraft.getMainRenderTarget();
        if(babel$depthRenderTarget.width != main.width || babel$depthRenderTarget.height != main.height) {
            babel$depthRenderTarget.resize(main.width, main.height, false);
        }
        if(main.isStencilEnabled()) {
            babel$depthRenderTarget.enableStencil();
        }
        else if(babel$depthRenderTarget.isStencilEnabled()) {
            babel$depthRenderTarget.destroyBuffers();
            babel$depthRenderTarget = new TextureTarget(main.width, main.height, true, Minecraft.ON_OSX);
        }

        babel$depthRenderTarget.setClearColor(0, 0, 0, 0);
        babel$depthRenderTarget.copyDepthFrom(main);
        main.bindWrite(false);
    }

    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/PostChain;process(F)V",
                    ordinal = 1
            )
    )

    private void babel$renderLevelPreFabulous(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        babel$copyDepthBuffer();
    }

    @Override
    public RenderTarget babel$getDepthRenderTarget() {
        return babel$depthRenderTarget;
    }
}
