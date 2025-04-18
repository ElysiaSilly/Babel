package com.elysiasilly.babel.impl.common.actor;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.events.ActorEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Event {

    @SubscribeEvent
    private static void actorsLoad(ActorEvents.Loaded event) {
        System.out.println(event.actor().position);
    }

    @SubscribeEvent
    private static void actorsUnload(ActorEvents.Unloaded event) {
        System.out.println(event.actor().position);
    }

}
