package com.elysiasilly.babel.mixin.client;

import com.elysiasilly.babel.api.client.screen.screen.IHideElementsScreen;
import com.elysiasilly.babel.api.client.screen.screen.IModifyCameraScreen;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final Minecraft minecraft;

    @Shadow @Final private Camera mainCamera;

    @Inject(
            method = "renderItemInHand",
            at = @At("HEAD"),
            cancellable = true
    )

    private void babel$renderItemInHand(Camera camera, float partialTick, Matrix4f projectionMatrix, CallbackInfo ci) {
        if(this.minecraft.screen instanceof IHideElementsScreen screen) if(screen.hideCharacter()) ci.cancel();
    }

    @Inject(
            method = "renderLevel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;prepareCullFrustum(Lnet/minecraft/world/phys/Vec3;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V")
    )

    // TODO : find better place to mixin
    private void babel$renderLevel(DeltaTracker deltaTracker, CallbackInfo ci) {
        if(Minecraft.getInstance().screen instanceof IModifyCameraScreen screen) screen.camera(this.mainCamera);
    }
}
