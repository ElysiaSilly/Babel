package com.elysiasilly.babel.api.task;

import com.elysiasilly.babel.Babel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.HashMap;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.GAME)
public class TaskQueueLevelMap {

    private static final HashMap<Level, TaskQueue> queLevelMap = new HashMap<>();

    protected static TaskQueue que(Level level) {
        return queLevelMap.computeIfAbsent(level, TaskQueue::new);
    }

    @SubscribeEvent
    private static void tick(LevelTickEvent.Pre event) {
        for(TaskQueue que : queLevelMap.values()) {
            que.tick();
        }
    }

    @SubscribeEvent
    private static void clear(ServerStoppedEvent event) {
        queLevelMap.clear();
    }
}
