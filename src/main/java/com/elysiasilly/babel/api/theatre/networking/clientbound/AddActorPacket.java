package com.elysiasilly.babel.api.theatre.networking.clientbound;

import com.elysiasilly.babel.api.BabelRegistries;
import com.elysiasilly.babel.api.theatre.Theatre;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.ActorType;
import com.elysiasilly.babel.api.theatre.networking.PayloadHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record AddActorPacket(UUID uuid, ActorType<?> actorType, CompoundTag tag) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<AddActorPacket> TYPE = PayloadHandler.create(AddActorPacket.class);

    public static final StreamCodec<RegistryFriendlyByteBuf, AddActorPacket> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, AddActorPacket::uuid,
            ByteBufCodecs.registry(BabelRegistries.ACTOR_TYPE.key()), AddActorPacket::actorType,
            ByteBufCodecs.TRUSTED_COMPOUND_TAG, AddActorPacket::tag,
            AddActorPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    ///

    public static void run(AddActorPacket packet, IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();

        Theatre.add(minecraft.level, unpack(packet, minecraft.level));
    }

    public static AddActorPacket pack(Actor actor, Level level) {
        CompoundTag tag = new CompoundTag();
        actor.serializeForClient(tag, level.registryAccess());
        return new AddActorPacket(actor.uuid(), actor.actorType(), tag);
    }

    public static Actor unpack(AddActorPacket packet, Level level) {
        Actor actor = packet.actorType.create(packet.uuid);
        actor.deserializeForClient(packet.tag, level.registryAccess());

        return actor;
    }
}
