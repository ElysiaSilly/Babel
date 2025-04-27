package com.elysiasilly.babel.api.theatre.storage;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.util.UtilsFormatting;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.*;

public class ActorStorage {

    /// all actors in the level
    private final ActorLookup lookup = new ActorLookup();

    /// map of populated sections and their actorLookups
    /// if a section's lookup is empty, it is automatically removed from the map
    /// only populated sections should be in here
    private final Long2ObjectMap<ActorLookup> sectionLookup = new Long2ObjectOpenHashMap<>();

    /// map of actor uuids and the section they reside in
    private final HashMap<UUID, Long> sectionByActorUUID = new HashMap<>();

    ///

    private HashMap<UUID, Long> sectionByActor() {
        return this.sectionByActorUUID;
    }

    public SectionPos section(Actor actor) {
        return SectionPos.of(sectionByActor().get(actor.uuid()));
    }

    private Long2ObjectMap<ActorLookup> sectionLookup() {
        return this.sectionLookup;
    }

    public Optional<ActorLookup> lookup(SectionPos section) {
        return Optional.ofNullable(sectionLookup().get(section.asLong()));
    }

    private ActorLookup lookup() {
        return this.lookup;
    }

    ///

    public void sort() {
        for(Actor actor : actors()) {
            if(actor.dirty()) {
                if(!sectionByActor().containsKey(actor.uuid()))
                    Babel.LOGGER.warn("No entry found for Actor {}, Something Somehow has gone wrong", actor);

                SectionPos oldPos = section(actor);
                SectionPos newPos = actor.sectionPos();

                if(!oldPos.equals(newPos)) {
                    try {
                        remove(actor);
                        add(actor);

                        Babel.LOGGER.info("moved Actor {} from Section {} to {} | {}", actor, format(oldPos), format(newPos), actor.scene());
                    } catch (Exception e) {
                        throw new IllegalStateException(String.format("Tried sorting %s from %s to %s [%s]", actor.uuid(), format(oldPos), format(newPos), actor.level().isClientSide), e);
                    }
                }
            }
        }
    }

    public void remove(Actor actor) {
        long section = section(actor).asLong();

        lookup().remove(actor);
        sectionLookup().get(section).remove(actor);

        if(sectionLookup().get(section).empty()) {
            sectionLookup().remove(section);
        }

        sectionByActor().remove(actor.uuid());
    }

    public void add(Actor actor) {
        long section = actor.sectionPos().asLong();

        lookup().add(actor);
        sectionLookup().computeIfAbsent(section, i -> new ActorLookup()).add(actor);

        sectionByActor().put(actor.uuid(), section);



        Babel.LOGGER.info("Linked Actor {} to {}", actor, sectionByActor().get(actor.uuid()));
    }

    ///

    public Actor actor(UUID uuid) {
        return lookup().actor(uuid);
    }

    public Collection<Actor> actors() {
        return lookup().actors();
    }

    public Collection<Actor> actors(SectionPos section) {
        Collection<Actor> actors = new ArrayList<>();

        lookup(section).ifPresent(lookup -> actors.addAll(lookup.actors()));

        return actors;
    }

    public Collection<Actor> actors(ChunkPos chunk) {
        Collection<Actor> actors = new ArrayList<>();

        for(long section : sectionLookup().keySet()) {
            SectionPos sectionPos = SectionPos.of(section);
            if(chunk.x == sectionPos.x() && chunk.z == sectionPos.z()) {
                lookup(sectionPos).ifPresent(lookup -> actors.addAll(lookup.actors()));
            }
        }

        return actors;
    }

    public Collection<Actor> actors(ChunkPos chunk, int min, int max) {
        Collection<Actor> actors = new ArrayList<>();

        for(int y = min; y < max; y++) {
            lookup(SectionPos.of(chunk, y)).ifPresent(lookup -> actors.addAll(lookup.actors()));
        }

        return actors;
    }

    public Collection<Actor> actors(ChunkAccess chunk) {
        return actors(chunk.getPos(), chunk.getMinSection(), chunk.getMaxSection());
    }

    public void unload(SectionPos section) {
        for(Actor actor : actors(section)) {
            sectionByActor().remove(actor.uuid());
            lookup().remove(actor);
        }

        lookup(section).ifPresent(ActorLookup::clear);
    }

    public void unload(ChunkPos chunk, int min, int max) {
        for(int y = min; y < max; y++) {
            unload(SectionPos.of(chunk, y));
        }
    }

    public void unload(ChunkAccess chunk) {
        unload(chunk.getPos(), chunk.getMinSection(), chunk.getMaxSection());
    }

    public Collection<ChunkPos> populatedChunks() {
        Collection<ChunkPos> chunks = new ArrayList<>();

        for(SectionPos section : populatedSections()) {
            if(!chunks.contains(section.chunk())) chunks.add(section.chunk());
        }

        return chunks;
    }

    public Collection<SectionPos> populatedSections() {
        Collection<SectionPos> sections = new ArrayList<>();

        for(long section : sectionLookup().keySet()) {
            if(!sections.contains(SectionPos.of(section))) sections.add(SectionPos.of(section));
        }

        return sections;
    }

    ///

    private static String format(SectionPos pos) {
        return UtilsFormatting.sectionPos(pos);
    }
}
