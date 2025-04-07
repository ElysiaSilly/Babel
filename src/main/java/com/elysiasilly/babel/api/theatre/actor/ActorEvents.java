package com.elysiasilly.babel.api.theatre.actor;

import com.elysiasilly.babel.api.theatre.scene.Scene;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;

public class ActorEvents extends Event implements IModBusEvent {

    private final Actor actor;
    private final Scene<?> scene;

    public Actor getActor() {
        return this.actor;
    }

    public Scene<?> getScene() {
        return this.scene;
    }

    public ActorEvents(Actor actor, Scene<?> scene) {
        this.actor = actor;
        this.scene = scene;
    }

    public static class ActorAdded extends ActorEvents implements ICancellableEvent {
        public ActorAdded(Actor actor, Scene<?> scene) {
            super(actor, scene);
        }
    }

    public static class ActorRemoved extends ActorEvents implements ICancellableEvent {
        //private final ActorRemovalReason reason;

        public ActorRemoved(Actor actor, Scene<?> scene) {
            super(actor, scene);
            //this.reason = reason;
        }

        //public ActorRemovalReason getReason() {
        //    return this.reason;
        //}
    }

    public static class ActorUnloaded extends ActorEvents {
        public ActorUnloaded(Actor actor, Scene<?> scene) {
            super(actor, scene);
        }
    }

    public static class ActorLoaded extends ActorEvents {
        public ActorLoaded(Actor actor, Scene<?> scene) {
            super(actor, scene);
        }
    }
}
