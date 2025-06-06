package com.elysiasilly.babel.api.events;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.ActorRemovalReason;
import com.elysiasilly.babel.api.theatre.scene.Scene;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public abstract class ActorEvents extends Event {

    private final Actor actor;
    private final Scene<?> scene;

    public Actor actor() {
        return this.actor;
    }

    public Scene<?> scene() {
        return this.scene;
    }

    private ActorEvents(Actor actor, Scene<?> scene) {
        this.actor = actor;
        this.scene = scene;
    }

    public static class Added extends ActorEvents implements ICancellableEvent {
        public Added(Actor actor, Scene<?> scene) {
            super(actor, scene);
        }
    }

    public static class Removed extends ActorEvents implements ICancellableEvent {
        private final ActorRemovalReason reason;

        public Removed(Actor actor, Scene<?> scene, ActorRemovalReason reason) {
            super(actor, scene);
            this.reason = reason;
        }

        public ActorRemovalReason reason() {
            return this.reason;
        }
    }

    public static class Unloaded extends ActorEvents {
        private final ChunkAccess chunk;

        public Unloaded(Actor actor, Scene<?> scene, ChunkAccess chunk) {
            super(actor, scene);
            this.chunk = chunk;
        }

        public ChunkAccess chunk() {
            return this.chunk;
        }
    }

    public static class Loaded extends ActorEvents {
        private final ChunkAccess chunk;

        public Loaded(Actor actor, Scene<?> scene, ChunkAccess chunk) {
            super(actor, scene);
            this.chunk = chunk;
        }

        public ChunkAccess chunk() {
            return this.chunk;
        }
    }
}
