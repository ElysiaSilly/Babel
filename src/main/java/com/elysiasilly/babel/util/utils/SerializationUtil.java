package com.elysiasilly.babel.util.utils;

import com.elysiasilly.babel.util.conversions.ColourConversions;
import com.elysiasilly.babel.util.conversions.VectorConversions;
import com.elysiasilly.babel.util.resource.RGBA;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.Optional;

public class SerializationUtil {

    public static void vector3d(String id, Vector3d vec, CompoundTag tag) {
        tag.putByteArray(id, NumberUtil.doubleToByte(VectorConversions.toArray(vec)));
    }

    public static Vector3d vector3d(String id, CompoundTag tag) {
        return VectorConversions.toJOML(NumberUtil.byteToDouble(tag.getByteArray(id)));
    }

    public static void vec3(String id, Vec3 vec, CompoundTag tag) {
        tag.putByteArray(id, NumberUtil.doubleToByte(VectorConversions.toArray(vec)));
    }

    public static Vec3 vec3(String id, CompoundTag tag) {
        return VectorConversions.toMojang(NumberUtil.byteToDouble(tag.getByteArray(id)));
    }

    public static void vector3f(String id, Vector3f vec, CompoundTag tag) {
        tag.putByteArray(id, NumberUtil.floatToByte(VectorConversions.toArray(vec)));
    }

    public static Vector3f vector3f(String id, CompoundTag tag) {
        return VectorConversions.toJOML(NumberUtil.byteToFloat(tag.getByteArray(id)));
    }

    public static void rgba(String id, RGBA rgba, CompoundTag tag) {
        tag.putByteArray(id, NumberUtil.intToByte(rgba.array()));
    }

    public static RGBA rgba(String id, CompoundTag tag) {
        return ColourConversions.rgba(NumberUtil.byteToInt(tag.getByteArray(id)));
    }

    ///

    public static void itemStack(String id, ItemStack stack, CompoundTag tag, HolderLookup.Provider registries) {
        if(ItemStackUtil.isValid(stack)) {
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
