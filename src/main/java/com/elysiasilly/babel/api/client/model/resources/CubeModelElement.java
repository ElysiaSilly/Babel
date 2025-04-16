package com.elysiasilly.babel.api.client.model.resources;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.client.model.Util;
import com.elysiasilly.babel.api.client.model.resources.model.Model;
import com.elysiasilly.babel.util.resource.UV;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class CubeModelElement extends ModelElement {

    private final Vector3f from, to;
    private final Face north, east, south, west, up, down;

    public CubeModelElement(JsonObject element, Model model) {
        super(element, false);
        this.from = Util.vector3f(element, "from").mul(1 / 16f);
        this.to = Util.vector3f(element, "to").mul(1 / 16f);

        JsonObject faces = element.get("faces").getAsJsonObject();

        this.north = face(faces.get("north").getAsJsonObject(), model);
        this.east = face(faces.get("east").getAsJsonObject(), model);
        this.south = face(faces.get("south").getAsJsonObject(), model);
        this.west = face(faces.get("west").getAsJsonObject(), model);
        this.up = face(faces.get("up").getAsJsonObject(), model);
        this.down = face(faces.get("down").getAsJsonObject(), model);
    }

    public Face face(JsonObject face, Model model) {
        if(face.get("texture").isJsonNull()) {
            return null;
        } else {
            JsonArray array = face.getAsJsonArray("uv");

            Texture texture = model.getTexture(face.get("texture").getAsInt());

            return new Face(new UV(
                    array.get(0).getAsFloat() / texture.width(),
                    array.get(1).getAsFloat() / texture.height(),
                    array.get(2).getAsFloat() / texture.width(),
                    array.get(3).getAsFloat() / texture.height()
            ),
                    texture
            );
        }
    }

    public Vector3f from() {
        return this.from;
    }

    public Vector3f to() {
        return this.to;
    }

    public Face north() {
        return this.north;
    }

    public Face east() {
        return this.east;
    }

    public Face south() {
        return this.south;
    }

    public Face west() {
        return this.west;
    }

    public Face up() {
        return this.up;
    }

    public Face down() {
        return this.down;
    }
}
