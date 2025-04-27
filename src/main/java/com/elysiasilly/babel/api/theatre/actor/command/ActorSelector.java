package com.elysiasilly.babel.api.theatre.actor.command;

import com.elysiasilly.babel.api.theatre.Theatre;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.registry.ActorType;
import com.elysiasilly.babel.api.theatre.scene.Scene;
import com.elysiasilly.babel.api.theatre.scene.registry.SceneType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class ActorSelector {
    public static final Predicate<Actor> CAN_BE_COLLIDED_WITH = actor -> actor.canCollide() || !actor.removed();
    public static final Predicate<Actor> CAN_TICK = actor -> actor.canTick() || !actor.removed();

    ///

    public enum Type { ALL, RANDOM, NEAREST, HIGHLIGHTED }

    private final Type type;
    @Nullable private final ActorType<?> actorType;
    @Nullable private final SceneType<?, ?> sceneType;

    public ActorSelector(@Nullable ActorType<?> actorType, @Nullable SceneType<?, ?> sceneType, Type type) {
        this.actorType = actorType;
        this.sceneType = sceneType;
        this.type = type;
    }

    public Collection<Actor> findActors(CommandSourceStack source) {
        Level level = source.getLevel();

        Collection<Actor> collection = new ArrayList<>();

        if(sceneType != null) {
            for(Scene<?> scene : Theatre.get(level)) {
                collection.addAll(scene.actors().stream().toList());
            }
        }

        return collection;
    }

}
