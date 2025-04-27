package com.elysiasilly.babel.api.theatre.actor;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.theatre.actor.registry.ActorLike;
import com.elysiasilly.babel.api.theatre.actor.registry.ActorType;
import com.elysiasilly.babel.api.theatre.scene.Scene;
import com.elysiasilly.babel.api.theatre.scene.registry.SceneLike;
import com.elysiasilly.babel.api.theatre.scene.registry.SceneType;
import com.elysiasilly.babel.networking.theatre.clientbound.UpdateActorPacket;
import com.elysiasilly.babel.networking.theatre.serverbound.RequestUpdateActorPacket;
import com.elysiasilly.babel.util.UtilsCodec;
import com.elysiasilly.babel.util.UtilsFormatting;
import com.elysiasilly.babel.util.conversions.ConversionsVector;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Vector3d;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings({"unchecked"})
public abstract class Actor implements ActorLike, SceneLike {

    private final UUID uuid;
    private Scene<?> scene;

    private boolean dirty = true, removed = false;

    private int ticks = 0;

    private Vector3d pos, previousPos;

    public Actor(Vector3d pos, UUID uuid, CompoundTag tag) {
        this(pos, uuid);
        if(tag != null) deserializeFromCopying(tag, level().registryAccess());
    }

    public Actor(Vector3d pos, UUID uuid) {
        this.uuid = uuid;
        this.pos = pos;
        this.previousPos = pos;
    }

    /// POSITION

    public final Vector3d previousPos() {
        return new Vector3d(this.previousPos);
    }

    public final Vec3 previousPosMojang() {
        return ConversionsVector.toMojang(previousPos());
    }

    public final Vector3d pos() {
        return new Vector3d(this.pos);
    }

    public final Vec3 posMojang() {
        return ConversionsVector.toMojang(pos());
    }

    public final SectionPos sectionPos() {
        return SectionPos.of(posMojang());
    }

    public final ChunkPos chunkPos() {
        return sectionPos().chunk();
    }

    public final boolean offset(Vector3d offset) {
        return pos(pos().add(offset));
    }

    public final boolean pos(Vec3 vec) {
        return pos(ConversionsVector.toJOML(vec));
    }

    public final boolean posInvalid(Vector3d pos) {
        return !posValid(pos(), new Vector3d(pos));
    }

    public final boolean posValid(Vector3d pos) {
        return posValid(pos(), new Vector3d(pos));
    }

    public final boolean pos(Vector3d pos) {
        if(posValid(pos)) {
            this.pos = pos;
            markDirty();
            return true;
        } else {
            return false;
        }
    }

    public boolean posValid(Vector3d oldPos, Vector3d newPos) {
        return true;
    }

    ///

    public Level level() {
        return scene().level();
    }

    public Optional<ServerLevel> server() {
        if(level() instanceof ServerLevel server) return Optional.of(server);
        return Optional.empty();
    }

    public Optional<ClientLevel> client() {
        if(level() instanceof ClientLevel client) return Optional.of(client);
        return Optional.empty();
    }

    public Scene<?> scene() {
        return this.scene;
    }

    public void scene(Scene<?> scene) {
        if(scene() == null) {
            this.scene = scene;
        } else {
            if(scene != scene()) {
                if(scene().removeActor(this, ActorRemovalReason.MOVED_SCENE)) this.scene = scene;
            } else {
                Babel.LOGGER.warn("Tried moving an actor to the same scene");
            }
        }
    }

    public UUID uuid() {
        return this.uuid;
    }

    public boolean destroy() {
        return scene().removeActor(this, ActorRemovalReason.MANUAL_DESTROY);
    }

    public void markDirty() {
        this.dirty = true;
    }

    public void markClean() {
        this.dirty = false;
    }

    public boolean dirty() {
        return this.dirty;
    }

    public int ticks() {
        return this.ticks;
    }

    public void markRemoved() {
        this.removed = true;
    }

    public boolean removed() {
        return this.removed;
    }

    public Component translationKey() {
        return Component.translatable(Util.makeDescriptionId("actor", key()));
    }

    public ResourceLocation key() {
        return actorType().getKey();
    }

    public boolean synced() {
        return actorType().synced();
    }

    public <A extends Actor> A self() {
        return (A) this;
    }

    @Override
    public final SceneType<?, ?> sceneType() {
        return actorType().sceneType();
    }

    /// COLLISION

    // wouldnt it be nice if we could put something akin to a model file in a datapack and read it as a voxelshape wink wink
    public abstract VoxelShape collisionShape();

    public VoxelShape collisionShapeWorldSpace() {
        return move(pos(), collisionShape());
    }

    public abstract VoxelShape interactionShape();

    public VoxelShape interactionShapeWorldSpace() {
        return move(pos(), interactionShape());
    }

    private static VoxelShape move(Vector3d vec, VoxelShape shape) {
        return shape.move(vec.x, vec.y, vec.z);
    }

    ///

    /**
     * creates a copy of the actor
     * @return returns the actor with the copied data
     */
    public final Actor copy() {
        return actorType().create(pos(), serializeForCopying(new CompoundTag(), level().registryAccess())).self();
    }

    public final Actor copyWithScene() {
        Actor actor = copy();
        actor.scene(scene());
        return actor;
    }

    /// serializing

    public void deserializeFromCommand(CompoundTag tag, HolderLookup.Provider registries) {
        deserialize(tag, registries);
    }

    /**
     * gathers the data to sync with the client.
     */
    public CompoundTag serializeForClient(CompoundTag tag, HolderLookup.Provider registries) {
        serialize(tag, registries);
        return tag;
    }

    /**
     * Receives the data gathered in {@link Actor#serializeForClient(CompoundTag, HolderLookup.Provider)}.
     */
    public void deserializeFromServer(CompoundTag tag, HolderLookup.Provider registries) {
        deserialize(tag, registries);
    }

    /// for saving
    public CompoundTag serializeForSaving(CompoundTag tag, HolderLookup.Provider registries) {
        serialize(tag, registries);
        return tag;
    }

    /// for saving
    public void deserializeFromSaving(CompoundTag tag, HolderLookup.Provider registries) {
        deserialize(tag, registries);
    }

    /// for copying
    public CompoundTag serializeForCopying(CompoundTag tag, HolderLookup.Provider registries) {
        serialize(tag, registries);
        return tag;
    }

    /// for copying
    public void deserializeFromCopying(CompoundTag tag, HolderLookup.Provider registries) {
        deserialize(tag, registries);
    }

    public void serialize(CompoundTag tag, HolderLookup.Provider registries) {}

    public void deserialize(CompoundTag tag, HolderLookup.Provider registries) {}

    /// SYNCING

    /**
     * Sends an update to the client.
     * Can only be requested from the server.
     */
    public final void sendUpdate() {
        if(level() instanceof ServerLevel server) {
            if(synced()) PacketDistributor.sendToPlayersTrackingChunk(server, chunkPos(), new UpdateActorPacket(this));
        } else {
            Babel.LOGGER.warn("Something tried sending an update packet for {} from the client", uuid().toString());
        }
    }

    /**
     * Requests a {@link Actor#sendUpdate()} to update the actor.
     * Can only be requested from the client.
     */
    public final void requestUpdate() {
        if(level() instanceof ClientLevel client) {
            if(synced()) PacketDistributor.sendToServer(RequestUpdateActorPacket.pack(this));
        } else {
            Babel.LOGGER.warn("Something tried requesting an update packet for {} from the server", uuid().toString());
        }
    }

    /// FUNCTIONALITY

    public final void tickInternal() {
        this.ticks++;
        onTick();
        this.previousPos = pos();
        if(pos().y < -64 && server().isPresent() && !removed()) destroy();
    }

    public void onTick() {}

    public boolean canTick() {
        return false;
    }

    public void onRandomTick() {

    }

    public boolean canRandomTick() {
        return false;
    }

    public void onAdd() {

    }

    public void onRemove(ActorRemovalReason reason) {

    }

    public InteractionResult onPlayerInteraction(Player player, InteractionHand hand, ItemStack stack) {
        return InteractionResult.PASS;
    }

    public boolean canPlayerInteract() {
        return false;
    }

    public boolean canCollide() {
        return true;
    }

    public void onCollide() {

    }

    /// CODEC

    public static final StreamCodec<RegistryFriendlyByteBuf, Actor> STREAM_CODEC = UtilsCodec.ACTOR_STREAM_CODEC;

    public static final Codec<Data> ACTOR_DATA_CODEC = UtilsCodec.ACTOR_DATA_CODEC;

    public Data toData(Level level) {
        return new Data(this, level.registryAccess());
    }

    public record Data(UUID uuid, Vector3d position, ActorType<?> type, CompoundTag tag) {
        public Data(Actor actor, HolderLookup.Provider registries) {
            this(actor.uuid(), actor.pos(), actor.actorType(), actor.serializeForSaving(new CompoundTag(), registries));
        }

        public Actor parse(Level level) {
            Actor actor = type().create(position(), uuid());
            actor.deserializeFromSaving(tag(), level.registryAccess());
            return actor;
        }
    }

    ///

    @Override
    public String toString() {
        return String.format("[%s, %s, %s, %s]", key(), UtilsFormatting.vector3d(pos()), uuid(), level());
    }
}
