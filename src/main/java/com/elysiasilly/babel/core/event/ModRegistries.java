package com.elysiasilly.babel.core.event;

import com.elysiasilly.babel.core.Babel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModRegistries {

    @SubscribeEvent
    static void registries(final NewRegistryEvent event) {
        event.register(Babel.registries.INTERACTABLE_MANAGER);
    }
}
