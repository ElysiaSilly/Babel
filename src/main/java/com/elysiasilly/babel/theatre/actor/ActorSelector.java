package com.elysiasilly.babel.theatre.actor;

import java.util.function.Predicate;

public class ActorSelector {
    public static final Predicate<Actor> CAN_BE_COLLIDED_WITH = Actor::canBeCollided;

    public static final Predicate<Actor> CAN_TICK = actor -> actor.canTick() || !actor.removed();
}
