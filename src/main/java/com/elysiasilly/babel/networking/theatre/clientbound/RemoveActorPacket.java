package com.elysiasilly.babel.networking.theatre.clientbound;

import com.elysiasilly.babel.api.theatre.Theatre;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.ActorRemovalReason;
import com.elysiasilly.babel.api.theatre.scene.Scene;
import com.elysiasilly.babel.api.theatre.scene.registry.SceneType;
import com.elysiasilly.babel.core.BBRegistries;
import com.elysiasilly.babel.networking.PayloadHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record RemoveActorPacket(UUID uuid, SceneType<?, ?> sceneType, ActorRemovalReason reason) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<RemoveActorPacket> TYPE = PayloadHandler.create(RemoveActorPacket.class);

    public static final StreamCodec<RegistryFriendlyByteBuf, RemoveActorPacket> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, RemoveActorPacket::uuid,
            ByteBufCodecs.registry(BBRegistries.SCENE_TYPE.key()), RemoveActorPacket::sceneType,
            NeoForgeStreamCodecs.enumCodec(ActorRemovalReason.class), RemoveActorPacket::reason,
            RemoveActorPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    ///

    public static void run(RemoveActorPacket packet, IPayloadContext context) {
        Scene<?> scene = Theatre.get(Minecraft.getInstance().level, packet.sceneType);
        scene.removeActor(packet.uuid, packet.reason);
    }

    public static RemoveActorPacket pack(Actor actor, ActorRemovalReason reason) {
        return new RemoveActorPacket(actor.uuid(), actor.sceneType(), reason);
    }
}
