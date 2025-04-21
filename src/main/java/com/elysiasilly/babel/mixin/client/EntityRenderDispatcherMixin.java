package com.elysiasilly.babel.mixin.client;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    /*
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

     */
}
