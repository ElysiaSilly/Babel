package com.elysiasilly.babel.mixin.client;

import com.elysiasilly.babel.api.client.IRenderTargets;
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
public class LevelRendererMixin implements IRenderTargets {

    @Shadow @Final
    private Minecraft minecraft;

    @Unique
    private RenderTarget babel$depthRenderTarget, babel$mainRenderTarget;

    @Unique
    private void babel$copyDepthBuffer() {
        if(this.babel$depthRenderTarget == null) {
            this.babel$depthRenderTarget = new TextureTarget(this.minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight(), true, Minecraft.ON_OSX);
        }
        RenderTarget main = minecraft.getMainRenderTarget();
        if(this.babel$depthRenderTarget.width != main.width || babel$depthRenderTarget.height != main.height) {
            this.babel$depthRenderTarget.resize(main.width, main.height, false);
        }
        if(main.isStencilEnabled()) {
            this.babel$depthRenderTarget.enableStencil();
        }
        else if(this.babel$depthRenderTarget.isStencilEnabled()) {
            this.babel$depthRenderTarget.destroyBuffers();
            this.babel$depthRenderTarget = new TextureTarget(main.width, main.height, true, Minecraft.ON_OSX);
        }

        this.babel$depthRenderTarget.setClearColor(0, 0, 0, 0);
        this.babel$depthRenderTarget.copyDepthFrom(main);
        main.bindWrite(false);
    }

    @Unique
    private void babel$copyMainBuffer() {
        if(this.babel$mainRenderTarget == null) {
            this.babel$mainRenderTarget = new TextureTarget(this.minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight(), true, Minecraft.ON_OSX);
        }
        RenderTarget main = minecraft.getMainRenderTarget();
        if(this.babel$mainRenderTarget.width != main.width || babel$mainRenderTarget.height != main.height) {
            this.babel$mainRenderTarget.resize(main.width, main.height, false);
        }
        if(main.isStencilEnabled()) {
            this.babel$mainRenderTarget.enableStencil();
        }
        else if(this.babel$mainRenderTarget.isStencilEnabled()) {
            this.babel$mainRenderTarget.destroyBuffers();
            this.babel$mainRenderTarget = new TextureTarget(main.width, main.height, true, Minecraft.ON_OSX);
        }

        this.babel$mainRenderTarget.setClearColor(0, 0, 0, 0);
        this.babel$mainRenderTarget = main;
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
        babel$copyMainBuffer();
    }

    @Override
    public RenderTarget babel$getDepthRenderTarget() {
        return this.babel$depthRenderTarget;
    }

    @Override
    public RenderTarget babel$getMainRenderTarget() {
        return this.babel$mainRenderTarget;
    }
}
