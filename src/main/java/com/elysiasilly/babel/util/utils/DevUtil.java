package com.elysiasilly.babel.util.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.fml.loading.FMLLoader;

public class DevUtil {

    public static boolean isDevEnv() {
        return !FMLLoader.isProduction();
    }

    public static boolean isModPresent(String namespace) {
        return ModList.get().isLoaded(namespace);
    }

    public static boolean isModsPresent(String...namespaces) {
        for(String namespace : namespaces) {
            if(!isModPresent(namespace)) return false;
        }

        return true;
    }

    public static <T extends Event & IModBusEvent> void postEvent(T event) {
        ModLoader.postEvent(event);
    }

    public static <T extends Event & IModBusEvent & ICancellableEvent> boolean postEventCancelable(T event) {
        return !ModLoader.postEventWithReturn(event).isCanceled();
    }

    @OnlyIn(Dist.CLIENT)
    public static RegistryAccess registry() {
        return level().registryAccess();
    }

    @OnlyIn(Dist.CLIENT)
    public static ClientLevel level() {
        return Minecraft.getInstance().level;
    }
}
