package com.elysiasilly.babel.theatre.actor.render;

import com.elysiasilly.babel.theatre.actor.Actor;
import com.elysiasilly.babel.theatre.actor.ActorType;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

public class ActorRenderersEvent extends Event implements IModBusEvent {

    public ActorRenderersEvent() {}

    public <A extends Actor> void registerRenderer(ActorType<? extends A> actorType, ActorRendererProvider<A> provider) {
        ActorRenderers.put(actorType, provider);
    }
}
