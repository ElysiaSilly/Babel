package com.elysiasilly.babel.api.theatre.actor;

import java.util.function.Predicate;

public class ActorPredicates {
    public static final Predicate<Actor> CAN_BE_COLLIDED_WITH = actor -> actor.canCollide() && !actor.removed() && !actor.collisionShape().isEmpty();
    public static final Predicate<Actor> CAN_TICK = actor -> actor.canTick() && !actor.removed();
    public static final Predicate<Actor> SHOULD_SYNC = actor -> actor.synced() && !actor.removed() && actor.dirty();
}
