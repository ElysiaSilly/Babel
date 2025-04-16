package com.elysiasilly.babel.api.theatre.scene;

import com.elysiasilly.babel.api.networking.clientbound.AddActorPacket;
import com.elysiasilly.babel.api.networking.clientbound.RemoveActorPacket;
import com.elysiasilly.babel.api.networking.serverbound.RequestLoadChunkPacket;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.ActorEvents;
import com.elysiasilly.babel.api.theatre.storage.ChunkData;
import com.elysiasilly.babel.impl.registry.BBAttachments;
import com.elysiasilly.babel.util.utils.DevUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ServerScene extends Scene<ServerLevel> {

    protected ServerScene(ServerLevel level) {
        super(level);
    }

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
        if(actor.synced()) {
            PacketDistributor.sendToPlayersTrackingChunk(level(), actor.getChunkPos(), AddActorPacket.pack(actor, level()));
        }
    }

    @Override
    public void removeActor(Actor actor) {
        super.removeActor(actor);
        if(actor.synced()) {
            PacketDistributor.sendToPlayersTrackingChunk(level(), actor.getChunkPos(), RemoveActorPacket.pack(actor));
        }
    }

    @Override
    public void loadChunk(ChunkAccess chunk) {
        ChunkData serializedData = chunk.getData(BBAttachments.SCENE_DATA);

        for(ChunkData.ActorData data : serializedData.data()) {
            Actor actor = data.actorType().create().self();
            actor.deserializeForSaving(data.tag(), level().registryAccess());
            DevUtil.postEvent(new ActorEvents.ActorLoaded(actor, this));
            addActor(actor);
        }
    }

    // TODO pack into a single packet because this is horrible :p
    public void packChunkForClient(RequestLoadChunkPacket packet, ServerPlayer player) {
        Collection<Actor> actors = storage().getActorsInChunk(new ChunkPos(packet.chunkPos()));
        if(!actors.isEmpty()) {
            for(Actor actor : actors) {
                PacketDistributor.sendToPlayer(player, AddActorPacket.pack(actor, level()));
            }
        }
    }

    @Override
    public void unloadChunk(ChunkAccess chunk) {
        List<ChunkData.ActorData> data = new ArrayList<>();

        for(Actor actor : storage().getActorsInChunk(chunk.getPos())) {
            DevUtil.postEvent(new ActorEvents.ActorUnloaded(actor, this));

            CompoundTag tag = new CompoundTag();
            actor.serializeForSaving(tag, level().registryAccess());
            data.add(new ChunkData.ActorData(actor.actorType(), tag));
        }

        this.storage().unloadChunk(chunk);

        chunk.setData(BBAttachments.SCENE_DATA, new ChunkData(data));
    }
}
