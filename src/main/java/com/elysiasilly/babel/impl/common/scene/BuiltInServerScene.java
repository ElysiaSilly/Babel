package com.elysiasilly.babel.impl.common.scene;

import com.elysiasilly.babel.api.theatre.scene.SceneType;
import com.elysiasilly.babel.api.theatre.scene.ServerScene;
import com.elysiasilly.babel.impl.registry.BBScenes;
import net.minecraft.server.level.ServerLevel;

public class BuiltInServerScene extends ServerScene {

    public BuiltInServerScene(ServerLevel level) {
        super(level);
    }

    @Override
    public SceneType<?, ?> getSceneType() {
        return BBScenes.BUILTIN.get();
    }
}
