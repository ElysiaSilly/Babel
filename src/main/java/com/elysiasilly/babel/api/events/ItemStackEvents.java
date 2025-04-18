package com.elysiasilly.babel.api.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

public abstract class ItemStackEvents extends Event implements IModBusEvent {

    private final ItemStack stack;

    public ItemStack stack() {
        return this.stack;
    }

    private ItemStackEvents(ItemStack stack) {
        this.stack = stack;
    }

    public static class Created extends ItemStackEvents {
        private final ServerLevel level;

        public Created(ItemStack stack, ServerLevel level) {
            super(stack);
            this.level = level;
        }

        public ServerLevel level() {
            return this.level;
        }
    }
}
