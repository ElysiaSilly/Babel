package com.elysiasilly.babel.core;

import com.elysiasilly.babel.common.interactible.InteractableManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Babel.MODID)
public class Babel {
    public static final String MODID = "babel";

    public static ResourceLocation location(String string) { return ResourceLocation.fromNamespaceAndPath(MODID, string); }

    public static final Logger LOGGER = LogManager.getLogger(MODID.toUpperCase());

    public Babel(final IEventBus bus, final ModContainer modContainer) {}

    public static class registries {
        public static final Registry<InteractableManager<?>> INTERACTABLE_MANAGER = new RegistryBuilder<>(keys.INTERACTABLE_MANAGER).create();
    }

    public static class keys {
        public static final ResourceKey<Registry<InteractableManager<?>>> INTERACTABLE_MANAGER = ResourceKey.createRegistryKey(location("interactable_manager"));
    }
}
