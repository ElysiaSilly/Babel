package com.elysiasilly.babel.networking;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.client.Keybindings;
import com.elysiasilly.babel.networking.dbi.clientbound.CycleDBIPacket;
import com.elysiasilly.babel.networking.dbi.serverbound.RequestCycleDBIPacket;
import com.elysiasilly.babel.networking.theatre.clientbound.AddActorPacket;
import com.elysiasilly.babel.networking.theatre.clientbound.AddActorsPacket;
import com.elysiasilly.babel.networking.theatre.clientbound.RemoveActorPacket;
import com.elysiasilly.babel.networking.theatre.clientbound.UpdateActorPacket;
import com.elysiasilly.babel.networking.theatre.serverbound.RequestLoadChunkPacket;
import com.elysiasilly.babel.networking.theatre.serverbound.RequestUpdateActorPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD)
public class PayloadHandler {

    @SubscribeEvent
    private static void onRegisterPayLoadHandlers(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        // clientbound
        registrar.playToClient(RemoveActorPacket.TYPE, RemoveActorPacket.CODEC, RemoveActorPacket::run);
        registrar.playToClient(AddActorPacket.TYPE, AddActorPacket.CODEC, AddActorPacket::run);
        registrar.playToClient(AddActorsPacket.TYPE, AddActorsPacket.CODEC, AddActorsPacket::run);
        registrar.playToClient(UpdateActorPacket.TYPE, UpdateActorPacket.CODEC, UpdateActorPacket::run);

        registrar.playToClient(CycleDBIPacket.TYPE, CycleDBIPacket.CODEC, Keybindings::cycleNext);

        // serverbound
        registrar.playToServer(RequestUpdateActorPacket.TYPE, RequestUpdateActorPacket.CODEC, RequestUpdateActorPacket::run);
        registrar.playToServer(RequestLoadChunkPacket.TYPE, RequestLoadChunkPacket.CODEC, RequestLoadChunkPacket::run);

        registrar.playToServer(RequestCycleDBIPacket.TYPE, RequestCycleDBIPacket.CODEC, Keybindings::cycleNext);
    }

    // forgive me
    public static <S extends CustomPacketPayload> CustomPacketPayload.Type<S> create(Class<S> clazz) {
        return new CustomPacketPayload.Type<>(Babel.location(clazz.getSimpleName().toLowerCase()));
    }
}
