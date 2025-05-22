package com.elysiasilly.babel.api.theatre.collision;

import net.minecraft.world.phys.AABB;
import org.joml.Vector3d;

public class SphereCollider implements Collider {

    private final double radius;

    public SphereCollider(double radius) {
        this.radius = radius;
    }

    @Override
    public Vector3d findFurthestPoint(Vector3d direction) {
        return null;
    }

    @Override
    public AABB aabb() {
        return null;
    }
}
