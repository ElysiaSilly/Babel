package com.elysiasilly.babel.api.theatre.storage;

import com.elysiasilly.babel.api.theatre.actor.Actor;

import java.util.*;

public class ActorLookup {

    private final Map<UUID, Actor> uuidActorMap = new HashMap<>();

    private Map<UUID, Actor> map() {
        return this.uuidActorMap;
    }

    public void add(Actor actor) {
        map().put(actor.uuid(), actor);
    }

    public void remove(Actor actor) {
        remove(actor.uuid());
    }

    public void remove(UUID uuid) {
        map().remove(uuid);
    }

    public Actor actor(UUID uuid) {
        return map().get(uuid);
    }

    public Collection<Actor> actors() {
        return new ArrayList<>(map().values());
    }

    public boolean empty() {
        return actors().isEmpty();
    }

    public void clear() {
        map().clear();
    }
}
