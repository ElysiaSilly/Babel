package com.elysiasilly.babel.api.theatre.collision;

import net.minecraft.world.phys.AABB;
import org.joml.Vector3d;

public interface Collider {

    Vector3d findFurthestPoint(Vector3d direction);

    AABB aabb();
}
