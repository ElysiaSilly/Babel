package com.elysiasilly.babel.api.client.model.resources;

import com.elysiasilly.babel.api.client.model.Util;
import com.google.gson.JsonObject;
import org.joml.Vector3f;

import java.util.UUID;

public abstract class ModelElement {

    private final String name;
    private final UUID uuid;

    private final Vector3f pivot, rotation;

    public ModelElement(JsonObject element, boolean locator) {
        this.name = element.get("name").getAsString();
        this.uuid = Util.uuid(element, "uuid");

        this.pivot = Util.vector3f(element, locator ? "position" : "origin").mul(1 / 16f);
        this.rotation = Util.vector3f(element, "rotation").mul((float) (Math.PI / 180f));
    }

    public String name() {
        return this.name;
    }

    public UUID uuid() {
        return this.uuid;
    }

    public Vector3f pivot() {
        return this.pivot;
    }

    public Vector3f rotation() {
        return this.rotation;
    }
}
