package com.elysiasilly.babel.api.theatre.actor;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.networking.clientbound.UpdateActorPacket;
import com.elysiasilly.babel.networking.serverbound.RequestUpdateActorPacket;
import com.elysiasilly.babel.api.theatre.scene.Scene;
import com.elysiasilly.babel.api.theatre.scene.SceneType;
import com.elysiasilly.babel.util.utils.SerializationUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
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

import java.util.UUID;

@SuppressWarnings({"unchecked"})
public abstract class Actor {

    private final UUID uuid;
    private Scene<?> scene;

    private boolean dirty = true, removed = false;

    public Vec3 position = Vec3.ZERO;

    public Actor(UUID uuid) {
        this.uuid = uuid;
    }

    /// position

    public Vec3 getPos() {
        return position;
    }

    // TODO section positions currently dont get recalculated
    public void setPos(Vec3 pos) {
        this.position = pos;
    }

    public SectionPos getSectionPos() {
        return SectionPos.of(getPos());
    }

    public ChunkPos getChunkPos() {
        return getSectionPos().chunk();
    }

    ///

    public Level level() {
        return getScene().level();
    }

    public boolean isServer() {
        return !isClient();
    }

    public boolean isClient() {
        return level().isClientSide();
    }

    public Scene<?> getScene() {
        return this.scene;
    }

    public void setScene(Scene<?> scene) {
        this.scene = scene;
    }

    public UUID uuid() {
        return this.uuid;
    }

    public void destroy() {
        getScene().removeActor(this);
    }

    public void markDirty() {
        this.dirty = true;
    }

    public void markRemoved() {
        this.removed = true;
    }

    public boolean removed() {
        return this.removed;
    }

    /// collision related stuff (temp)

    public boolean canBeCollided() {
        return true;
    }

    // wouldnt it be nice if we could put something akin to a model file in a datapack and read it as a voxelshape wink wink
    public abstract VoxelShape getCollisionBox();

    ///

    /**
     * yeah
     */
    public <A extends Actor> A self() {
        return (A) this;
    }

    /**
     * creates a copy of the actor
     * @return returns the actor with the copied data
     */
    public final Actor copy() {
        Actor copy = actorType().create(UUID.randomUUID()).self();
        CompoundTag tag = new CompoundTag();
        serializeForCopying(tag, level().registryAccess());
        copy.deserializeForCopying(tag, level().registryAccess());
        return copy;
    }

    /// serializing

    /**
     * gathers the data to sync with the client.
     */
    public void serializeForClient(CompoundTag tag, HolderLookup.Provider registries) {
        serialize(tag, registries);
    }

    /**
     * Receives the data gathered in {@link Actor#serializeForClient(CompoundTag, HolderLookup.Provider)}.
     */
    public void deserializeForClient(CompoundTag tag, HolderLookup.Provider registries) {
        deserialize(tag, registries);
    }

    /// for saving
    public void serializeForSaving(CompoundTag tag, HolderLookup.Provider registries) {
        serialize(tag, registries);
    }

    /// for saving
    public void deserializeForSaving(CompoundTag tag, HolderLookup.Provider registries) {
        deserialize(tag, registries);
    }

    /// for copying
    public void serializeForCopying(CompoundTag tag, HolderLookup.Provider registries) {
        serialize(tag, registries);
    }

    /// for copying
    public void deserializeForCopying(CompoundTag tag, HolderLookup.Provider registries) {
        deserialize(tag, registries);
    }

    public void serialize(CompoundTag tag, HolderLookup.Provider registries) {
        SerializationUtil.vec3("pos", getPos(), tag);
    }

    public void deserialize(CompoundTag tag, HolderLookup.Provider registries) {
        setPos(SerializationUtil.vec3("pos", tag));
    }

    /// syncing

    /**
     * Sends an update to the client.
     * Can only be requested from the server.
     */
    public final void sendUpdate() {
        if(level() instanceof ServerLevel server) {
            if(synced()) PacketDistributor.sendToPlayersTrackingChunk(server, getChunkPos(), UpdateActorPacket.pack(this));
        } else {
            Babel.LOGGER.warn("Something tried sending an update packet for " + uuid().toString() + " from the client");
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
            Babel.LOGGER.warn("Something tried requesting an update packet for " + uuid().toString() + " from the server");
        }
    }

    /// functionality

    public void onTick() {

    }

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

    public void onRemove() {

    }

    public InteractionResult onPlayerInteraction(Player player, InteractionHand hand, ItemStack stack) {
        return InteractionResult.PASS;
    }

    public boolean canPlayerInteract() {
        return false;
    }

    /// types

    public abstract ActorType<?> actorType();

    public boolean synced() {
        return actorType().synced();
    }

    public abstract SceneType<?, ?> sceneType();

    /// codec
}
