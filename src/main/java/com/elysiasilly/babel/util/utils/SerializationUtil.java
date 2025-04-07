package com.elysiasilly.babel.util.utils;

import com.elysiasilly.babel.util.conversions.VectorConversions;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.util.Optional;

public class SerializationUtil {

    public static void vector3d(String id, Vector3d vec, CompoundTag tag) {
        tag.putByteArray(id, doubleToByte(VectorConversions.toArray(vec)));
    }

    public static Vector3d vector3d(String id, CompoundTag tag) {
        return VectorConversions.toJOML(byteToDouble(tag.getByteArray(id)));
    }

    public static void vec3(String id, Vec3 vec, CompoundTag tag) {
        tag.putByteArray(id, doubleToByte(VectorConversions.toArray(vec)));
    }

    public static Vec3 vec3(String id, CompoundTag tag) {
        return VectorConversions.toMojang(byteToDouble(tag.getByteArray(id)));
    }

    public static void vector3f(String id, Vector3f vec, CompoundTag tag) {
        tag.putByteArray(id, floatToByte(VectorConversions.toArray(vec)));
    }

    public static Vector3f vector3f(String id, CompoundTag tag) {
        return VectorConversions.toJOML(byteToFloat(tag.getByteArray(id)));
    }

    ///

    public static void itemStack(String id, ItemStack stack, CompoundTag tag, HolderLookup.Provider registries) {
        if(ItemUtil.isValid(stack)) {
            tag.put(id, stack.save(registries, tag));
        }
    }

    public static ItemStack itemStack(String id, CompoundTag tag, HolderLookup.Provider registries) {
        Tag data = tag.get(id);
        if(data == null) return ItemStack.EMPTY;
        Optional<ItemStack> stack = ItemStack.parse(registries, data);
        return stack.orElse(ItemStack.EMPTY);
    }

    ///

    private static byte[] doubleToByte(double[] doubleArray) {
        ByteBuffer buffer = ByteBuffer.allocate(doubleArray.length * 8);
        buffer.asDoubleBuffer().put(doubleArray);
        return buffer.array();
    }

    private static byte[] floatToByte(float[] floatArray) {
        ByteBuffer buffer = ByteBuffer.allocate(floatArray.length * 4);
        buffer.asFloatBuffer().put(floatArray);
        return buffer.array();
    }

    private static byte[] intToByte(int[] intArray) {
        ByteBuffer buffer = ByteBuffer.allocate(intArray.length * 4);
        buffer.asIntBuffer().put(intArray);
        return buffer.array();
    }

    private static double[] byteToDouble(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        double[] doubleArray = new double[byteArray.length / 8];
        buffer.asDoubleBuffer().get(doubleArray);
        return doubleArray;
    }

    private static float[] byteToFloat(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        float[] floatArray = new float[byteArray.length / 4];
        buffer.asFloatBuffer().get(floatArray);
        return floatArray;
    }

    private static int[] byteToInt(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        int[] intArray = new int[byteArray.length / 4];
        buffer.asIntBuffer().get(intArray);
        return intArray;

    }
}
