package com.elysiasilly.babel.common.actor;

import com.elysiasilly.babel.core.registry.BBScenes;
import com.elysiasilly.babel.theatre.scene.SceneType;
import com.elysiasilly.babel.theatre.scene.ServerScene;
import net.minecraft.server.level.ServerLevel;

public class GenericServerScene extends ServerScene {
    public GenericServerScene(ServerLevel level) {
        super(level);
    }

    @Override
    public SceneType<?, ?> getSceneType() {
        return BBScenes.TEST_SCENE.get();
    }
}
