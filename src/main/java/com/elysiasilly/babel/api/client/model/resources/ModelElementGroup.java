package com.elysiasilly.babel.api.client.model.resources;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.client.model.Util;
import com.elysiasilly.babel.api.client.model.resources.model.Model;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ModelElementGroup extends ModelElement {

    private final List<ModelElement> children = new ArrayList<>();
    private final List<UUID> childrenByUuid = new ArrayList<>();

    private final HashMap<UUID, ModelElement> lookup = new HashMap<>();

    public ModelElementGroup(JsonObject element, Model model) {
        super(element, false);

        element.get("children").getAsJsonArray().forEach(child -> {
            if(child.isJsonPrimitive()) {
                UUID uuid = Util.uuid(child.getAsString());
                ModelElement object = model.getElement(uuid);
                if(object == null) {
                    Babel.LOGGER.warn("Object with uuid {} not found", uuid);
                } else {
                    put(object);
                }
            } else {
                ModelElementGroup outliner = new ModelElementGroup(child.getAsJsonObject(), model);
                model.put(outliner);
                put(outliner);
            }
        });
    }

    public List<ModelElement> children() {
        return this.children;
    }

    public List<UUID> childrenByUuid() {
        return this.childrenByUuid;
    }

    public ModelElement get(UUID uuid) {
        return this.lookup.get(uuid);
    }

    public void put(ModelElement object) {
        this.children.add(object);
        this.childrenByUuid.add(object.uuid());
        this.lookup.put(object.uuid(), object);
    }
}
