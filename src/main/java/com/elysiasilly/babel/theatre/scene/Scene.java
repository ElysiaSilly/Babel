package com.elysiasilly.babel.theatre.scene;

import com.elysiasilly.babel.theatre.actor.Actor;
import com.elysiasilly.babel.theatre.actor.ActorSelector;
import com.elysiasilly.babel.theatre.storage.ActorStorage;
import com.elysiasilly.babel.util.MCUtil;
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

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class Scene<A extends Actor, L extends Level> /* extends SavedData */ {

    // todo : will probably need something more elegant than SavedData
    // todo : separate into ClientScene and ServerScene (SceneType<YourClientScene, YourServerScene>)

    private final ActorStorage<A> storage = new ActorStorage<>();
    private final L level;

    protected Scene(L level) {
        this.level = level;
    }

    public L level() {
        return this.level;
    }

    //public ResourceKey<Level> dimension() {
    //    return level().dimension();
    //}

    public ActorStorage<A> storage() {
        return this.storage;
    }

    public abstract SceneType<?, ?> getSceneType();

    ///

    // lmao
    public <S extends Scene> S self() {
        return (S) this;
    }

    /// functionality

    private void tickInternal() {
        tickActors();
        tick();
    }

    private void tickActors() { // temp
        for(A actor : getActorsInStorage()) {
            if(actor.canTick()) actor.tick();
        }
    }

    public void tick() {}

    public void onSceneLoad() {}

    public void onSceneUnload() {}

    /// actor storage

    public ActorStorage<A> getActorStorage() {
        return this.storage;
    }

    public Collection<A> getActorsInStorage() {
        return this.storage.getActors();
    }

    // eventually want to allow adding actors from client (synced to server) with specific conditions, but need to think through security
    public void addActor(A actor) {
        System.out.println("added actor on " + level() + " : " + actor.getSectionPos().asLong());

        actor.setScene(self());
        this.storage.addActor(actor);
        actor.onAdd();
    }

    // ditto
    public void removeActor(A actor) {
        actor.onRemove();
        actor.markRemoved();
        this.storage.removeActor(actor);
    }

    public void removeActor(UUID uuid) {
        removeActor(this.storage.getActor(uuid));
    }

    public A getActor(UUID uuid) {
        return this.storage.getActor(uuid);
    }

    public abstract void loadChunk(ChunkAccess chunkAccess);

    public abstract void unloadChunk(ChunkAccess chunk);

    /// TEMP collisions (ActorGetter)

    public List<Actor<?>> getActors(AABB area, Predicate<? super Actor<?>> predicate) {

        List<Actor<?>> actors = new ArrayList<>();

        for(Actor<?> actor : getActorsInStorage()) {
            if(area.contains(actor.getPos())) {
                actors.add(actor);
            }
        }

        return actors;
    }

    public List<VoxelShape> getCollisions(AABB area) {

        List<Actor<?>> actors = getActors(area.inflate(1), ActorSelector.CAN_BE_COLLIDED_WITH);

        ImmutableList.Builder<VoxelShape> builder = ImmutableList.builderWithExpectedSize(actors.size());

        for(Actor<?> actor : actors) {
            builder.add(actor.getCollisionBox());
        }

        //if(!actors.isEmpty()) System.out.println(actors.size());

        return builder.build();
    }

    public InteractionResult playerInteractionRequest(Player player, InteractionHand hand, ItemStack stack) {
        List<Vec3> raycast = MCUtil.Raycast.shittyRayCast(player, MCUtil.Raycast.GOOD_ENOUGH);

        A temp = null;

        outer : for(A actor : getActorsInStorage()) {
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

    /// TEMP savedata stuff

    /*
    public Scene<?, ?> get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(this::create, this::load), getSceneType().getKey().toString());
    }

    public Scene<?, ?> load(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return copy();
    }

    public Scene<?, ?> copy() {
        return getSceneType().create(this.level);
    }

    private Scene<?, ?> create() {
        return copy();
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return compoundTag;
    }
    */
}
