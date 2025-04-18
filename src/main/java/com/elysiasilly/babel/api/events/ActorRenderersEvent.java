package com.elysiasilly.babel.api.events;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.ActorType;
import com.elysiasilly.babel.api.theatre.actor.render.ActorRendererProvider;
import com.elysiasilly.babel.api.theatre.actor.render.ActorRenderers;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

public abstract class ActorRenderersEvent extends Event implements IModBusEvent {

    private ActorRenderersEvent() {}

    public static class RegisterRenderer extends ActorRenderersEvent {

        public <A extends Actor> void register(ActorType<? extends A> actorType, ActorRendererProvider<A> provider) {
            ActorRenderers.put(actorType, provider);
        }
    }
}
