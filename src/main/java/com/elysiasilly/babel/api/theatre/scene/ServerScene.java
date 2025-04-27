package com.elysiasilly.babel.api.theatre.scene;

import com.elysiasilly.babel.api.events.ActorEvents;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.ActorRemovalReason;
import com.elysiasilly.babel.api.theatre.attachment.ChunkData;
import com.elysiasilly.babel.core.registry.BBAttachments;
import com.elysiasilly.babel.networking.theatre.clientbound.AddActorPacket;
import com.elysiasilly.babel.networking.theatre.clientbound.AddActorsPacket;
import com.elysiasilly.babel.networking.theatre.clientbound.RemoveActorPacket;
import com.elysiasilly.babel.networking.theatre.serverbound.RequestLoadChunkPacket;
import com.elysiasilly.babel.util.UtilsDev;
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
    public boolean addActor(Actor actor) {
        boolean flag = super.addActor(actor);
        if(actor.synced() && flag) {
            PacketDistributor.sendToPlayersTrackingChunk(level(), actor.chunkPos(), new AddActorPacket(actor));
        }
        return flag;
    }

    @Override
    public boolean removeActor(Actor actor, ActorRemovalReason reason) {
        boolean flag = super.removeActor(actor, reason);
        if(actor.synced() && flag) {
            PacketDistributor.sendToPlayersTrackingChunk(level(), actor.chunkPos(), RemoveActorPacket.pack(actor, reason));
        }
        return flag;
    }

    @Override
    public void loadChunk(ChunkAccess chunk) {
        if(chunk.hasData(BBAttachments.SCENE_DATA)) {
            ChunkData serializedData = chunk.getData(BBAttachments.SCENE_DATA);

            for(Actor.Data data : serializedData.data()) {
                Actor actor = data.parse(level());
                UtilsDev.postGameEvent(new ActorEvents.Loaded(actor, this, chunk));
                addActor(actor);
            }
        }
    }

    public void packChunkForClient(RequestLoadChunkPacket packet, ServerPlayer player) {
        Collection<Actor> actors = actorsInChunk(new ChunkPos(packet.chunkPos()));
        if(!actors.isEmpty()) PacketDistributor.sendToPlayer(player, new AddActorsPacket(actors.stream().toList()));

    }

    @Override
    public void unloadChunk(ChunkAccess chunk) {
        //Babel.LOGGER.info("try unloading chunk!");

        if(!actorsInChunk(chunk.getPos()).isEmpty()) {
            List<Actor.Data> data = new ArrayList<>();

            for(Actor actor : actorsInChunk(chunk.getPos())) {
                UtilsDev.postGameEvent(new ActorEvents.Unloaded(actor, this, chunk));
                data.add(actor.toData(level()));
            }

            //Babel.LOGGER.info("chunk has been unloaded!");

            storage().unload(chunk);

            chunk.setData(BBAttachments.SCENE_DATA, new ChunkData(data));
        }
    }
}
