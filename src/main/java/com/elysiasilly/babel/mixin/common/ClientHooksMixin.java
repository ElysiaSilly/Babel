package com.elysiasilly.babel.mixin.common;

import com.elysiasilly.babel.theatre.actor.render.ActorRenderersEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.neoforged.fml.ModLoader;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO this probably shouldnt be a mixin but cant be arsed to figure out a better place to put it
@Mixin(ClientHooks.class)
public class ClientHooksMixin {

    @Inject(
            method = "initClientHooks",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/fml/ModLoader;postEvent(Lnet/neoforged/bus/api/Event;)V",
                    shift = At.Shift.AFTER
            )
    )

    private static void babel$initClientHooks(Minecraft mc, ReloadableResourceManager resourceManager, CallbackInfo ci) {
        ModLoader.postEvent(new ActorRenderersEvent());
    }
}
