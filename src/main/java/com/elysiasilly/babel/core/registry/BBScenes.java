package com.elysiasilly.babel.core.registry;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.theatre.scene.SceneType;
import com.elysiasilly.babel.core.BBRegistries;
import com.elysiasilly.babel.impl.common.scene.BuiltInClientScene;
import com.elysiasilly.babel.impl.common.scene.BuiltInServerScene;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BBScenes {

    public static final DeferredRegister<SceneType<?, ?>> SCENES = DeferredRegister.create(BBRegistries.SCENE_TYPE, Babel.MODID);

    public static final Supplier<SceneType<BuiltInClientScene, BuiltInServerScene>> BUILTIN =
            SCENES.register("builtin", () -> SceneType.Builder.of(BuiltInClientScene::new, BuiltInServerScene::new).tick(SceneType.Tick.POST).build());
}
