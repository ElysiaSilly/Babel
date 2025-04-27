package com.elysiasilly.babel.util;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.registry.ActorType;
import com.elysiasilly.babel.core.BBRegistries;
import com.elysiasilly.babel.util.type.RGBA;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.joml.Vector3d;

import java.util.List;
import java.util.UUID;

public class UtilsCodec {

    /// VECTOR3D

    public static final Codec<Vector3d> VECTOR3D_CODEC = Codec.DOUBLE.listOf().comapFlatMap(list -> Util.fixedSize(list, 3)
            .map(decode -> new Vector3d(decode.get(0), decode.get(1), decode.get(2))), encode -> List.of(encode.x, encode.y, encode.z)
    );

    public static final StreamCodec<ByteBuf, Vector3d> VECTOR3D_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public Vector3d decode(ByteBuf buffer) {
            return new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        }

        @Override
        public void encode(ByteBuf buffer, Vector3d value) {
            buffer.writeDouble(value.x);
            buffer.writeDouble(value.y);
            buffer.writeDouble(value.z);
        }
    };

    /// RGBA

    public static final Codec<RGBA> RGBA_CODEC = Codec.INT.listOf().comapFlatMap(list -> Util.fixedSize(list, 4)
            .map(decode -> new RGBA(decode.get(0), decode.get(1), decode.get(2), decode.get(4))), encode -> List.of(encode.r(), encode.g(), encode.b(), encode.a())
    );

    public static final StreamCodec<ByteBuf, RGBA> RGBA_STREAM_CODEC = new StreamCodec<>() {

        @Override
        public RGBA decode(ByteBuf buffer) {
            return new RGBA(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt());
        }

        @Override
        public void encode(ByteBuf buffer, RGBA rgba) {
            buffer.writeInt(rgba.r());
            buffer.writeInt(rgba.g());
            buffer.writeInt(rgba.b());
            buffer.writeInt(rgba.a());
        }
    };

    /// ACTOR

    public static final Codec<Actor.Data> ACTOR_DATA_CODEC = RecordCodecBuilder.create(builder -> builder.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(Actor.Data::uuid),
            VECTOR3D_CODEC.fieldOf("pos").forGetter(Actor.Data::position),
            BBRegistries.ACTOR_TYPE.get().byNameCodec().fieldOf("type").forGetter(Actor.Data::type),
            CompoundTag.CODEC.fieldOf("tag").forGetter(Actor.Data::tag)
    ).apply(builder, Actor.Data::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Actor> ACTOR_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buffer, Actor value) {
            UUIDUtil.STREAM_CODEC.encode(buffer, value.uuid());
            ByteBufCodecs.registry(BBRegistries.ACTOR_TYPE.key()).encode(buffer, value.actorType());
            VECTOR3D_STREAM_CODEC.encode(buffer, value.pos());
            ByteBufCodecs.TRUSTED_COMPOUND_TAG.encode(buffer, value.serializeForClient(new CompoundTag(), buffer.registryAccess()));
        }

        @Override
        public Actor decode(RegistryFriendlyByteBuf buffer) {
            return actor(
                    UUIDUtil.STREAM_CODEC.decode(buffer),
                    ByteBufCodecs.registry(BBRegistries.ACTOR_TYPE.key()).decode(buffer),
                    VECTOR3D_STREAM_CODEC.decode(buffer),
                    ByteBufCodecs.TRUSTED_COMPOUND_TAG.decode(buffer), buffer.registryAccess()
            );
        }
    };

    private static Actor actor(UUID uuid, ActorType<?> actorType, Vector3d position, CompoundTag tag, RegistryAccess registryAccess) {
        Actor actor = actorType.create(position, uuid);
        actor.deserializeFromServer(tag, registryAccess);
        return actor;
    }
}
