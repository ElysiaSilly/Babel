package com.elysiasilly.babel.theatre.scene;

import com.elysiasilly.babel.theatre.actor.Actor;
import com.elysiasilly.babel.theatre.networking.clientbound.LoadChunkPacket;
import com.elysiasilly.babel.theatre.networking.serverbound.RequestChunkPacket;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class ClientScene<A extends Actor<?>> extends Scene<A, ClientLevel> {

    protected ClientScene(ClientLevel level) {
        super(level);
    }

    @Override
    public void loadChunk(ChunkAccess chunkAccess) {

        System.out.println("Loaded chunk on client");

        PacketDistributor.sendToServer(RequestChunkPacket.pack(chunkAccess.getPos(), this));
    }

    public void loadChunkFromPacket(LoadChunkPacket packet) {

    }

    @Override
    public void unloadChunk(ChunkAccess chunk) {

        System.out.println("Unloaded chunk on client");

        for(int y = chunk.getMinSection(); y < chunk.getMaxSection(); y++) {
            SectionPos pos = SectionPos.of(chunk.getPos(), y);
            this.storage().unloadSection(pos);
        }
    }
}
