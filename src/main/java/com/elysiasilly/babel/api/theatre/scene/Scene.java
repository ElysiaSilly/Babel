package com.elysiasilly.babel.api.theatre.scene;

import com.elysiasilly.babel.api.events.ActorEvents;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.ActorSelector;
import com.elysiasilly.babel.api.theatre.storage.ActorStorage;
import com.elysiasilly.babel.util.MCUtil;
import com.elysiasilly.babel.util.utils.DevUtil;
import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@SuppressWarnings({"unchecked"})
public abstract class Scene<L extends Level> {

    private final ActorStorage storage = new ActorStorage();
    private final L level;

    protected Scene(L level) {
        this.level = level;
    }

    public L level() {
        return this.level;
    }

    public ResourceKey<Level> dimension() {
        return level().dimension();
    }

    public abstract SceneType<?, ?> getSceneType();

    ///

    // lmao
    public <S extends Scene<?>> S self() {
        return (S) this;
    }

    /// functionality

    public final void tickInternal() {
        tickActors();
        tick();
    }

    private void tickActors() {
        for(Actor actor : getActorsInStorage()) {
            if(actor.canTick() && !actor.removed()) actor.onTick();
        }
    }

    public void tick() {}

    public void onSceneLoad() {}

    public void onSceneUnload() {}

    /// actor storage

    public ActorStorage storage() {
        return this.storage;
    }

    public Collection<Actor> getActorsInStorage() {
        return storage().getActors();
    }

    public void addActor(Actor actor) {
        if(DevUtil.postEventCancelable(new ActorEvents.Added(actor, this))) {
            actor.setScene(this);
            storage().addActor(actor);
            actor.onAdd();
        }
    }

    public void removeActor(Actor actor) {
        if(DevUtil.postEventCancelable(new ActorEvents.Removed(actor, this))) {
            actor.onRemove();
            actor.markRemoved();
            storage().removeActor(actor);
        }
    }

    public void removeActor(UUID uuid) {
        removeActor(storage().getActor(uuid));
    }

    public Actor getActor(UUID uuid) {
        return storage().getActor(uuid);
    }

    public abstract void loadChunk(ChunkAccess chunkAccess);

    public abstract void unloadChunk(ChunkAccess chunk);

    /// TEMP collisions (ActorGetter)

    public List<Actor> getActors(AABB area, Predicate<? super Actor> predicate) {

        List<Actor> actors = new ArrayList<>();

        for(Actor actor : getActorsInStorage()) {
            if(area.contains(actor.getPos())) {
                actors.add(actor);
            }
        }

        return actors;
    }

    public List<VoxelShape> getCollisions(AABB area) {

        List<Actor> actors = getActors(area.inflate(1), ActorSelector.CAN_BE_COLLIDED_WITH);

        ImmutableList.Builder<VoxelShape> builder = ImmutableList.builderWithExpectedSize(actors.size());

        for(Actor actor : actors) {
            builder.add(actor.getCollisionBox());
        }

        return builder.build();
    }

    public InteractionResult playerInteractionRequest(Player player, InteractionHand hand, ItemStack stack) {
        List<Vec3> raycast = MCUtil.Raycast.shittyRayCast(player, MCUtil.Raycast.GOOD_ENOUGH);

        Actor temp = null;

        outer : for(Actor actor : getActorsInStorage()) {
            for(Vec3 point : raycast) {
                if(actor.getCollisionBox().isEmpty()) break;
                AABB aabb = actor.getCollisionBox().bounds().move(actor.getPos());

                if(aabb.contains(point)) {
                    temp = actor;
                    break outer;
                }
            }
        }

        return temp == null ? InteractionResult.PASS : temp.onPlayerInteraction(player, hand, stack);
    }
}
