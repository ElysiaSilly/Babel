package com.elysiasilly.babel.api.theatre;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.ActorType;
import com.elysiasilly.babel.api.theatre.scene.Scene;
import com.elysiasilly.babel.api.theatre.scene.SceneType;
import com.elysiasilly.babel.api.theatre.storage.LevelSceneAttachment;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.List;

public class Theatre {

    private static Level get(ResourceKey<Level> dimension) {
        return LevelSceneAttachment.dimensionLevelMap.get(dimension);
    }

    public static <S extends Scene<?>> S get(ResourceKey<Level> dimension, SceneType<?, ?> type) {
        return get(get(dimension), type);
    }

    public static List<Scene<?>> get(Level level) {
        return LevelSceneAttachment.sceneLevelMap.get(level);
    }

    // cooking straight shit here
    public static <S extends Scene<?>> S get(Level level, SceneType<?, ?> type) {
        List<Scene<?>> scenes = LevelSceneAttachment.sceneLevelMap.get(level);

        Scene<?> temp = null;

        for(Scene<?> scene : scenes) {
            if(scene.getSceneType().equals(type)) {
                temp = scene; break;
            }
        }

        return temp.self();
    }

    public static <A extends Actor> A add(Level level, ActorType<A> actorType) {
        A actor = actorType.create();
        add(level, actor);
        return actor;
    }

    public static <A extends Actor, S extends Scene<?>> void add(Level level, A actor) {
        S scene = get(level, actor.sceneType());
        scene.addActor(actor);
    }
}
