package com.elysiasilly.babel.api.theatre.storage;

import com.elysiasilly.babel.api.BabelRegistries;
import com.elysiasilly.babel.api.theatre.actor.ActorType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public record ChunkData(List<ActorData> data) {

    public static final ChunkData EMPTY = new ChunkData(List.of());

    public record ActorData(ActorType<?> actorType, CompoundTag tag) {
        public static final Codec<ActorData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BabelRegistries.ACTOR_TYPE.byNameCodec().fieldOf("t").forGetter(i -> i.actorType),
                CompoundTag.CODEC.fieldOf("c").forGetter(i -> i.tag)
        ).apply(instance, ActorData::new));
    }

    public static final Codec<ChunkData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ActorData.CODEC.listOf().fieldOf("l").forGetter(i -> i.data)
    ).apply(instance, ChunkData::new));
}
