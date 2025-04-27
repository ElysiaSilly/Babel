package com.elysiasilly.babel.core;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.dbi.DynamicBlockItem;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.registry.ActorType;
import com.elysiasilly.babel.api.theatre.actor.registry.DeferredActor;
import com.elysiasilly.babel.api.theatre.scene.ClientScene;
import com.elysiasilly.babel.api.theatre.scene.ServerScene;
import com.elysiasilly.babel.api.theatre.scene.registry.DeferredScene;
import com.elysiasilly.babel.api.theatre.scene.registry.SceneType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD)
public class BBRegistries {

    /// REGISTRIES
    public static final Registrar<SceneType<?, ?>> SCENE_TYPE = Babel.registry("scene_type", true);
    public static final Registrar<ActorType<?>> ACTOR_TYPE = Babel.registry("actor_type", true);

    /// DATAPACK REGISTRIES
    public static final ResourceKey<Registry<DynamicBlockItem>> DYNAMIC_BLOCK_ITEM = Babel.registryKey("dbi");

    @SubscribeEvent
    private static void register(final NewRegistryEvent event) {
        event.register(SCENE_TYPE.get());
        event.register(ACTOR_TYPE.get());
    }

    @SubscribeEvent
    private static void register(final DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(DYNAMIC_BLOCK_ITEM, DynamicBlockItem.CODEC);
    }

    ///

    public static Scenes createScenes(String namespace) {
        return new Scenes(namespace);
    }

    public static Actors createActors(String namespace) {
        return new Actors(namespace);
    }

    public static class Scenes extends DeferredRegister<SceneType<?, ?>> {

        protected Scenes(String namespace) {
            super(SCENE_TYPE.key(), namespace);
        }

        public <C extends ClientScene, S extends ServerScene> DeferredScene<C, S> registerScene(final String name, final Supplier<SceneType< C, S>> sup) {
            return new DeferredScene<>(super.register(name, sup).getKey());
        }
    }

    public static class Actors extends DeferredRegister<ActorType<?>> {

        protected Actors(String namespace) {
            super(ACTOR_TYPE.key(), namespace);
        }

        public <A extends Actor> DeferredActor<A> registerActor(final String name, final Supplier<ActorType<A>> sup) {
            return new DeferredActor<>(super.register(name, sup).getKey());
        }

        // TODO
        public void registerWithItem() {

        }
    }
}
