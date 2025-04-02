package com.elysiasilly.babel.mixin.client;

import com.elysiasilly.babel.theatre.Theatre;
import com.elysiasilly.babel.theatre.scene.Scene;
import com.elysiasilly.babel.theatre.storage.LevelSceneAttachment;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Nullable public ClientLevel level;

    @Shadow @Nullable public LocalPlayer player;

    @Inject(
            method = "startUseItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z",
                    ordinal  = 1
            ),
            cancellable = true
    )

    private void babel$startUseItem(CallbackInfo ci, @Local InteractionHand hand, @Local ItemStack stack) {
        List<Scene< ?>> scenes = Theatre.get(this.level);

        Scene<?> scene = scenes.getFirst();

        InteractionResult result = scene.playerInteractionRequest(this.player, hand, stack);

        if(result.consumesAction()) {
            if (result.shouldSwing()) {
                this.player.swing(hand);
            }
            ci.cancel();
        }
    }
}
