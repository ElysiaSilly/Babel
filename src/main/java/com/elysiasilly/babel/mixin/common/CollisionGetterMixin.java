package com.elysiasilly.babel.mixin.common;

import com.elysiasilly.babel.api.theatre.Theatre;
import com.elysiasilly.babel.api.theatre.scene.Scene;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(CollisionGetter.class)
public interface CollisionGetterMixin {

    @WrapOperation(
            method = "getCollisions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/CollisionGetter;getEntityCollisions(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;")
    )

    private List<VoxelShape> babel$getCollisions(CollisionGetter instance, Entity entity, AABB aabb, Operation<List<VoxelShape>> original) {

        List<Scene<?>> scenes = Theatre.get((Level) this);

        List<VoxelShape> list = new ArrayList<>(original.call(instance, entity, aabb));

        for(Scene<?> scene : scenes) {
            list.addAll(scene.getCollisions(aabb));
        }

        ///
        //list = new ArrayList<>();
        ///

        return list;
    }

    @Inject(
            method = "noCollision(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z",
            at = @At( value = "HEAD" ),
            cancellable = true
    )

    private void babel$noCollision(Entity entity, AABB collisionBox, CallbackInfoReturnable<Boolean> cir) {

        ///
        //cir.setReturnValue(true);
        ///

        List<Scene<?>> scenes = Theatre.get((Level) this);

        List<VoxelShape> list = new ArrayList<>();

        for(Scene<?> scene : scenes) {
            list.addAll(scene.getCollisions(collisionBox));
        }

        for(VoxelShape shape : list) {
            if(!shape.isEmpty()) cir.setReturnValue(false);
        }
    }
}
