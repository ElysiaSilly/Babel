package com.elysiasilly.babel.api.client.model.resources;

import com.google.gson.JsonObject;

public class MeshModelElement extends ModelElement {

    private final Vertex[] vertices;

    public MeshModelElement(JsonObject element) {
        super(element, false);
        this.vertices = new Vertex[]{};
    }

    public Vertex[] vertices() {
        return this.vertices;
    }
}
