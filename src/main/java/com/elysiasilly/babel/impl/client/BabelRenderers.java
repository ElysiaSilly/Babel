package com.elysiasilly.babel.impl.client;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.client.hud.CycleHudRenderer;
import com.elysiasilly.babel.api.events.ActorRenderersEvent;
import com.elysiasilly.babel.impl.client.render.actor.TankActorRenderer;
import com.elysiasilly.babel.impl.client.render.actor.TestActorRenderer;
import com.elysiasilly.babel.impl.registry.BBActors;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BabelRenderers {

    @SubscribeEvent
    private static void renderers(ActorRenderersEvent.RegisterRenderer event) {
        event.register(BBActors.TEST_ACTOR.get(), TestActorRenderer::new);
        event.register(BBActors.TANK.get(), TankActorRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterGuiLayersEvent(RegisterGuiLayersEvent event) {
        event.registerBelowAll(ResourceLocation.fromNamespaceAndPath(Babel.MODID, "cycle_item_element"), CycleHudRenderer.LAYER);
    }
}
