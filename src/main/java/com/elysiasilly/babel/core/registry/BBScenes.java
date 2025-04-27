package com.elysiasilly.babel.core.registry;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.theatre.scene.registry.DeferredScene;
import com.elysiasilly.babel.api.theatre.scene.registry.SceneType;
import com.elysiasilly.babel.core.BBRegistries;
import com.elysiasilly.babel.impl.common.scene.BuiltInClientScene;
import com.elysiasilly.babel.impl.common.scene.BuiltInServerScene;

public class BBScenes {
    public static final BBRegistries.Scenes SCENES = BBRegistries.createScenes(Babel.MODID);

    public static final DeferredScene<BuiltInClientScene, BuiltInServerScene> BUILTIN =
            SCENES.registerScene("builtin", () -> SceneType.Builder.of(BuiltInClientScene::new, BuiltInServerScene::new).tick(SceneType.Tick.PRE).build());
}
