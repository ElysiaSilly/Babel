package com.elysiasilly.babel.api.client.model.resources;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.client.model.Util;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class Texture {

    private final String name;
    private final UUID uuid;

    private final ResourceLocation location;

    private final float width, height, uvWidth, uvHeight;

    public Texture(JsonObject element, ResourceLocation location) {

        this.name = element.get("name").getAsString();
        this.uuid = Util.uuid(element, "uuid");

        // ?
        this.width = element.get("width").getAsFloat();
        this.height = element.get("height").getAsFloat();
        this.uvWidth = element.get("uv_width").getAsFloat();
        this.uvHeight = element.get("uv_height").getAsFloat();

        String path = element.get("path").getAsString();
        if(path.contains(".png")) path = path.replaceAll(".png", "");
        this.location = path.contains(":") ? ResourceLocation.parse(path) : ResourceLocation.fromNamespaceAndPath(location.getNamespace(), path);
    }

    public float width() {
        return this.width;
    }

    public float height() {
        return this.height;
    }

    public String name() {
        return this.name;
    }

    public UUID uuid() {
        return this.uuid;
    }


    public ResourceLocation location() {
        return this.location;
    }
}
