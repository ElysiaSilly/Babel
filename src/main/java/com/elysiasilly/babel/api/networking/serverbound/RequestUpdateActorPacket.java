package com.elysiasilly.babel.api.networking.serverbound;

import com.elysiasilly.babel.api.BabelRegistries;
import com.elysiasilly.babel.api.networking.PayloadHandler;
import com.elysiasilly.babel.api.theatre.Theatre;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.scene.Scene;
import com.elysiasilly.babel.api.theatre.scene.SceneType;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record RequestUpdateActorPacket(UUID uuid, SceneType<?, ?> sceneType) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<RequestUpdateActorPacket> TYPE = PayloadHandler.create(RequestUpdateActorPacket.class);

    public static final StreamCodec<RegistryFriendlyByteBuf, RequestUpdateActorPacket> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, RequestUpdateActorPacket::uuid,
            ByteBufCodecs.registry(BabelRegistries.SCENE_TYPE.key()), RequestUpdateActorPacket::sceneType,
            RequestUpdateActorPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    ///

    public static void run(RequestUpdateActorPacket packet, IPayloadContext context) {
        if(context.player().level() instanceof ServerLevel server) {
            Scene<?> scene = Theatre.get(server, packet.sceneType);
            scene.getActor(packet.uuid).sendUpdate();
        }
    }

    public static RequestUpdateActorPacket pack(Actor actor) {
        return new RequestUpdateActorPacket(actor.uuid(), actor.sceneType());
    }
}
