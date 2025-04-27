package com.elysiasilly.babel.networking.theatre.clientbound;

import com.elysiasilly.babel.api.theatre.Theatre;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.networking.PayloadHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AddActorPacket(Actor actor) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<AddActorPacket> TYPE = PayloadHandler.create(AddActorPacket.class);

    public static final StreamCodec<RegistryFriendlyByteBuf, AddActorPacket> CODEC = StreamCodec.composite(
            Actor.STREAM_CODEC, AddActorPacket::actor,
            AddActorPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void run(AddActorPacket packet, IPayloadContext context) {
        Theatre.add(Minecraft.getInstance().level, packet.actor);
    }
}
