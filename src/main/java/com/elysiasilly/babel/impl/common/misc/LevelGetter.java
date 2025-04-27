package com.elysiasilly.babel.impl.common.misc;

import com.elysiasilly.babel.Babel;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

import java.util.Optional;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.GAME)
public class LevelGetter {

    private static ServerLevel LEVEL = null;

    public static Optional<ServerLevel> level() {
        return Optional.ofNullable(LEVEL);
    }

    @SubscribeEvent
    private static void serverStart(ServerStartingEvent event) {
        LEVEL = event.getServer().overworld();
    }

    @SubscribeEvent
    private static void serverStop(ServerStoppedEvent event) {
        LEVEL = null;
    }
}
