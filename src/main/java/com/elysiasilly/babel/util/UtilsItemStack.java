package com.elysiasilly.babel.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class UtilsItemStack {

    public static boolean isValid(ItemStack...stacks) {
        for(ItemStack stack : stacks)
            if(stack == null || stack.isEmpty()) return false;
        return true;
    }

    public static boolean isInvalid(ItemStack...stacks) {
        for(ItemStack stack : stacks) {
            if(stack == null || stack.isEmpty()) return true;
        }
        return false;
    }

    public static void serialize(String id, ItemStack stack, CompoundTag tag, HolderLookup.Provider registries) {
        UtilsSerialization.itemStack(id, stack, tag, registries);
    }

    public static ItemStack deserialize(String id, CompoundTag tag, HolderLookup.Provider registries) {
        return UtilsSerialization.itemStack(id, tag, registries);
    }

    @Deprecated
    public static boolean hasComponent(ItemStack stack, Supplier<? extends DataComponentType<?>> component) {
        return stack.has(component) && stack.get(component) != null;
    }

    @Deprecated
    public static ItemStack splitWithCheck(ItemStack stack, int amount, Player player) {
        if(isInvalid(stack)) return ItemStack.EMPTY;
        if(player.hasInfiniteMaterials()) return stack.copy();
        return stack.split(amount);
    }

    @Deprecated
    public static void increment(ItemStack stack, int increment) {
        int count = stack.getCount() + increment;
        if(count > stack.getMaxStackSize() || count < 1) return;
        stack.grow(increment);
    }
}
