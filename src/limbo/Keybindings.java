package com.elysiasilly.babel.api.client;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.client.hud.CycleHudRenderer;
import com.elysiasilly.babel.api.common.item.cycleable.CycleBlockItem;
import com.elysiasilly.babel.api.dbi.DynamicBlockItemComponent;
import com.elysiasilly.babel.core.registry.BBComponents;
import com.elysiasilly.babel.impl.client.BabelKeybindings;
import com.elysiasilly.babel.networking.theatre.serverbound.DynamicBlockItemPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class Keybindings {

    @SubscribeEvent
    private static void run(ClientTickEvent.Pre event) {
        Player player = Minecraft.getInstance().player;

        if(player != null) {
            ItemStack stack = player.getMainHandItem();

            if(stack.has(BBComponents.DYNAMIC_BLOCK_ITEM)) {
                DynamicBlockItemComponent component = stack.get(BBComponents.DYNAMIC_BLOCK_ITEM);

                //boolean flag = false;

                while(BabelKeybindings.CYCLE_NEXT.get().consumeClick()) {
                    PacketDistributor.sendToServer(new DynamicBlockItemPacket());
                }

                //if(flag) {
                //    player.swing(InteractionHand.MAIN_HAND);
                //}
            }
        }

        /*
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

         */
    }

    public static void cycleNext(DynamicBlockItemPacket packet, IPayloadContext context) {
        DynamicBlockItemComponent.cycle(context.player().getMainHandItem());
        context.player().swing(InteractionHand.MAIN_HAND);
    }
}
