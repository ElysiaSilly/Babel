package com.elysiasilly.babel.api.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

public abstract class ItemStackEvents extends Event {

    private final ItemStack stack;
    private final Item item;

    public ItemStack itemStack() {
        return this.stack;
    }

    public Item item() {
        return this.item;
    }

    private ItemStackEvents(ItemStack stack) {
        this.stack = stack;
        this.item = stack.getItem();
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
