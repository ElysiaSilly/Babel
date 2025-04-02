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

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class Actor<S extends Scene> {

    private UUID uuid = UUID.randomUUID();
    private S scene;

    private boolean dirty = true, removed = false;

    public Vec3 position = Vec3.ZERO;

    /// position

    public Vec3 getPos() {
        return position;
    }

    public void setPos(Vec3 pos) {
        this.position = pos;
    }

    //public void movePos(Vector3f chunkPos) {
    //    this.position = this.position.add(chunkPos);
    //}

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

    public S getScene() {
        return this.scene;
    }

    public void setScene(S scene) {
        this.scene = scene;
    }

    public UUID uuid() {
        return this.uuid;
    }

    // don't randomly reassign this unless you fancy yourself desyncs and other funniness probably :pray:
    // todo : ^ lol
    public void setUuid(UUID id) {
        this.uuid = id;
    }

    public void destroy() {
        this.scene.removeActor(this);
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
    public <A extends Actor<?>> A self() {
        return (A) this;
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

    public void serialize(CompoundTag tag) {
        MCUtil.Serialize.vec3("pos", getPos(), tag);
    }

    public void deserialize(CompoundTag tag) {
        setPos(MCUtil.Serialize.vec3("pos", tag));
    }

    public void markDirty() {

    }

    public void sendUpdate() {
        if(level() instanceof ServerLevel server) {
            if(getActorType().synced()) PacketDistributor.sendToPlayersTrackingChunk(server, getChunkPos(), UpdateActorPacket.pack(this));
        } else {
            Babel.LOGGER.warn("Something tried sending an update packet from the client");
        }
    }

    public void requestUpdate() {
        if(level() instanceof ClientLevel) {
            if(getActorType().synced()) PacketDistributor.sendToServer(RequestUpdateActorPacket.pack(this));
        } else {
            Babel.LOGGER.warn("Something tried requesting an update packet from the server");
        }
    }

    /// functionality

    public void tick() {}

    public boolean canTick() {
        return false;
    }

    public void onAdd() {}

    public void onRemove() {}

    public InteractionResult onPlayerInteraction(Player player, InteractionHand hand, ItemStack stack) {
        return InteractionResult.PASS;
    }

    /// types

    public abstract ActorType<?> getActorType();

    public abstract SceneType<?, ?> getSceneType();
}
