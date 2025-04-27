package com.elysiasilly.babel.mixin.common;

import net.minecraft.world.level.CollisionGetter;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CollisionGetter.class)
public interface CollisionGetterMixin {

    /*
    @ModifyReturnValue(
            method = "getCollisions",
            at = @At("RETURN")
    )

    private Iterable<VoxelShape> babel$getEntityCollisions(Iterable<VoxelShape> original) {
        return List.of();
    }

    @ModifyReturnValue(
            method = "noCollision(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z",
            at = @At("RETURN")
    )

    private boolean babel$noCollision(boolean original) {
        return true;
    }

    @ModifyReturnValue(
            method = "noBlockCollision",
            at = @At("RETURN")
    )

    private boolean babel$noBlockCollision(boolean original) {
        return true;
    }
     */

    /*
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

     */


}
