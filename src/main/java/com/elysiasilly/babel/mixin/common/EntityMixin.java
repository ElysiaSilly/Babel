package com.elysiasilly.babel.mixin.common;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class EntityMixin {


    /*
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

        ///
        //list = new ArrayList<>();
        ///

        return list;
    }


     */


    /*
    @Shadow public abstract void setPos(Vec3 pos);

    @Shadow public abstract Vec3 position();

    @ModifyReturnValue(
            method = "collectColliders",
            at = @At("RETURN")
    )

    private static List<VoxelShape> babel$collectColliders(List<VoxelShape> original) {
        return List.of();
    }

    @ModifyReturnValue(
            method = "isInWall",
            at = @At("RETURN")
    )

    private boolean babel$isInWall(boolean original) {
        return false;
    }


    @Inject(
            method = "move",
            at = @At(value = "HEAD"),
            cancellable = true
    )

    private void babel$move(MoverType type, Vec3 pos, CallbackInfo ci) {
        this.setPos(position().add(pos));
        ci.cancel();
    }

     */



}
