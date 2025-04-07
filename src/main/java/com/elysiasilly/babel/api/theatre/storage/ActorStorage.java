package com.elysiasilly.babel.api.theatre.storage;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Collection;
import java.util.UUID;

public class ActorStorage {

    /// all actors in the level
    final ActorLookup lookupLevel = new ActorLookup();

    /// map of sections and actors that are in it
    final ActorSectionMap lookupSection = new ActorSectionMap();

    /// map of chunks and actors that are in it
    final ActorChunkMap lookupChunk = new ActorChunkMap();

    ///

    public Collection<Actor> getActors() {
        return this.lookupLevel.getActors();
    }

    public void addActor(Actor actor) {
        this.lookupLevel.addActor(actor);
        this.lookupSection.addActor(actor);
        this.lookupChunk.addActor(actor);
    }

    public void removeActor(Actor actor) {
        this.lookupLevel.removeActor(actor);
        this.lookupSection.removeActor(actor);
        this.lookupChunk.removeActor(actor);
    }

    public Actor getActor(UUID uuid) {
        return this.lookupLevel.getActor(uuid);
    }

    public Collection<Actor> getActorsInSection(SectionPos pos) {
        return this.lookupSection.getActorsInSection(pos);
    }

    public Collection<Actor> getActorsInChunk(ChunkPos pos) {
        return this.lookupChunk.getActorsInChunk(pos);
    }

    private void unloadSection(SectionPos pos) {
        for(Actor actor : getActorsInSection(pos)) this.lookupLevel.removeActor(actor);
        this.lookupSection.removeSection(pos);
    }

    public void unloadChunk(ChunkAccess chunk) {
        this.lookupChunk.removeChunk(chunk.getPos());
        for(int y = chunk.getMinSection(); y < chunk.getMaxSection(); y++) {
            unloadSection(SectionPos.of(chunk.getPos(), y));
        }
    }
}
