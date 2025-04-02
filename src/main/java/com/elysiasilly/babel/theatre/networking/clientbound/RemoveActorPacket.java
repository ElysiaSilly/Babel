package com.elysiasilly.babel.theatre.networking.clientbound;

import com.elysiasilly.babel.core.registry.BabelRegistries;
import com.elysiasilly.babel.theatre.Theatre;
import com.elysiasilly.babel.theatre.actor.Actor;
import com.elysiasilly.babel.theatre.networking.PayloadHandler;
import com.elysiasilly.babel.theatre.scene.Scene;
import com.elysiasilly.babel.theatre.scene.SceneType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record RemoveActorPacket(UUID uuid, SceneType<?, ?> sceneType) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<RemoveActorPacket> TYPE = PayloadHandler.create(RemoveActorPacket.class);

    public static final StreamCodec<RegistryFriendlyByteBuf, RemoveActorPacket> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, RemoveActorPacket::uuid,
            ByteBufCodecs.registry(BabelRegistries.SCENE_TYPE.key()), RemoveActorPacket::sceneType,
            RemoveActorPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    ///

    public static void run(RemoveActorPacket packet, IPayloadContext context) {
        Scene<?> scene = Theatre.get(Minecraft.getInstance().level, packet.sceneType);
        scene.removeActor(packet.uuid);
    }

    public static RemoveActorPacket pack(Actor actor) {
        return new RemoveActorPacket(actor.uuid(), actor.getSceneType());
    }
}
