package com.elysiasilly.babel.api.theatre.actor.registry;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.scene.registry.SceneLike;
import com.elysiasilly.babel.api.theatre.scene.registry.SceneType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DeferredActor <A extends Actor> extends DeferredHolder<ActorType<?>, ActorType<A>> implements ActorLike, ItemLike, SceneLike {

    public DeferredActor(ResourceKey<ActorType<?>> key) {
        super(key);
    }

    @Override
    public ActorType<A> actorType() {
        return get();
    }

    // TODO
    @Override
    public Item asItem() {
        return null;
    }

    @Override
    public SceneType<?, ?> sceneType() {
        return actorType().sceneType();
    }
}
