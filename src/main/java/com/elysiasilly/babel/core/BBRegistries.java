package com.elysiasilly.babel.core;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.dbi.DynamicBlockItem;
import com.elysiasilly.babel.api.theatre.actor.ActorType;
import com.elysiasilly.babel.api.theatre.scene.SceneType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD)
public class BBRegistries {

    /// REGISTRIES
    public static final Registry<SceneType<?, ?>> SCENE_TYPE = Babel.registry("scene_type", true);

    public static final Registry<ActorType<?>> ACTOR_TYPE = Babel.registry("actor_type", true);

    /// DATAPACK REGISTRIES
    public static final ResourceKey<Registry<DynamicBlockItem>> DYNAMIC_BLOCK_ITEM = Babel.registryKey("dbi");

    @SubscribeEvent
    private static void register(final NewRegistryEvent event) {
        event.register(SCENE_TYPE);
        event.register(ACTOR_TYPE);
    }

    @SubscribeEvent
    private static void register(final DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(DYNAMIC_BLOCK_ITEM, DynamicBlockItem.CODEC);
    }
}
