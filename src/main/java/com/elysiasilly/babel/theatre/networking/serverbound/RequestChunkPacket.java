package com.elysiasilly.babel.theatre.networking.serverbound;

import com.elysiasilly.babel.core.registry.BabelRegistries;
import com.elysiasilly.babel.theatre.Theatre;
import com.elysiasilly.babel.theatre.networking.PayloadHandler;
import com.elysiasilly.babel.theatre.scene.Scene;
import com.elysiasilly.babel.theatre.scene.SceneType;
import com.elysiasilly.babel.theatre.scene.ServerScene;
import com.elysiasilly.babel.theatre.storage.LevelSceneAttachment;
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

public record RequestChunkPacket(long chunkPos, ResourceKey<Level> dimension, SceneType<?, ?> sceneType) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<RequestChunkPacket> TYPE = PayloadHandler.create(RequestChunkPacket.class);

    public static final StreamCodec<RegistryFriendlyByteBuf, RequestChunkPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, RequestChunkPacket::chunkPos,
            ResourceKey.streamCodec(Registries.DIMENSION), RequestChunkPacket::dimension,
            ByteBufCodecs.registry(BabelRegistries.SCENE_TYPE.key()), RequestChunkPacket::sceneType,
            RequestChunkPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    ///

    public static void run(RequestChunkPacket packet, IPayloadContext context) {
        if(context.player() instanceof ServerPlayer player) {
            Scene<?, ?> scene = Theatre.get(packet.dimension, packet.sceneType);
            if(scene instanceof ServerScene<?> serverScene) {
                serverScene.packChunkForClient(packet, player);
            }
        }
    }

    public static RequestChunkPacket pack(ChunkPos pos, Scene<?, ?> scene) {
        return new RequestChunkPacket(pos.toLong(), scene.level().dimension(), scene.getSceneType());
    }
}
