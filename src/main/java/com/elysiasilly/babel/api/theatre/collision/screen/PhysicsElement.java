package com.elysiasilly.babel.api.theatre.collision.screen;

import com.elysiasilly.babel.api.client.screen.neo.BabelElement;
import com.elysiasilly.babel.api.client.screen.neo.BabelScreen;
import com.elysiasilly.babel.api.theatre.collision.MeshCollider;
import org.joml.Vector3d;

public class PhysicsElement extends BabelElement {

    public PhysicsElement(Vector3d position, BabelScreen screen, MeshCollider collider) {
        super(position, screen, collider);
    }
}
