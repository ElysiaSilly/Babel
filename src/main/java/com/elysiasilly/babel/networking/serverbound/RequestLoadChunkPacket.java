package com.elysiasilly.babel.networking.serverbound;

import com.elysiasilly.babel.api.theatre.Theatre;
import com.elysiasilly.babel.api.theatre.scene.Scene;
import com.elysiasilly.babel.api.theatre.scene.SceneType;
import com.elysiasilly.babel.api.theatre.scene.ServerScene;
import com.elysiasilly.babel.core.BBRegistries;
import com.elysiasilly.babel.networking.PayloadHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RequestLoadChunkPacket(long chunkPos, ResourceKey<Level> dimension, SceneType<?, ?> sceneType) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<RequestLoadChunkPacket> TYPE = PayloadHandler.create(RequestLoadChunkPacket.class);

    public static final StreamCodec<RegistryFriendlyByteBuf, RequestLoadChunkPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, RequestLoadChunkPacket::chunkPos,
            ResourceKey.streamCodec(Registries.DIMENSION), RequestLoadChunkPacket::dimension,
            ByteBufCodecs.registry(BBRegistries.SCENE_TYPE.key()), RequestLoadChunkPacket::sceneType,
            RequestLoadChunkPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    ///

    public static void run(RequestLoadChunkPacket packet, IPayloadContext context) {
        if(context.player() instanceof ServerPlayer player) {
            Scene<?> scene = Theatre.get(packet.dimension, packet.sceneType);
            if(scene instanceof ServerScene serverScene) {
                serverScene.packChunkForClient(packet, player);
            }
        }
    }

    public static RequestLoadChunkPacket pack(ChunkPos pos, Scene<?> scene) {
        return new RequestLoadChunkPacket(pos.toLong(), scene.dimension(), scene.getSceneType());
    }
}
