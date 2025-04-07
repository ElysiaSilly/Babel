package com.elysiasilly.babel.api.theatre.storage;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.SectionPos;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class ActorSectionMap {

    /// map of section keys to the actors in a section
    private final Long2ObjectMap<ActorLookup> lookupSection = new Long2ObjectOpenHashMap<>();

    /// map of actor uuid to section keys
    private final HashMap<UUID, Long> actorSectionMap = new HashMap<>();

    public ActorLookup getLookupInSection(SectionPos pos) {
        return this.lookupSection.get(pos.asLong());
    }

    public void addActor(Actor actor) {
        long key = actor.getSectionPos().asLong();

        this.actorSectionMap.put(actor.uuid(), key);
        this.lookupSection.computeIfAbsent(key, i -> new ActorLookup()).addActor(actor);
    }

    public void removeActor(Actor actor) {
        long key = actor.getSectionPos().asLong();

        this.actorSectionMap.remove(actor.uuid());

        this.lookupSection.get(key).removeActor(actor);
        if(this.lookupSection.get(key).getActors().isEmpty()) this.lookupSection.remove(key);
    }

    public Collection<Actor> getActorsInSection(SectionPos pos) {
        return this.lookupSection.getOrDefault(pos.asLong(), new ActorLookup()).getActors();
    }

    public void removeSection(SectionPos pos) {
        long key = pos.asLong();

        if(this.lookupSection.containsKey(key)) {
            if(getActorsInSection(pos).isEmpty()) {
                this.lookupSection.remove(key);
                return;
            }
            for(Actor actor : getActorsInSection(pos)) {
                this.actorSectionMap.remove(actor.uuid());
            }
            getLookupInSection(pos).clear();
            this.lookupSection.remove(key);
        }
    }
}
