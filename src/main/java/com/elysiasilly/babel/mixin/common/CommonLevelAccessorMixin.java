package com.elysiasilly.babel.mixin.common;

import net.minecraft.world.level.CommonLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CommonLevelAccessor.class)
public interface CommonLevelAccessorMixin {

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
