package com.elysiasilly.babel.core.registry;

import com.elysiasilly.babel.common.actor.GenericServerScene;
import com.elysiasilly.babel.theatre.scene.SceneType;
import com.elysiasilly.babel.common.actor.GenericClientScene;
import com.elysiasilly.babel.core.Babel;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BBScenes {

    public static final DeferredRegister<SceneType<?, ?>> SCENES = DeferredRegister.create(BabelRegistries.SCENE_TYPE, Babel.MODID);

    public static final Supplier<SceneType<GenericClientScene, GenericServerScene>> TEST_SCENE =
            SCENES.register("builtin_scene", () -> SceneType.Builder.of(GenericClientScene::new, GenericServerScene::new).tick(SceneType.Tick.POST).build());
}
