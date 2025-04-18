package com.elysiasilly.babel.networking;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.networking.clientbound.AddActorPacket;
import com.elysiasilly.babel.networking.clientbound.RemoveActorPacket;
import com.elysiasilly.babel.networking.clientbound.UpdateActorPacket;
import com.elysiasilly.babel.networking.serverbound.RequestLoadChunkPacket;
import com.elysiasilly.babel.networking.serverbound.RequestUpdateActorPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD)
public class PayloadHandler {

    @SubscribeEvent
    public static void onRegisterPayLoadHandlers(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        // clientbound
        registrar.playToClient(RemoveActorPacket.TYPE, RemoveActorPacket.CODEC, RemoveActorPacket::run);
        registrar.playToClient(AddActorPacket.TYPE, AddActorPacket.CODEC, AddActorPacket::run);
        registrar.playToClient(UpdateActorPacket.TYPE, UpdateActorPacket.CODEC, UpdateActorPacket::run);

        // serverbound
        registrar.playToServer(RequestUpdateActorPacket.TYPE, RequestUpdateActorPacket.CODEC, RequestUpdateActorPacket::run);
        registrar.playToServer(RequestLoadChunkPacket.TYPE, RequestLoadChunkPacket.CODEC, RequestLoadChunkPacket::run);
    }

    // forgive me
    public static <S extends CustomPacketPayload> CustomPacketPayload.Type<S> create(Class<S> clazz) {
        return new CustomPacketPayload.Type<>(Babel.location(clazz.getSimpleName().toLowerCase()));
    }
}
