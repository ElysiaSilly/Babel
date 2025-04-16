package com.elysiasilly.babel.api.client.model;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.client.model.resources.model.Model;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelLoader extends SimplePreparableReloadListener<Map<ResourceLocation, JsonElement>> {

    private static final ModelLoader INSTANCE = new ModelLoader();

    private static final Map<ResourceLocation, Model> CACHE = new HashMap<>();

    public static Model model(ResourceLocation location) {
        return CACHE.get(location);
    }

    private final Gson gson = new Gson();

    @Override
    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        HashMap<ResourceLocation, JsonElement> map = new HashMap<>();

        System.out.println("BEGAN PARSING");

        for(Map.Entry<ResourceLocation, Resource> entry : resourceManager.listResources("models", path -> path.getPath().endsWith(".bbmodel")).entrySet()) {
            ResourceLocation location = entry.getKey();

            System.out.println(location);

            try {
                BufferedReader reader = entry.getValue().openAsReader();
                try {
                    JsonElement jsonElement = GsonHelper.fromJson(this.gson, reader, JsonElement.class);
                    map.put(location, jsonElement);
                } finally {
                    ((Reader) reader).close();
                }
            } catch (JsonParseException | IOException | IllegalArgumentException exception) {
                Babel.LOGGER.error("Couldn't parse data file {} from {}", location, exception);
            }
        }

        return map;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        CACHE.clear();
        map.forEach((location, model) -> CACHE.put(location, new Model(location, model.getAsJsonObject())));
    }



    @SubscribeEvent
    public static void registerReloadListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(INSTANCE);
    }
}
