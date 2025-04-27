package com.elysiasilly.babel.api.client;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.dbi.DynamicBlockItemComponent;
import com.elysiasilly.babel.core.registry.BBComponents;
import com.elysiasilly.babel.impl.client.BBKeyBinds;
import com.elysiasilly.babel.networking.dbi.clientbound.CycleDBIPacket;
import com.elysiasilly.babel.networking.dbi.serverbound.RequestCycleDBIPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
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

                while(BBKeyBinds.CYCLE_NEXT.get().consumeClick()) {
                    PacketDistributor.sendToServer(new RequestCycleDBIPacket(1));
                }
            }
        }
    }

    // just in case to prevent any funiness
    public static void cycleNext(RequestCycleDBIPacket packet, IPayloadContext context) {
        ItemStack stack = context.player().getMainHandItem();

        switch(packet.channel()) {
            case 1 -> {
                if(DynamicBlockItemComponent.cycle(stack) && context.player() instanceof ServerPlayer player) {
                    PacketDistributor.sendToPlayer(player, new CycleDBIPacket(1, stack.get(BBComponents.DYNAMIC_BLOCK_ITEM).index()));
                }
            }
        }
    }

    public static void cycleNext(CycleDBIPacket packet, IPayloadContext context) {
        ItemStack stack = context.player().getMainHandItem();

        switch(packet.channel()) {
            case 1 -> {
                DynamicBlockItemComponent.cycle(stack, packet.index());
                context.player().swing(InteractionHand.MAIN_HAND);
            }
        }
    }
}
