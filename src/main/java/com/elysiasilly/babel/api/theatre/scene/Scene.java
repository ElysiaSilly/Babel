package com.elysiasilly.babel.api.theatre.scene;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.events.ActorEvents;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.ActorPredicates;
import com.elysiasilly.babel.api.theatre.actor.ActorRemovalReason;
import com.elysiasilly.babel.api.theatre.handler.ActorCollisionHandler;
import com.elysiasilly.babel.api.theatre.scene.registry.SceneLike;
import com.elysiasilly.babel.api.theatre.storage.ActorStorage;
import com.elysiasilly.babel.networking.theatre.clientbound.UpdateActorPacket;
import com.elysiasilly.babel.util.UtilsDev;
import com.elysiasilly.babel.util.UtilsMC;
import net.minecraft.Util;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"unchecked"})
public abstract class Scene<L extends Level> implements SceneLike {

    private final ActorCollisionHandler collisionHandler = new ActorCollisionHandler(this);
    private final ActorStorage storage = new ActorStorage();
    private final L level;

    private int ticks = 0;

    protected Scene(L level) {
        this.level = level;
    }

    public L level() {
        return this.level;
    }

    public ResourceKey<Level> dimension() {
        return level().dimension();
    }

    public <S extends Scene<?>> S self() {
        return (S) this;
    }

    private ActorCollisionHandler collisionHandler() {
        return this.collisionHandler;
    }

    public Component translationKey() {
        return Component.translatable(Util.makeDescriptionId("actor", key()));
    }

    public ResourceLocation key() {
        return sceneType().getKey();
    }

    /// functionality

    public final void tickInternal() {
        try {
            this.ticks++;
            tickActors();
            collisionHandler().tick();
            onTick();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Something went wrong while trying to tick %s [%s]", this, actors().size()), e);
        }
    }

    private void tickActors() {
        try {
            storage().sort();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Something went wrong while trying to sort the storage %s [%s]", this, actors().size()), e);
        }

        for(Actor actor : actors()) {
            if(ActorPredicates.CAN_TICK.test(actor)) {
                try {
                    actor.tickInternal();
                } catch (Exception e) {
                    throw new RuntimeException(String.format("Something went wrong while trying to tick %s", actor), e);
                }
            }

            if(ActorPredicates.SHOULD_SYNC.test(actor) && this instanceof ServerScene server) {
                PacketDistributor.sendToPlayersTrackingChunk(server.level(), actor.chunkPos(), new UpdateActorPacket(actor));
                actor.markClean();
            }
        }
    }

    public int ticks() {
        return this.ticks;
    }

    public void onTick() {}

    public void onSceneLoad() {}

    public void onSceneUnload() {}

    /// actor storage

    protected ActorStorage storage() {
        return this.storage;
    }

    public Collection<Actor> actors() {
        return storage().actors();
    }

    public Collection<Actor> actorsInChunk(ChunkPos pos) {
        return storage().actors(pos);
    }

    public Collection<Actor> actorsInSection(SectionPos pos) {
        return storage().actors(pos);
    }

    public Collection<ChunkPos> populatedChunks() {
        return storage().populatedChunks();
    }

    public Collection<SectionPos> populatedSections() {
        return storage().populatedSections();
    }

    public boolean addActor(Actor actor) {
        if(!actor.sceneType().equals(sceneType()))
            throw new IllegalStateException("Tried adding an Actor to the wrong Scene");

        if(UtilsDev.postGameEventCancelable(new ActorEvents.Added(actor, this))) {
            actor.scene(this);
            storage().add(actor);
            actor.onAdd();
            Babel.LOGGER.info("Added Actor {} to Scene {}", actor, this);
            return true;
        } else {
            Babel.LOGGER.info("Failed to add Actor {} to Scene {}", actor, this);
            return false;
        }
    }

    public boolean removeActor(Actor actor, ActorRemovalReason reason) {
        if(UtilsDev.postGameEventCancelable(new ActorEvents.Removed(actor, this, reason))) {
            actor.onRemove(reason);
            actor.markRemoved();
            storage().remove(actor);
            Babel.LOGGER.info("Removed Actor {} from Scene {}", actor, this);
            return true;
        } else {
            Babel.LOGGER.info("Failed to remove Actor {} from Scene {}", actor, this);
            return false;
        }
    }

    public boolean removeActor(UUID uuid, ActorRemovalReason reason) {
        return removeActor(storage().actor(uuid), reason);
    }

    public Actor getActor(UUID uuid) {
        return storage().actor(uuid);
    }

    public abstract void loadChunk(ChunkAccess chunkAccess);

    public abstract void unloadChunk(ChunkAccess chunk);

    /// TEMP

    public InteractionResult playerInteractionRequest(Player player, InteractionHand hand, ItemStack stack) {
        List<Vec3> raycast = UtilsMC.Raycast.shittyRayCast(player, UtilsMC.Raycast.GOOD_ENOUGH);

        Actor temp = null;

        outer : for(Actor actor : actors()) {
            for(Vec3 point : raycast) {
                if(actor.collisionShape().isEmpty()) break;
                AABB aabb = actor.collisionShape().bounds().move(actor.posMojang());

                if(aabb.contains(point)) {
                    temp = actor;
                    break outer;
                }
            }
        }

        return temp == null ? InteractionResult.PASS : temp.onPlayerInteraction(player, hand, stack);
    }

    ///

    @Override
    public String toString() {
        return String.format("[%s, %s, %s]", sceneType().getKey(), dimension().location(), level());
    }
}
