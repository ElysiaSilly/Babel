package com.elysiasilly.babel.impl.client;

import com.elysiasilly.babel.api.theatre.actor.render.ActorRenderersEvent;
import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.impl.client.render.actor.TankActorRenderer;
import com.elysiasilly.babel.impl.common.actor.TankActor;
import com.elysiasilly.babel.impl.registry.BBActors;
import com.elysiasilly.babel.impl.client.render.actor.TestActorRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BabelRenderers {

    @SubscribeEvent
    private static void renderers(ActorRenderersEvent event) {
        event.registerRenderer(BBActors.TEST_ACTOR.get(), TestActorRenderer::new);
        event.registerRenderer(BBActors.TANK.get(), TankActorRenderer::new);
    }
}
