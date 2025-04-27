package com.elysiasilly.babel.impl.common.actor;

import com.elysiasilly.babel.api.theatre.actor.PhysicsActor;
import com.elysiasilly.babel.api.theatre.actor.registry.ActorType;
import com.elysiasilly.babel.core.registry.BBActors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3d;

import java.util.UUID;

public class FlamethrowerActor extends PhysicsActor {

    public FlamethrowerActor(Vector3d pos, UUID uuid, CompoundTag tag) {
        super(pos, uuid, tag);
    }

    @Override
    public VoxelShape collisionShape() {
        return Block.box(-6, 0, -52, 6, 16, 43);
    }

    @Override
    public VoxelShape interactionShape() {
        return Block.box(-6, 0, -52, 6, 16, 43);
    }

    @Override
    public ActorType<?> actorType() {
        return BBActors.FLAMETHROWER.get();
    }
}
