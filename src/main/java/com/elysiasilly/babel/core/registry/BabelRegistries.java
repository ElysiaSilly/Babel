package com.elysiasilly.babel.core.registry;

import com.elysiasilly.babel.common.interactible.InteractableManager;
import com.elysiasilly.babel.core.Babel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD)
public class BabelRegistries {

    @SubscribeEvent
    static void registries(final NewRegistryEvent event) {
        event.register(INTERACTABLE_MANAGER);
    }

    public static final Registry<InteractableManager<?>> INTERACTABLE_MANAGER = new Key<InteractableManager<?>>("interactable_manager").create();

    private record Key<T> (String ID) {

        public Registry<T> create() {
            return new RegistryBuilder<>(get()).create();
        }

        public ResourceKey<Registry<T>> get() {
            return ResourceKey.createRegistryKey(Babel.location(ID));
        }
    }
}
