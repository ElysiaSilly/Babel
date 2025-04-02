package com.elysiasilly.babel.theatre.scene;

import com.elysiasilly.babel.common.actor.TestActor;
import com.elysiasilly.babel.core.registry.BBAttachments;
import com.elysiasilly.babel.theatre.actor.Actor;
import com.elysiasilly.babel.theatre.networking.clientbound.AddActorPacket;
import com.elysiasilly.babel.theatre.networking.clientbound.RemoveActorPacket;
import com.elysiasilly.babel.theatre.networking.serverbound.RequestLoadChunkPacket;
import com.elysiasilly.babel.theatre.storage.ChunkData;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public abstract class ServerScene extends Scene<ServerLevel> {

    protected ServerScene(ServerLevel level) {
        super(level);
    }

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);

        if(actor.getActorType().synced()) {
            PacketDistributor.sendToPlayersTrackingChunk(level(), actor.getChunkPos(), AddActorPacket.pack(actor));
        }
    }

    @Override
    public void removeActor(Actor actor) {
        super.removeActor(actor);

        if(actor.getActorType().synced()) {
            PacketDistributor.sendToPlayersTrackingChunk(level(), actor.getChunkPos(), RemoveActorPacket.pack(actor));
        }
    }

    @Override
    public void loadChunk(ChunkAccess chunk) {
        ChunkData serializedData = chunk.getData(BBAttachments.SCENE_DATA);

        for(ChunkData.ActorData data : serializedData.data()) {
            System.out.println("actor loaded on server!");

            Actor actor = data.actorType().create().self();
            actor.deserializeForSaving(data.tag());
            System.out.println(actor.getPos());
            addActor(actor);
        }
    }

    // yeah no
    public void packChunkForClient(RequestLoadChunkPacket packet, ServerPlayer player) {
        ChunkPos pos = new ChunkPos(packet.chunkPos());

        LevelChunk chunk = level().getChunk(pos.x, pos.z);

        // TODO pack into a single packet because this is horrible :p
        for(int y = chunk.getMinSection(); y < chunk.getMaxSection(); y++) {
            SectionPos section = SectionPos.of(pos, y);
            for (Actor actor : this.storage().getActorsInSection(section.asLong())) {
                PacketDistributor.sendToPlayer(player, AddActorPacket.pack(actor));
            }
        }
    }

    @Override
    public void unloadChunk(ChunkAccess chunk) {
        List<ChunkData.ActorData> data = new ArrayList<>();

        for(int y = chunk.getMinSection(); y < chunk.getMaxSection(); y++) {
            SectionPos pos = SectionPos.of(chunk.getPos(), y);
            for (Actor actor : storage().getActorsInSection(pos)) {
                System.out.println("actor unload on server!");

                CompoundTag tag = new CompoundTag();
                actor.serializeForSaving(tag);
                System.out.println(actor.getPos());
                if(actor instanceof TestActor test) System.out.println(test.startPos);
                data.add(new ChunkData.ActorData(actor.getActorType(), tag));
            }
            this.storage().unloadSection(pos);
        }

        chunk.setData(BBAttachments.SCENE_DATA, new ChunkData(data));
    }
}
