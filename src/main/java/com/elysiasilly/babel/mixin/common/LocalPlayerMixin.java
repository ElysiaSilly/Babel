package com.elysiasilly.babel.mixin.common;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    /*
    @Inject(
            method = "moveTowardsClosestSpace",
            at = @At(value = "HEAD")
    )

    private void babel$moveTowardsClosestSpace(double x, double z, CallbackInfo ci) {
        //EntityCollisionHandler.collide((LocalPlayer) (Object) this);
    }

     */
}
