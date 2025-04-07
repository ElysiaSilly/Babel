package com.elysiasilly.babel.impl.common.actor;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.ActorType;
import com.elysiasilly.babel.api.theatre.scene.SceneType;
import com.elysiasilly.babel.impl.registry.BBActors;
import com.elysiasilly.babel.impl.registry.BBScenes;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.UUID;

public class TankActor extends Actor {

    int rotation = 0;

    public TankActor(UUID uuid) {
        super(uuid);
    }

    public void setRot(int rot) {
        this.rotation = rot;
    }

    public int getRot() {
        return this.rotation;
    }

    @Override
    public void serialize(CompoundTag tag, HolderLookup.Provider registries) {
        super.serialize(tag, registries);
        tag.putInt("rot", getRot());
    }

    @Override
    public void deserialize(CompoundTag tag, HolderLookup.Provider registries) {
        super.deserialize(tag, registries);
        setRot(tag.getInt("rot"));
    }

    @Override
    public VoxelShape getCollisionBox() {
        return Block.box(-24, 0, -24, 24, 48, 24);
    }

    @Override
    public ActorType<?> actorType() {
        return BBActors.TANK.get();
    }

    @Override
    public SceneType<?, ?> sceneType() {
        return BBScenes.BUILTIN.get();
    }
}
