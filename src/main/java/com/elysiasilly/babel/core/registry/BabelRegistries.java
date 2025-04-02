package com.elysiasilly.babel.core.registry;

import com.elysiasilly.babel.theatre.actor.ActorType;
import com.elysiasilly.babel.theatre.scene.SceneType;
import com.elysiasilly.babel.client.lexicon.element.LexiconElement;
import com.elysiasilly.babel.client.lexicon.layer.LexiconPageLayer;
import com.elysiasilly.babel.client.lexicon.page.LexiconPage;
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
        event.register(SCENE_TYPE);
        event.register(ACTOR_TYPE);
    }

    public static class Lexicon {

        public static final Registry<LexiconElement> ELEMENT = new Registrar<LexiconElement>("lexicon_element", false).create();
        public static final Registry<LexiconPageLayer> PAGE_LAYER = new Registrar<LexiconPageLayer>("lexicon_page_layer", false).create();
        public static final Registry<LexiconPage> PAGE = new Registrar<LexiconPage>("lexicon_page", false).create();

    }

    public static final Registry<SceneType<?, ?>> SCENE_TYPE = new Registrar<SceneType<?, ?>>("scene_type", true).create();

    public static final Registry<ActorType<?>> ACTOR_TYPE = new Registrar<ActorType<?>>("actor_type", true).create();

    private record Registrar<T> (String ID, boolean sync) {
        public Registry<T> create() {
            return new RegistryBuilder<>(key()).sync(sync).create();
        }

        public ResourceKey<Registry<T>> key() {
            return ResourceKey.createRegistryKey(Babel.location(ID));
        }
    }
}
