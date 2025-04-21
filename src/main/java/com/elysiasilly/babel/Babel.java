package com.elysiasilly.babel;

import com.elysiasilly.babel.core.registry.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(Babel.MODID)
public class Babel {

    public static final String MODID = "babel";

    public static ResourceLocation location(String string) { return ResourceLocation.fromNamespaceAndPath(MODID, string); }

    public static <T> ResourceKey<Registry<T>> registryKey(String string) {
        return ResourceKey.createRegistryKey(location(string));
    }

    public static <T> Registry<T> registry(String id, boolean sync) {
        return new Registrar<T>(id, sync).create();
    }

    private record Registrar<T> (String id, boolean sync) {
        public Registry<T> create() {
            return new RegistryBuilder<>(key()).sync(sync).create();
        }

        public ResourceKey<Registry<T>> key() {
            return ResourceKey.createRegistryKey(Babel.location(id));
        }
    }

    public static final Logger LOGGER = LogManager.getLogger(MODID.toUpperCase());

    public List<DeferredRegister<?>> registries = List.of(
            BBScenes.SCENES,
            BBItems.ITEMS,
            BBActors.ACTORS,
            BBAttachments.ATTACHMENTS,
            BBComponents.COMPONENTS
    );

    public Babel(final IEventBus bus, final ModContainer container) {
        for(DeferredRegister<?> registry : registries) {
            registry.register(bus);
        }

        NeoForge.EVENT_BUS.addListener(Babel::serverStart);
        NeoForge.EVENT_BUS.addListener(Babel::serverStop);

        container.registerConfig(ModConfig.Type.CLIENT, BBConfig.CLIENT_CONFIG);
        container.registerConfig(ModConfig.Type.COMMON, BBConfig.COMMON_CONFIG);
    }

    /// lol, lmao

    private static ServerLevel LEVEL = null;

    public static ServerLevel level() {
        return LEVEL;
    }

    private static void serverStart(ServerStartingEvent event) {
        LEVEL = event.getServer().overworld();
    }

    private static void serverStop(ServerStoppedEvent event) {
        LEVEL = null;
    }
}
