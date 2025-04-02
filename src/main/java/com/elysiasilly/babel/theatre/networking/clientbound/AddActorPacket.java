package com.elysiasilly.babel.theatre.networking.clientbound;

import com.elysiasilly.babel.core.registry.BabelRegistries;
import com.elysiasilly.babel.theatre.Theatre;
import com.elysiasilly.babel.theatre.actor.Actor;
import com.elysiasilly.babel.theatre.actor.ActorType;
import com.elysiasilly.babel.theatre.networking.PayloadHandler;
import com.elysiasilly.babel.theatre.storage.LevelSceneAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
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

        Theatre.add(minecraft.level, unpack(packet));
    }

    public static AddActorPacket pack(Actor<?> actor) {
        CompoundTag tag = new CompoundTag();
        actor.serializeForClient(tag);
        return new AddActorPacket(actor.uuid(), actor.getActorType(), tag);
    }

    public static Actor<?> unpack(AddActorPacket packet) {
        Actor<?> actor = packet.actorType.create();
        actor.setUuid(packet.uuid);
        actor.deserializeForClient(packet.tag);

        return actor;
    }
}
