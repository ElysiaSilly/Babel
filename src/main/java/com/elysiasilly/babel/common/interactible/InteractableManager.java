package com.elysiasilly.babel.common.interactible;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public abstract class InteractableManager<I extends Interactable> extends SavedData {

    // todo: networking CLIENT <-> SERVER
    // todo: automatic (de)serializing
    // todo: collisions
    // todo: load/unload with chunk
    // todo: dimensions?

    public final Tick TICK;
    public final String NAME;

    public enum Tick { PRE, POST, NONE }

    public InteractableManager(Tick tick, String name) {
        this.TICK = tick;
        this.NAME = name;
    }

    public abstract Codec<I> getCodec();

    public abstract InteractableManager load(CompoundTag compoundTag, HolderLookup.Provider provider);

    public abstract InteractableManager create();

    public InteractableManager get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(this::create, this::load), this.NAME);
    }

    public final List<I> interactables = new ArrayList<>();

    public void add(I interactable) {
        this.interactables.add(interactable);
        interactable.manager = this;
        setDirty();
    }

    public void render(RenderLevelStageEvent event) {
        final List<I> interactables = List.copyOf(this.interactables);
        for(I interactable : interactables) {
            interactable.render(event);
        }
    }

    public void tick(Level level) {
        final List<I> interactables = List.copyOf(this.interactables);
        for(I interactable : interactables) {
            interactable.tick(level);
        }
    }

    public void destroy(I interactable) {
        this.interactables.remove(interactable);
        setDirty();
    }
}
