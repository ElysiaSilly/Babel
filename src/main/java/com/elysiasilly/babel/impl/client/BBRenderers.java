package com.elysiasilly.babel.impl.client;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.client.hud.CycleHudRenderer;
import com.elysiasilly.babel.api.dbi.hud.DBIHudRenderer;
import com.elysiasilly.babel.api.events.ActorRenderersEvent;
import com.elysiasilly.babel.core.registry.BBActors;
import com.elysiasilly.babel.impl.client.render.actor.FlamethrowerActorRenderer;
import com.elysiasilly.babel.impl.client.render.actor.TestActorRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BBRenderers {

    @SubscribeEvent
    private static void renderers(ActorRenderersEvent.RegisterRenderer event) {
        event.register(BBActors.TEST_ACTOR.get(), TestActorRenderer::new);
        event.register(BBActors.FLAMETHROWER.get(), FlamethrowerActorRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterGuiLayersEvent(RegisterGuiLayersEvent event) {
        event.registerBelowAll(ResourceLocation.fromNamespaceAndPath(Babel.MODID, "cycle_item_element"), CycleHudRenderer.LAYER);
        event.registerBelowAll(ResourceLocation.fromNamespaceAndPath(Babel.MODID, "dbi"), DBIHudRenderer.INSTANCE);

    }
}
