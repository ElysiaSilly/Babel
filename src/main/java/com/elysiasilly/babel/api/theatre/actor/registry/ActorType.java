package com.elysiasilly.babel.api.theatre.actor.registry;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.scene.registry.SceneType;
import com.elysiasilly.babel.core.BBRegistries;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3d;

import java.util.UUID;

public class ActorType<A extends Actor> {

    private final ActorFactory<? extends A> factory;
    private final boolean synced;
    private final Holder<SceneType<?, ?>> sceneType;

    public ActorType(ActorFactory<? extends A> factory, boolean synced, Holder<SceneType<?, ?>> sceneType) {
        this.factory = factory;
        this.synced = synced;
        this.sceneType = sceneType;
    }

    public ResourceLocation getKey() {
        return getKey(this);
    }

    public static ResourceLocation getKey(ActorType<?> actorType) {
        return BBRegistries.ACTOR_TYPE.get().getKey(actorType);
    }

    public A create(Vector3d pos, CompoundTag tag) {
        return this.factory.create(pos, UUID.randomUUID(), tag);
    }

    public A create(Vector3d pos, UUID uuid) {
        return this.factory.create(pos, uuid, null);
    }

    public A create(Vector3d pos) {
        return this.factory.create(pos, UUID.randomUUID(), null);
    }

    public boolean synced() {
        return this.synced;
    }

    public SceneType<?, ?> sceneType() {
        return this.sceneType.value();
    }

    public static class Builder<A extends Actor> {

        private final ActorFactory<? extends A> factory;
        private boolean sync = true;
        private final Holder<SceneType<?, ?>> sceneType;

        private Builder(ActorFactory<? extends A> factory, Holder<SceneType<?, ?>> sceneType) {
            this.factory = factory;
            this.sceneType = sceneType;
        }

        public static <A extends Actor> Builder<A> of(ActorFactory<? extends A> factory, Holder<SceneType<?, ?>> sceneType) {
            return new Builder<>(factory, sceneType);
        }

        public ActorType<A> build() {
            return new ActorType<>(this.factory, this.sync, this.sceneType);
        }

        public Builder<A> sync(boolean sync) {
            this.sync = sync; return this;
        }
    }

    @FunctionalInterface
    public interface ActorFactory<A extends Actor> {
        A create(Vector3d pos, UUID uuid, CompoundTag tag);
    }
}
