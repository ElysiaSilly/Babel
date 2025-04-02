package com.elysiasilly.babel.core;

import com.elysiasilly.babel.core.registry.BBActors;
import com.elysiasilly.babel.core.registry.BBAttachments;
import com.elysiasilly.babel.core.registry.BBItems;
import com.elysiasilly.babel.core.registry.BBScenes;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
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
    }


}
