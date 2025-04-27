package com.elysiasilly.babel.networking.theatre.clientbound;

import com.elysiasilly.babel.api.theatre.Theatre;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.networking.PayloadHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record AddActorsPacket(List<Actor> actors) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<AddActorsPacket> TYPE = PayloadHandler.create(AddActorsPacket.class);

    public static final StreamCodec<RegistryFriendlyByteBuf, AddActorsPacket> CODEC = StreamCodec.composite(
            Actor.STREAM_CODEC.apply(ByteBufCodecs.list()), AddActorsPacket::actors,
            AddActorsPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void run(AddActorsPacket packet, IPayloadContext context) {
        for(Actor actor : packet.actors()) {
            Theatre.add(Minecraft.getInstance().level, actor);
        }
    }
}
