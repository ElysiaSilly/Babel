package com.elysiasilly.babel.impl.common.actor;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.theatre.actor.ActorEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Event {

    @SubscribeEvent
    private static void actorsLoad(ActorEvents.ActorLoaded event) {
        System.out.println(event.getActor().position);
    }

    @SubscribeEvent
    private static void actorsUnload(ActorEvents.ActorUnloaded event) {
        System.out.println(event.getActor().position);
    }

}
