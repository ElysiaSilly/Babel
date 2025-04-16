package com.elysiasilly.babel.api.theatre.storage;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Collection;
import java.util.UUID;

public class ActorStorage {

    /// all actors in the level
    final ActorLookup uuidActorLookup = new ActorLookup();

    /// map of sections and actors that are in it
    final ActorSectionMap sectionActorLookup = new ActorSectionMap();

    /// map of chunks and actors that are in it
    final ActorChunkMap chunkActorLookup = new ActorChunkMap();

    ///

    public Collection<Actor> getActors() {
        return this.uuidActorLookup.getActors();
    }

    public void addActor(Actor actor) {
        this.uuidActorLookup.addActor(actor);
        this.sectionActorLookup.addActor(actor);
        this.chunkActorLookup.addActor(actor);
    }

    public void removeActor(Actor actor) {
        this.uuidActorLookup.removeActor(actor);
        this.sectionActorLookup.removeActor(actor);
        this.chunkActorLookup.removeActor(actor);
    }

    public Actor getActor(UUID uuid) {
        return this.uuidActorLookup.getActor(uuid);
    }

    public Collection<Actor> getActorsInSection(SectionPos pos) {
        return this.sectionActorLookup.getActorsInSection(pos);
    }

    public Collection<Actor> getActorsInChunk(ChunkPos pos) {
        return this.chunkActorLookup.getActorsInChunk(pos);
    }

    private void unloadSection(SectionPos pos) {
        for(Actor actor : getActorsInSection(pos)) this.uuidActorLookup.removeActor(actor);
        this.sectionActorLookup.removeSection(pos);
    }

    public void unloadChunk(ChunkAccess chunk) {
        this.chunkActorLookup.removeChunk(chunk.getPos());
        for(int y = chunk.getMinSection(); y < chunk.getMaxSection(); y++) {
            unloadSection(SectionPos.of(chunk.getPos(), y));
        }
    }
}
