package com.elysiasilly.babel.mixin.client;

import com.elysiasilly.babel.api.client.screen.screen.IHideElementsScreen;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(
            method = "render",
            at = @At("HEAD"),
            cancellable = true
    )

    private void babel$render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if(this.minecraft.screen instanceof IHideElementsScreen screen) if(screen.hideHUD()) ci.cancel();
    }
}
