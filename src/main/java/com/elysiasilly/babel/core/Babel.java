package com.elysiasilly.babel.core;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Babel.MODID)
public class Babel {
    public static final String MODID = "babel";

    public static ResourceLocation location(String string) { return ResourceLocation.fromNamespaceAndPath(MODID, string); }

    public static final Logger LOGGER = LogManager.getLogger(MODID.toUpperCase());

    public Babel(final IEventBus bus, final ModContainer modContainer) {}
}
