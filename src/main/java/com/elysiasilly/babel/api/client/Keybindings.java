package com.elysiasilly.babel.api.client;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.client.hud.CycleHudRenderer;
import com.elysiasilly.babel.api.common.item.cycleable.CycleBlockItem;
import com.elysiasilly.babel.impl.client.BabelKeybindings;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class Keybindings {

    @SubscribeEvent
    private static void run(ClientTickEvent.Pre event) {
        Player player = Minecraft.getInstance().player;

        if(player != null && player.getMainHandItem().getItem() instanceof CycleBlockItem item) {
            boolean flag = false;
            while(BabelKeybindings.CYCLE_NEXT.get().consumeClick()) {
                flag = CycleHudRenderer.LAYER.idle() ? CycleHudRenderer.LAYER.resetIdle() : item.cycleNext();
            }
            while(BabelKeybindings.CYCLE_PREVIOUS.get().consumeClick()) {
                flag = CycleHudRenderer.LAYER.idle() ? CycleHudRenderer.LAYER.resetIdle() : item.cyclePrevious();
            }
            while(BabelKeybindings.CYCLE_RANDOM.get().consumeClick()) {
                flag = item.cycleRandom();
            }
            if(flag) {
                player.swing(InteractionHand.MAIN_HAND);
            }
        }
    }
}
