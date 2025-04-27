package com.elysiasilly.babel.networking.theatre.clientbound;

import com.elysiasilly.babel.api.theatre.Theatre;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.networking.PayloadHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateActorPacket(Actor actor) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<UpdateActorPacket> TYPE = PayloadHandler.create(UpdateActorPacket.class);


    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateActorPacket> CODEC = StreamCodec.composite(
            Actor.STREAM_CODEC, UpdateActorPacket::actor,
            UpdateActorPacket::new
    );

    public static void run(UpdateActorPacket packet, IPayloadContext context) {
        Actor decodedActor = packet.actor();
        Level level = context.player().level();

        Actor actor = Theatre.get(level, decodedActor.sceneType()).getActor(decodedActor.uuid());

        actor.pos(decodedActor.pos());
        actor.deserializeFromServer(decodedActor.serializeForClient(new CompoundTag(), level.registryAccess()), level.registryAccess());
        actor.markDirty();
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
