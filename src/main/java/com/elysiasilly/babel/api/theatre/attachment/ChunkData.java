package com.elysiasilly.babel.api.theatre.attachment;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record ChunkData(List<Actor.Data> data) {

    public static final ChunkData EMPTY = new ChunkData(List.of());

    public static final Codec<ChunkData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Actor.ACTOR_DATA_CODEC.listOf().fieldOf("l").forGetter(ChunkData::data)
    ).apply(instance, ChunkData::new));
}
