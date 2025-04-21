package com.elysiasilly.babel.impl.client;

import com.elysiasilly.babel.Babel;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BBKeyBinds {

    public static final String CATEGORY = "key.category.babel";

    public static final Lazy<KeyMapping> CYCLE_NEXT = Lazy.of(() ->
            new KeyMapping("key.babel.cycle_next", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY));

    public static final Lazy<KeyMapping> CYCLE_PREVIOUS = Lazy.of(() ->
            new KeyMapping("key.babel.cycle_previous", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY));

    public static final Lazy<KeyMapping> CYCLE_RANDOM = Lazy.of(() ->
            new KeyMapping("key.babel.cycle_random", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY));

    @SubscribeEvent
    private static void register(RegisterKeyMappingsEvent event) {
        event.register(CYCLE_NEXT.get());
        event.register(CYCLE_PREVIOUS.get());
        event.register(CYCLE_RANDOM.get());
    }
}
