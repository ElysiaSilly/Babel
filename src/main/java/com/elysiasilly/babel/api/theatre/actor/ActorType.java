package com.elysiasilly.babel.api.theatre.actor;

import com.elysiasilly.babel.core.BBRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class ActorType<A extends Actor> {

    private final ActorFactory<? extends A> factory;
    private final boolean synced;

    public ActorType(ActorFactory<? extends A> factory, boolean synced) {
        this.factory = factory;
        this.synced = synced;
    }

    public ResourceLocation getKey() {
        return getKey(this);
    }

    public static ResourceLocation getKey(ActorType<?> actorType) {
        return BBRegistries.ACTOR_TYPE.getKey(actorType);
    }

    public A create(UUID uuid) {
        return this.factory.create(uuid);
    }

    public A create() {
        return this.factory.create(UUID.randomUUID());
    }

    public boolean synced() {
        return this.synced;
    }

    public static class Builder<A extends Actor> {

        private final ActorFactory<? extends A> factory;
        boolean sync = true;

        private Builder(ActorFactory<? extends A> factory) {
            this.factory = factory;
        }

        public static <A extends Actor> Builder<A> of(ActorFactory<? extends A> factory) {
            return new Builder<>(factory);
        }

        public ActorType<A> build() {
            return new ActorType<>(this.factory, this.sync);
        }

        public Builder<A> sync(boolean sync) {
            this.sync = sync; return this;
        }
    }

    @FunctionalInterface
    public interface ActorFactory<A extends Actor> {
        A create(UUID uuid);
    }
}
