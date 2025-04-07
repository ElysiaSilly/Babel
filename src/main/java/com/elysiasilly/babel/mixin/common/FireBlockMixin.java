package com.elysiasilly.babel.mixin.common;

import com.elysiasilly.babel.api.common.block.api.ITransformOnBurnBlock;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(FireBlock.class)
public class FireBlockMixin {

    @Inject(
            method = "checkBurnOut",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z",
                    shift = At.Shift.AFTER
            )
    )

    // TODO : transformation occasionally fails for whatever reason

    private void babel$checkBurnOut(Level level, BlockPos pos, int chance, RandomSource random, int age, Direction face, CallbackInfo ci, @Local BlockState before) {
        if(before.getBlock() instanceof ITransformOnBurnBlock block) block.onBurn(level, pos, face, before);
    }
}