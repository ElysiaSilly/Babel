package com.elysiasilly.babel.api.theatre.scene;

import com.elysiasilly.babel.networking.theatre.serverbound.RequestLoadChunkPacket;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class ClientScene extends Scene<ClientLevel> {

    protected ClientScene(ClientLevel level) {
        super(level);
    }

    @Override
    public void loadChunk(ChunkAccess chunkAccess) {
        PacketDistributor.sendToServer(RequestLoadChunkPacket.pack(chunkAccess.getPos(), this));
    }

    @Override
    public void unloadChunk(ChunkAccess chunk) {
        storage().unload(chunk);
    }
}
