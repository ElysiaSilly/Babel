package com.elysiasilly.babel.util;

import com.elysiasilly.babel.util.conversions.ConverionsColour;
import com.elysiasilly.babel.util.conversions.ConversionsArray;
import com.elysiasilly.babel.util.conversions.ConversionsVector;
import com.elysiasilly.babel.util.type.RGBA;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.Optional;

public class UtilsSerialization {

    public static void component(String id, Component component, CompoundTag tag) {
        tag.put(id, ComponentSerialization.CODEC.encodeStart(NbtOps.INSTANCE, component).getOrThrow());
    }

    public static Component component(String id, CompoundTag tag) {
        return ComponentSerialization.CODEC.parse(NbtOps.INSTANCE, tag.get(id)).getOrThrow();
    }

    ///

    public static void blockPos(String id, BlockPos pos, CompoundTag tag) {
        tag.putIntArray(id, ConversionsVector.toArray(pos));
    }

    public static BlockPos blockPos(String id, CompoundTag tag) {
        return ConversionsVector.toBlockPos(tag.getIntArray(id));
    }

    public static void vector3d(String id, Vector3d vec, CompoundTag tag) {
        tag.putByteArray(id, ConversionsArray.toByte(ConversionsVector.toArray(vec)));
    }

    public static Vector3d vector3d(String id, CompoundTag tag) {
        return ConversionsVector.toJOML(ConversionsArray.toDouble(tag.getByteArray(id)));
    }

    public static void vec3(String id, Vec3 vec, CompoundTag tag) {
        tag.putByteArray(id, ConversionsArray.toByte(ConversionsVector.toArray(vec)));
    }

    public static Vec3 vec3(String id, CompoundTag tag) {
        return ConversionsVector.toMojang(ConversionsArray.toDouble(tag.getByteArray(id)));
    }

    public static void vector3f(String id, Vector3f vec, CompoundTag tag) {
        tag.putByteArray(id, ConversionsArray.toByte(ConversionsVector.toArray(vec)));
    }

    public static Vector3f vector3f(String id, CompoundTag tag) {
        return ConversionsVector.toJOML(ConversionsArray.toFloat(tag.getByteArray(id)));
    }

    ///

    public static void rgba(String id, RGBA rgba, CompoundTag tag) {
        tag.putByteArray(id, ConversionsArray.toByte(rgba.array()));
    }

    public static RGBA rgba(String id, CompoundTag tag) {
        return ConverionsColour.toRGBA(ConversionsArray.toInt(tag.getByteArray(id)));
    }

    ///

    public static void itemStack(String id, ItemStack stack, CompoundTag tag, HolderLookup.Provider registries) {
        if(UtilsItemStack.isValid(stack)) {
            tag.put(id, stack.save(registries, tag));
        }
    }

    public static ItemStack itemStack(String id, CompoundTag tag, HolderLookup.Provider registries) {
        Tag data = tag.get(id);
        if(data == null) return ItemStack.EMPTY;
        Optional<ItemStack> stack = ItemStack.parse(registries, data);
        return stack.orElse(ItemStack.EMPTY);
    }
}
