package com.elysiasilly.babel.api.client.render.geometry.trail;

import net.minecraft.world.phys.Vec3;

public class TrailPoint<T extends Trail> {

    final T trail;

    int age = 0;
    Vec3 pos;

    public TrailPoint(T trail) {
        this.trail = trail;
    }

    public void tick() {
        this.age++;
        if(this.age >= this.trail.maxAge) destroy();
    }

    public void destroy() {
        this.trail.destroy(this);
    }
}
