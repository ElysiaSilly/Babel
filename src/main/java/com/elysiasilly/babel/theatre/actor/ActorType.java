package com.elysiasilly.babel.theatre.actor;

import com.elysiasilly.babel.core.registry.BabelRegistries;
import net.minecraft.resources.ResourceLocation;

public class ActorType<A extends Actor<?>> {

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
        return BabelRegistries.ACTOR_TYPE.getKey(actorType);
    }

    //@Nullable
    public A create() {
        return this.factory.create();
    }

    public boolean synced() {
        return this.synced;
    }

    public static class Builder<A extends Actor<?>> {

        private final ActorFactory<? extends A> factory;
        boolean sync = true;

        private Builder(ActorFactory<? extends A> factory) {
            this.factory = factory;
        }

        public static <A extends Actor<?>> Builder<A> of(ActorFactory<? extends A> factory) {
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
    public interface ActorFactory<A extends Actor<?>> {
        A create();
    }
}
