package com.elysiasilly.babel.impl.common.scene;

import com.elysiasilly.babel.api.theatre.scene.ClientScene;
import com.elysiasilly.babel.api.theatre.scene.registry.SceneType;
import com.elysiasilly.babel.core.registry.BBScenes;
import net.minecraft.client.multiplayer.ClientLevel;

public class BuiltInClientScene extends ClientScene {

    public BuiltInClientScene(ClientLevel level) {
        super(level);
    }

    @Override
    public SceneType<?, ?> sceneType() {
        return BBScenes.BUILTIN.get();
    }
}
