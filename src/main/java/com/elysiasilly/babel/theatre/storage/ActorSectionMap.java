package com.elysiasilly.babel.theatre.storage;

import com.elysiasilly.babel.theatre.actor.Actor;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.SectionPos;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class ActorSectionMap {

    /// map of section keys to the actors in a section
    private final Long2ObjectMap<ActorLookup> lookupBySection = new Long2ObjectOpenHashMap<>();

    /// map of actor uuid to section keys
    private final HashMap<UUID, Long> actorSectionMap = new HashMap<>();


    public Long2ObjectMap<ActorLookup> getSections() {
        return this.lookupBySection;
    }

    public ActorLookup getLookupInSection(SectionPos pos) {
        return this.lookupBySection.get(pos.asLong());
    }

    public void addActor(Actor actor) {
        long key = actor.getSectionPos().asLong();

        actorSectionMap.put(actor.uuid(), key);

        if(!lookupBySection.containsKey(key)) lookupBySection.put(key, new ActorLookup());
        lookupBySection.get(key).addActor(actor);
    }

    public void removeActor(Actor actor) {
        long key = actor.getSectionPos().asLong();

        actorSectionMap.remove(actor.uuid());

        lookupBySection.get(key).removeActor(actor);
        if(lookupBySection.get(key).getActors().isEmpty()) lookupBySection.remove(key);
    }

    public Collection<Actor> getActorsInSection(SectionPos pos) {
        return lookupBySection.getOrDefault(pos.asLong(), new ActorLookup()).getActors();
    }

    public Collection<Actor> getActorsInSection(long key) {
        return lookupBySection.getOrDefault(key, new ActorLookup()).getActors();
    }

    public void removeSection(SectionPos pos) {
        long key = pos.asLong();

        if(this.lookupBySection.containsKey(key)) {
            if(getActorsInSection(pos).isEmpty()) {
                this.lookupBySection.remove(key);
                return;
            }
            for(Actor actor : getActorsInSection(pos)) {
                actorSectionMap.remove(actor.uuid());
            }
            getLookupInSection(pos).clear();
            this.lookupBySection.remove(key);
        }
    }
}
