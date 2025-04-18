package com.elysiasilly.babel.networking.clientbound;

import com.elysiasilly.babel.api.BabelRegistries;
import com.elysiasilly.babel.networking.PayloadHandler;
import com.elysiasilly.babel.api.theatre.Theatre;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.scene.Scene;
import com.elysiasilly.babel.api.theatre.scene.SceneType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record UpdateActorPacket(UUID uuid, SceneType<?, ?> sceneType, CompoundTag tag) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<UpdateActorPacket> TYPE = PayloadHandler.create(UpdateActorPacket.class);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateActorPacket> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, UpdateActorPacket::uuid,
            ByteBufCodecs.registry(BabelRegistries.SCENE_TYPE.key()), UpdateActorPacket::sceneType,
            ByteBufCodecs.TRUSTED_COMPOUND_TAG, UpdateActorPacket::tag,
            UpdateActorPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    ///

    public static void run(UpdateActorPacket packet, IPayloadContext context) {
        Scene<?> scene = Theatre.get(Minecraft.getInstance().level, packet.sceneType);
        scene.getActor(packet.uuid).deserializeForClient(packet.tag, Minecraft.getInstance().level.registryAccess());
    }

    public static UpdateActorPacket pack(Actor actor) {
        CompoundTag tag = new CompoundTag();
        actor.serializeForClient(tag, Minecraft.getInstance().level.registryAccess());
        return new UpdateActorPacket(actor.uuid(), actor.sceneType(), tag);
    }

}
