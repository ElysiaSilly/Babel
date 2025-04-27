package com.elysiasilly.babel.util;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;

public class UtilsDev {

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

    public static <T extends Event & IModBusEvent> void postModEvent(T event) {
        ModLoader.postEvent(event);
    }

    public static <T extends Event & IModBusEvent & ICancellableEvent> boolean postModEventCancelable(T event) {
        return !ModLoader.postEventWithReturn(event).isCanceled();
    }

    public static <T extends Event> void postGameEvent(T event) {
        NeoForge.EVENT_BUS.post(event);
    }

    public static <T extends Event & ICancellableEvent> boolean postGameEventCancelable(T event) {
        return !NeoForge.EVENT_BUS.post(event).isCanceled();
    }
}
