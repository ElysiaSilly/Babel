package com.elysiasilly.babel.common.actor;

import com.elysiasilly.babel.core.registry.BBActors;
import com.elysiasilly.babel.theatre.actor.render.ActorRenderersEvent;
import com.elysiasilly.babel.core.Babel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ActorRenderers {

    @SubscribeEvent
    private static void register(ActorRenderersEvent event) {
        event.registerRenderer(BBActors.TEST_ACTOR.get(), TestRenderer::new);
    }
}
