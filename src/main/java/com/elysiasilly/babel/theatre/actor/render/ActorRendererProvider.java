package com.elysiasilly.babel.theatre.actor.render;

import com.elysiasilly.babel.theatre.actor.Actor;

@FunctionalInterface
public interface ActorRendererProvider<A extends Actor> {
    ActorRenderer<A> create();

    /*
    public static class Context {

    }
     */
}
