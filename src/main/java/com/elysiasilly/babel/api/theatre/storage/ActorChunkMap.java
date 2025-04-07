package com.elysiasilly.babel.api.theatre.storage;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.world.level.ChunkPos;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class ActorChunkMap {

    private final Long2ObjectMap<ActorLookup> lookupChunk = new Long2ObjectOpenHashMap<>();

    private final HashMap<UUID, Long> actorChunkMap = new HashMap<>();

    public ActorLookup getLookupInChunk(ChunkPos pos) {
        return this.lookupChunk.get(pos.toLong());
    }

    public void addActor(Actor actor) {
        long key = actor.getChunkPos().toLong();

        this.actorChunkMap.put(actor.uuid(), key);
        this.lookupChunk.computeIfAbsent(key, i -> new ActorLookup()).addActor(actor);
    }

    public void removeActor(Actor actor) {
        long key = actor.getChunkPos().toLong();

        this.actorChunkMap.remove(actor.uuid());
        this.lookupChunk.get(key).removeActor(actor);
        if(this.lookupChunk.get(key).getActors().isEmpty()) this.lookupChunk.remove(key);
    }

    public Collection<Actor> getActorsInChunk(ChunkPos pos) {
        return this.lookupChunk.getOrDefault(pos.toLong(), new ActorLookup()).getActors();
    }

    public void removeChunk(ChunkPos pos) {
        long key = pos.toLong();

        if(this.lookupChunk.containsKey(key)) {
            if(getActorsInChunk(pos).isEmpty()) {
                this.lookupChunk.remove(key);
                return;
            }
            for(Actor actor : getActorsInChunk(pos)) {
                this.actorChunkMap.remove(actor.uuid());
            }
            getLookupInChunk(pos).clear();
            this.lookupChunk.remove(key);
        }
    }
}
