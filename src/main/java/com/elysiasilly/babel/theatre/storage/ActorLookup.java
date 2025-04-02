package com.elysiasilly.babel.theatre.storage;

import com.elysiasilly.babel.theatre.actor.Actor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActorLookup<A extends Actor> {

    private final Map<UUID, A> uuidActorMap = new HashMap<>();

    public void addActor(A actor) {
        this.uuidActorMap.put(actor.uuid(), actor);
    }

    public void removeActor(A actor) {
        removeActor(actor.uuid());
    }

    public void removeActor(UUID id) {
        this.uuidActorMap.remove(id);
    }

    public A getActor(UUID id) {
        return uuidActorMap.get(id);
    }

    public Collection<A> getActors() {
        return uuidActorMap.values();
    }

    public void clear() {
        this.uuidActorMap.clear();
    }
}
