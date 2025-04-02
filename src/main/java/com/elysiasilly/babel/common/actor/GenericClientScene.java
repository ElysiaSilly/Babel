package com.elysiasilly.babel.common.actor;

import com.elysiasilly.babel.theatre.scene.ClientScene;
import com.elysiasilly.babel.theatre.scene.Scene;
import com.elysiasilly.babel.theatre.scene.SceneType;
import com.elysiasilly.babel.core.registry.BBScenes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;

public class GenericClientScene extends ClientScene<TestActor> {

    public GenericClientScene(ClientLevel level) {
        super(level);
    }

    @Override
    public SceneType<?, ?> getSceneType() {
        return BBScenes.TEST_SCENE.get();
    }
}
