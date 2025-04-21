package com.elysiasilly.babel.util.utils;

import com.elysiasilly.babel.util.resource.RGBA;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public class CodecUtil {

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

    ///

    public static <B extends ByteBuf, L, R> StreamCodec<B, Pair<L, R>> pairStreamCodec(final StreamCodec<? super B, L> leftCodec, final StreamCodec<? super B, R> rightCodec) {
        return new StreamCodec<>() {
            @Override
            public Pair<L, R> decode(B buffer) {
                return new Pair<>(leftCodec.decode(buffer), rightCodec.decode(buffer));
            }

            @Override
            public void encode(B buffer, Pair<L, R> value) {
                leftCodec.encode(buffer, value.getFirst());
                rightCodec.encode(buffer, value.getSecond());
            }
        };
    }

    /*
    public static <E extends Enum<E>> Codec<E> enumCodec(E enum) {
    }

    public static <E extends Enum<E>> Codec<E> enumStreamCodec(E enum) {
    }
    */
}
