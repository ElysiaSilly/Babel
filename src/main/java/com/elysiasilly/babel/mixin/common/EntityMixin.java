package com.elysiasilly.babel.mixin.common;

import com.elysiasilly.babel.theatre.Theatre;
import com.elysiasilly.babel.theatre.scene.Scene;
import com.elysiasilly.babel.theatre.storage.LevelSceneAttachment;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(Entity.class)
public class EntityMixin {

    @WrapOperation(
            method = "collide",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntityCollisions(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;")
    )

    // todo
    private List<VoxelShape> babel$collide(Level instance, Entity entity, AABB aabb, Operation<List<VoxelShape>> original) {

        List<Scene<?>> scenes = Theatre.get(instance);

        List<VoxelShape> list = new ArrayList<>(original.call(instance, entity, aabb));

        for(Scene<?> scene : scenes) {
            list.addAll(scene.getCollisions(aabb));
        }

        return list;
    }
}
