package com.elysiasilly.babel.mixin.client;

import com.elysiasilly.babel.impl.client.render.ShadowRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;renderShadow(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/Entity;FFLnet/minecraft/world/level/LevelReader;F)V"
            )
    )

    public void babel$render(PoseStack poseStack, MultiBufferSource bufferSource, Entity entity, float shadowStrengthMaybe, float partialTicks, LevelReader levelReader, float shadowRadius) {
        ShadowRenderer.renderShadow(bufferSource, entity, shadowStrengthMaybe, partialTicks, levelReader, shadowRadius);
    }
}
