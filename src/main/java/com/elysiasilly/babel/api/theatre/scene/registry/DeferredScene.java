package com.elysiasilly.babel.api.theatre.scene.registry;

import com.elysiasilly.babel.api.theatre.scene.ClientScene;
import com.elysiasilly.babel.api.theatre.scene.ServerScene;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DeferredScene<C extends ClientScene, S extends ServerScene> extends DeferredHolder<SceneType<?, ?>, SceneType<C, S>> implements SceneLike {

    public DeferredScene(ResourceKey<SceneType<?, ?>> key) {
        super(key);
    }

    @Override
    public SceneType<C, S> sceneType() {
        return get();
    }
}
