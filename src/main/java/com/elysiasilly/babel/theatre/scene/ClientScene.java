package com.elysiasilly.babel.theatre.scene;

import com.elysiasilly.babel.theatre.networking.serverbound.RequestLoadChunkPacket;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.SectionPos;
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
        for(int y = chunk.getMinSection(); y < chunk.getMaxSection(); y++) {
            SectionPos pos = SectionPos.of(chunk.getPos(), y);
            this.storage().unloadSection(pos);
        }
    }
}
