package com.elysiasilly.babel.api.client.model.resources.model;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.client.model.Util;
import com.elysiasilly.babel.api.client.model.resources.*;
import com.elysiasilly.babel.util.resource.RGBA;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

public class Model {

    private final List<ModelElement> elements = new ArrayList<>();
    private final Map<UUID, ModelElement> uuidElementLookup = new HashMap<>();
    private final Map<String, ModelElement> nameElementLookup = new HashMap<>();

    private final List<ModelElementGroup> groups = new ArrayList<>();
    private final Map<UUID, ModelElementGroup> uuidGroupLookup = new HashMap<>();
    private final Map<String, ModelElementGroup> nameGroupLookup = new HashMap<>();

    private final List<Texture> textures = new ArrayList<>();
    private final Map<String, Texture> idTextureLookup = new HashMap<>();

    public Model(ResourceLocation location, JsonObject model) {

        model.get("textures").getAsJsonArray().forEach(element -> {
            put(new Texture(element.getAsJsonObject(), location));
        });

        model.get("elements").getAsJsonArray().forEach(element -> {
            JsonObject object = element.getAsJsonObject();
            String type = object.get("type").getAsString();

            switch(type) {
                case "cube" -> put(new CubeModelElement(object, this));
                case "locator" -> put(new LocatorModelElement(object));
                case "mesh" -> put(new MeshModelElement(object));
                case null, default -> Babel.LOGGER.warn("Could not parse type '{}'", type);
            }
        });

        if(model.get("outliner").isJsonArray()) {
            model.get("outliner").getAsJsonArray().forEach(element -> {
                try {
                    if (element.isJsonObject()) put(new ModelElementGroup(element.getAsJsonObject(), this));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public ModelElementGroup getOutliner(String name) {
        return this.nameGroupLookup.get(name);
    }

    public ModelElementGroup getOutliner(UUID uuid) {
        return this.uuidGroupLookup.get(uuid);
    }

    public List<ModelElementGroup> getOutliners() {
        return this.groups;
    }

    public ModelElement getElement(UUID uuid) {
        return this.uuidElementLookup.get(uuid);
    }

    public ModelElement getElement(String name) {
        return this.nameElementLookup.get(name);
    }

    public List<ModelElement> getElements() {
        return this.elements;
    }

    public void put(Texture texture) {
        this.textures.add(texture);
        //this.idTextureLookup.put(texture.id(), texture);
    }

    public List<Texture> getTextures() {
        return this.textures;
    }

    public Texture getTexture(int id) {
        return this.textures.get(id);
    }

    public void put(ModelElementGroup group) {
        this.groups.add(group);
        this.uuidGroupLookup.put(group.uuid(), group);
        this.nameGroupLookup.put(group.name(), group);
    }

    public void put(ModelElement element) {
        this.elements.add(element);
        this.uuidElementLookup.put(element.uuid(), element);
        this.nameElementLookup.put(element.name(), element);
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {


        for(ModelElement object : getElements()) {
            if(object instanceof CubeModelElement cube) {
                poseStack.pushPose();

                Vector3f pos = cube.pivot();

                Vector3f rot = cube.rotation();
                poseStack.rotateAround(new Quaternionf().rotationXYZ(rot.x, rot.y, rot.z), pos.x, pos.y, pos.z);

                Util.render(cube, multiBufferSource.getBuffer(RenderType.CUTOUT), poseStack.last().pose(), packedLight, RGBA.NULL);

                poseStack.popPose();
            }
        }
    }
}
