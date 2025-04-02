package com.elysiasilly.babel.theatre.storage;

import com.elysiasilly.babel.theatre.actor.Actor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActorLookup {

    private final Map<UUID, Actor> uuidActorMap = new HashMap<>();

    public void addActor(Actor actor) {
        this.uuidActorMap.put(actor.uuid(), actor);
    }

    public void removeActor(Actor actor) {
        removeActor(actor.uuid());
    }

    public void removeActor(UUID id) {
        this.uuidActorMap.remove(id);
    }

    public Actor getActor(UUID id) {
        return uuidActorMap.get(id);
    }

    public Collection<Actor> getActors() {
        return uuidActorMap.values();
    }

    public void clear() {
        this.uuidActorMap.clear();
    }
}
