package com.elysiasilly.babel.theatre.actor;

import com.elysiasilly.babel.core.Babel;
import com.elysiasilly.babel.theatre.networking.clientbound.UpdateActorPacket;
import com.elysiasilly.babel.theatre.networking.serverbound.RequestUpdateActorPacket;
import com.elysiasilly.babel.theatre.scene.Scene;
import com.elysiasilly.babel.theatre.scene.SceneType;
import com.elysiasilly.babel.util.MCUtil;
import net.minecraft.client.multiplayer.ClientLevel;
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

    protected Actor() {
        this.uuid = UUID.randomUUID();
    }

    protected Actor(UUID uuid) {
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
        return this.scene.level();
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
        this.scene.removeActor(this);
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

    // lmao
    public <A extends Actor> A self() {
        return (A) this;
    }

    public final Actor copy(UUID uuid) {
        Actor copy = getActorType().create(uuid).self();
        CompoundTag tag = new CompoundTag();
        copy.serializeForCopying(tag);
        copy.deserializeForCopying(tag);
        return copy;
    }

    /// serializing

    /// for syncing with the client
    public void serializeForClient(CompoundTag tag) {
        serialize(tag);
    }

    /// for syncing with the client
    public void deserializeForClient(CompoundTag tag) {
        deserialize(tag);
    }

    /// for saving
    public void serializeForSaving(CompoundTag tag) {
        serialize(tag);
    }

    /// for saving
    public void deserializeForSaving(CompoundTag tag) {
        deserialize(tag);
    }

    /// for copying
    public void serializeForCopying(CompoundTag tag) {
        serialize(tag);
    }

    /// for copying
    public void deserializeForCopying(CompoundTag tag) {
        deserialize(tag);
    }

    public void serialize(CompoundTag tag) {
        MCUtil.Serialize.vec3("pos", getPos(), tag);
    }

    public void deserialize(CompoundTag tag) {
        setPos(MCUtil.Serialize.vec3("pos", tag));
    }

    /// syncing

    public final void sendUpdate() {
        if(level() instanceof ServerLevel server) {
            if(getActorType().synced()) PacketDistributor.sendToPlayersTrackingChunk(server, getChunkPos(), UpdateActorPacket.pack(this));
        } else {
            Babel.LOGGER.warn("Something tried sending an update packet from the client");
        }
    }

    public final void requestUpdate() {
        if(level() instanceof ClientLevel) {
            if(getActorType().synced()) PacketDistributor.sendToServer(RequestUpdateActorPacket.pack(this));
        } else {
            Babel.LOGGER.warn("Something tried requesting an update packet from the server");
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

    public abstract ActorType<?> getActorType();

    public abstract SceneType<?, ?> getSceneType();
}
