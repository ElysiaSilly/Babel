package com.elysiasilly.babel.networking.clientbound;

import com.elysiasilly.babel.networking.PayloadHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CycleDBIPacket(int channel, int index) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<CycleDBIPacket> TYPE = PayloadHandler.create(CycleDBIPacket.class);

    public static final StreamCodec<ByteBuf, CycleDBIPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, CycleDBIPacket::channel,
            ByteBufCodecs.INT, CycleDBIPacket::index,
            CycleDBIPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
