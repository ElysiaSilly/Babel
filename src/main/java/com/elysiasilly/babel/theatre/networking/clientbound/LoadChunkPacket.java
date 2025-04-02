package com.elysiasilly.babel.theatre.networking.clientbound;

import com.elysiasilly.babel.theatre.networking.PayloadHandler;
import com.elysiasilly.babel.theatre.scene.SceneType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LoadChunkPacket(long chunkPos, ResourceKey<Level> dimension, SceneType<?, ?> sceneType, CompoundTag tag) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LoadChunkPacket> TYPE = PayloadHandler.create(LoadChunkPacket.class);

    /*
    public static final StreamCodec<RegistryFriendlyByteBuf, LoadChunkPacket> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, RemoveActorPacket::uuid,
            ByteBufCodecs.registry(BabelRegistries.SCENE_TYPE.key()), RemoveActorPacket::sceneType,
            CustomPacketPayload::new
    );

     */

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    ///

    public static void run(LoadChunkPacket packet, IPayloadContext context) {
    }

    /*
    public static LoadChunkPacket pack(ServerScene<?> scene, RequestChunkPacket requestPacket) {
        CompoundTag tag = new CompoundTag();
        //tag.put()

        return new LoadChunkPacket(requestPacket.chunkPos(), scene.level().dimension(), scene.getSceneType(), );
    }

     */
}
