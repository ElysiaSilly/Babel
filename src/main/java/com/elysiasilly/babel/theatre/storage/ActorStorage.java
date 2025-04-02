package com.elysiasilly.babel.theatre.storage;

import com.elysiasilly.babel.theatre.actor.Actor;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.core.SectionPos;

import java.util.Collection;
import java.util.UUID;

public class ActorStorage {

    /// all actors in the level
    final ActorLookup lookupLevel = new ActorLookup();

    /// map of section and actors that are in it
    final ActorSectionMap lookupSection = new ActorSectionMap();

    public Long2ObjectMap<ActorLookup> getSections() {
        return this.lookupSection.getSections();
    }

    public Collection<Actor> getActors() {
        return this.lookupLevel.getActors();
    }

    public void addActor(Actor actor) {
        this.lookupLevel.addActor(actor);
        this.lookupSection.addActor(actor);
    }

    public void removeActor(Actor actor) {
        this.lookupLevel.removeActor(actor);
        this.lookupSection.removeActor(actor);
    }

    public Actor getActor(UUID uuid) {
        return this.lookupLevel.getActor(uuid);
    }

    public Collection<Actor> getActorsInSection(long key) {
        return this.lookupSection.getActorsInSection(key);
    }

    public Collection<Actor> getActorsInSection(SectionPos pos) {
        return this.lookupSection.getActorsInSection(pos);
    }

    public void unloadSection(SectionPos pos) {
        for(Actor actor : getActorsInSection(pos)) lookupLevel.removeActor(actor);
        lookupSection.removeSection(pos);
    }
}
