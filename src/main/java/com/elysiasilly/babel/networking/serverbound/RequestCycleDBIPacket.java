package com.elysiasilly.babel.networking.serverbound;

import com.elysiasilly.babel.networking.PayloadHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RequestCycleDBIPacket(int channel) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<RequestCycleDBIPacket> TYPE = PayloadHandler.create(RequestCycleDBIPacket.class);

    public static final StreamCodec<ByteBuf, RequestCycleDBIPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, RequestCycleDBIPacket::channel,
            RequestCycleDBIPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
