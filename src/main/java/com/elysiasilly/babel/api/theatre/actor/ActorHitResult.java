package com.elysiasilly.babel.api.theatre.actor;

import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ActorHitResult extends HitResult {
    protected ActorHitResult(Vec3 location) {
        super(location);
    }

    @Override
    public Type getType() {
        return null;
    }
}
