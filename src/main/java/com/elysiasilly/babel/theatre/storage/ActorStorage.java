package com.elysiasilly.babel.theatre.storage;

import com.elysiasilly.babel.theatre.actor.Actor;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.core.SectionPos;

import java.util.Collection;
import java.util.UUID;

public class ActorStorage<A extends Actor> {

    /// all actors in the level
    final ActorLookup<A> lookupLevel = new ActorLookup<>();

    /// map of section and actors that are in it
    final ActorSectionMap<A> lookupSection = new ActorSectionMap<>();

    public Long2ObjectMap<ActorLookup<A>> getSections() {
        return this.lookupSection.getSections();
    }

    public Collection<A> getActors() {
        return this.lookupLevel.getActors();
    }

    public void addActor(A actor) {
        this.lookupLevel.addActor(actor);
        this.lookupSection.addActor(actor);
    }

    public void removeActor(A actor) {
        this.lookupLevel.removeActor(actor);
        this.lookupSection.removeActor(actor);
    }

    public A getActor(UUID uuid) {
        return this.lookupLevel.getActor(uuid);
    }

    public Collection<A> getActorsInSection(long key) {
        return this.lookupSection.getActorsInSection(key);
    }

    public Collection<A> getActorsInSection(SectionPos pos) {
        return this.lookupSection.getActorsInSection(pos);
    }

    public void unloadSection(SectionPos pos) {
        for(A actor : getActorsInSection(pos)) {
            lookupLevel.removeActor(actor);
        }
        lookupSection.removeSection(pos);
    }
}
