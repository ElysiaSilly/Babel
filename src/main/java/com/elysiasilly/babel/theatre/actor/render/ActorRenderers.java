package com.elysiasilly.babel.theatre.actor.render;

import com.elysiasilly.babel.theatre.actor.Actor;
import com.elysiasilly.babel.theatre.actor.ActorType;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActorRenderers {

    private static final Map<ActorType<?>, ActorRendererProvider<?>> PROVIDERS = new ConcurrentHashMap<>();

    public static <A extends Actor> void put(ActorType<? extends A> actorType, ActorRendererProvider<A> provider) {
        PROVIDERS.put(actorType, provider);
    }

    public static Map<ActorType<?>, ActorRenderer<?>> createRenderers() {
        ImmutableMap.Builder<ActorType<?>, ActorRenderer<?>> builder = ImmutableMap.builder();

         PROVIDERS.forEach((actorType, actorRenderer) -> {
            try {
                builder.put(actorType, actorRenderer.create());
            } catch (Exception e) {
                throw new IllegalStateException(
                        "Failed to create model for " + actorType.getKey(), e
                );
            }
        });

        return builder.build();
    }
}
