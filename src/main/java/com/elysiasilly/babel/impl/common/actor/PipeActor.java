package com.elysiasilly.babel.impl.common.actor;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.ActorType;
import com.elysiasilly.babel.api.theatre.scene.SceneType;
import com.elysiasilly.babel.core.registry.BBActors;
import com.elysiasilly.babel.core.registry.BBScenes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.UUID;

public class PipeActor extends Actor {

    public PipeActor(UUID uuid) {
        super(uuid);
    }

    @Override
    public VoxelShape getCollisionBox() {
        return Block.box(0, 0, 0, 1, 1, 1);
    }

    @Override
    public ActorType<?> actorType() {
        return BBActors.PIPE.get();
    }

    @Override
    public SceneType<?, ?> sceneType() {
        return BBScenes.BUILTIN.get();
    }
}
