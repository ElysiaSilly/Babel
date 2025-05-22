package com.elysiasilly.babel;

import com.elysiasilly.babel.core.Registrar;
import com.elysiasilly.babel.core.registry.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

@Mod(Babel.MODID)
public class Babel {

    public static final String MODID = "babel";

    public static final Logger LOGGER = LogManager.getLogger(MODID.toUpperCase());

    public static ResourceLocation location(String string) { return ResourceLocation.fromNamespaceAndPath(MODID, string); }

    public static <T> ResourceKey<Registry<T>> registryKey(String string) {
        return ResourceKey.createRegistryKey(location(string));
    }

    public static <T> Registrar<T> registry(String id, boolean sync) {
        return new Registrar<>(location(id), sync);
    }

    public static final List<DeferredRegister<?>> REGISTRIES = List.of(
            BBActors.ACTORS,
            BBScenes.SCENES,
            BBItems.ITEMS,
            BBAttachments.ATTACHMENTS,
            BBComponents.COMPONENTS,
            BBArgumentTypes.ARGUMENTS
    );

    public static final Map<ModConfig.Type, ModConfigSpec> CONFIGS = Map.of(
            ModConfig.Type.CLIENT, BBConfig.CLIENT_CONFIG,
            ModConfig.Type.COMMON, BBConfig.COMMON_CONFIG
    );

    public Babel(final IEventBus bus, final ModContainer container) {
        BBBlocks.REGISTRAR.register(bus);

        for(DeferredRegister<?> registry : REGISTRIES)
            registry.register(bus);
        for(Map.Entry<ModConfig.Type, ModConfigSpec> config : CONFIGS.entrySet())
            container.registerConfig(config.getKey(), config.getValue());
    }
}
