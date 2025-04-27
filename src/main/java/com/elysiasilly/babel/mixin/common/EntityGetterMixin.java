package com.elysiasilly.babel.mixin.common;

import net.minecraft.world.level.EntityGetter;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityGetter.class)
public interface EntityGetterMixin {

    /*
    @ModifyReturnValue(
            method = "getEntityCollisions",
            at = @At("RETURN")
    )

    private List<VoxelShape> babel$getEntityCollisions(List<VoxelShape> original) {
        return List.of();
    }

     */
}
