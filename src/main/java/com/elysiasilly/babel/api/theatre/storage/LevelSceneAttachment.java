package com.elysiasilly.babel.api.theatre.storage;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.BabelRegistries;
import com.elysiasilly.babel.api.theatre.Theatre;
import com.elysiasilly.babel.api.theatre.scene.ClientScene;
import com.elysiasilly.babel.api.theatre.scene.Scene;
import com.elysiasilly.babel.api.theatre.scene.SceneType;
import com.elysiasilly.babel.api.theatre.scene.ServerScene;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.GAME)
public class LevelSceneAttachment {

    public static final HashMap<Level, List<Scene<?>>> sceneLevelMap = new HashMap<>();
    public static final List<Scene<?>> scenes = new ArrayList<>();
    public static final HashMap<ResourceKey<Level>, ServerLevel> dimensionLevelMap = new HashMap<>();

    @SubscribeEvent
    private static void clear(ServerStoppedEvent event) {
        sceneLevelMap.clear();
        scenes.clear();
        dimensionLevelMap.clear();
    }

    @SubscribeEvent
    private static void load(LevelEvent.Load event) {
        Level level = (Level) event.getLevel();

        for(Map.Entry<ResourceKey<SceneType<?, ?>>, SceneType<?, ?>> entry : BabelRegistries.SCENE_TYPE.entrySet()) {
            SceneType<?, ?> type = entry.getValue();

            if(level instanceof ServerLevel server) {
                dimensionLevelMap.put(server.dimension(), server);

                ServerScene scene = type.createServer(server);
                put(server, scene);
                scene.onSceneLoad();
            }
            if(level instanceof ClientLevel client) {
                ClientScene scene = type.createClient(client);
                put(client, scene);
                scene.onSceneLoad();
            }
        }
    }

    private static void put(Level level, Scene<?> scene) {
        scenes.add(scene);
        sceneLevelMap.computeIfAbsent(level, i -> new ArrayList<>()).add(scene);
    }

    @SubscribeEvent
    private static void unload(LevelEvent.Unload event) {
        Level level = (Level) event.getLevel();

        if(sceneLevelMap.get(level) != null) {
            for (Scene<?> scene : sceneLevelMap.get(level)) {
                scene.onSceneUnload();
                scenes.remove(scene);
            }
        }

        if(level instanceof ServerLevel server) dimensionLevelMap.remove(server.dimension());

        sceneLevelMap.remove(level);
    }

    @SubscribeEvent
    private static void tick(LevelTickEvent.Pre event) {
        for(Scene<?> scene : scenes) {
            if(scene.getSceneType().tickPre()) scene.tickInternal();
        }
    }

    @SubscribeEvent
    private static void tick(LevelTickEvent.Post event) {
        for(Scene<?> scene : scenes) {
            if(scene.getSceneType().tickPost()) scene.tickInternal();
        }
    }

    @SubscribeEvent
    private static void chunkLoad(ChunkEvent.Load event) {
        Level level = (Level) event.getLevel();
        ChunkAccess chunk = event.getChunk();

        for(Scene<?> scene : Theatre.get(level)) {
            scene.loadChunk(chunk);
        }
    }

    @SubscribeEvent
    private static void chunkUnload(ChunkEvent.Unload event) {
        Level level = (Level) event.getLevel();
        ChunkAccess chunk = event.getChunk();

        for(Scene<?> scene : Theatre.get(level)) {
            scene.unloadChunk(chunk);
        }
    }
}
