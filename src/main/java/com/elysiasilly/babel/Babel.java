package com.elysiasilly.babel;

import com.elysiasilly.babel.impl.registry.BBActors;
import com.elysiasilly.babel.impl.registry.BBAttachments;
import com.elysiasilly.babel.impl.registry.BBItems;
import com.elysiasilly.babel.impl.registry.BBScenes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(Babel.MODID)
public class Babel {

    public static final String MODID = "babel";

    public static ResourceLocation location(String string) { return ResourceLocation.fromNamespaceAndPath(MODID, string); }

    public static final Logger LOGGER = LogManager.getLogger(MODID.toUpperCase());

    public List<DeferredRegister<?>> registries = List.of(
            BBScenes.SCENES,
            BBItems.ITEMS,
            BBActors.ACTORS,
            BBAttachments.ATTACHMENTS
    );

    public Babel(final IEventBus bus, final ModContainer modContainer) {
        for(DeferredRegister<?> registry : registries) {
            registry.register(bus);
        }

        NeoForge.EVENT_BUS.addListener(Babel::serverStart);
        NeoForge.EVENT_BUS.addListener(Babel::serverStop);
    }

    private static boolean SERVER_INIT = false;
    private static ServerLevel LEVEL = null;

    public static boolean serverInit() {
        return SERVER_INIT;
    }

    public static ServerLevel level() {
        return LEVEL;
    }

    private static void serverStart(ServerStartingEvent event) {
        SERVER_INIT = true;
        LEVEL = event.getServer().overworld();
    }

    private static void serverStop(ServerStoppedEvent event) {
        SERVER_INIT = false;
        LEVEL = null;
    }
}
