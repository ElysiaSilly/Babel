package com.elysiasilly.babel.api.theatre.util;

import org.joml.Quaterniond;
import org.joml.Vector3d;

public class Mesh {

    private final Element[] elements;
    private Quaterniond orientation;
    private Vector3d origin;

    public Mesh(Vector3d origin, Quaterniond orientation, Element...elements) {
        this.elements = elements;
    }
}
