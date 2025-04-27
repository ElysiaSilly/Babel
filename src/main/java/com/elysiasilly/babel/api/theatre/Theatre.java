package com.elysiasilly.babel.api.theatre;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.attachment.SceneLevelMap;
import com.elysiasilly.babel.api.theatre.scene.Scene;
import com.elysiasilly.babel.api.theatre.scene.registry.SceneType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.List;

public class Theatre {

    private static Level get(ResourceKey<Level> dimension) {
        return SceneLevelMap.dimensionLevelMap.get(dimension);
    }

    public static <S extends Scene<?>> S get(ResourceKey<Level> dimension, SceneType<?, ?> type) {
        return get(get(dimension), type);
    }

    public static List<Scene<?>> get(Level level) {
        return SceneLevelMap.sceneLevelMap.get(level);
    }

    // cooking straight shit here
    public static <S extends Scene<?>> S get(Level level, SceneType<?, ?> type) {
        List<Scene<?>> scenes = SceneLevelMap.sceneLevelMap.get(level);

        Scene<?> temp = null;

        for(Scene<?> scene : scenes) {
            if(scene.sceneType().equals(type)) {
                temp = scene; break;
            }
        }

        return temp.self();
    }

    public static <A extends Actor, S extends Scene<?>> void add(Level level, A actor) {
        get(level, actor.sceneType()).addActor(actor);
    }
}
