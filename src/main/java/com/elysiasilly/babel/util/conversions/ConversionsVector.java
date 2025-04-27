package com.elysiasilly.babel.util.conversions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class ConversionsVector {

    public static float[] toArray(Vector3f vec) {
        return new float[]{vec.x, vec.y, vec.z};
    }

    public static Vector3f toJOML(float[] array) {
        return new Vector3f(array[0], array[1], array[2]);
    }

    public static double[] toArray(Vec3 vec) {
        return new double[]{vec.x, vec.y, vec.z};
    }

    public static Vec3 toMojang(double[] array) {
        return new Vec3(array[0], array[1], array[2]);
    }

    public static double[] toArray(Vector3d vec) {
        return new double[]{vec.x, vec.y, vec.z};
    }

    public static Vector3d toJOML(double[] array) {
        return new Vector3d(array[0], array[1], array[2]);
    }

    public static Vec3 toMojang(Vector3d vec) {
        return new Vec3(vec.x, vec.y, vec.z);
    }

    public static Vector3d toJOML(Vec3 vec) {
        return new Vector3d(vec.x, vec.y, vec.z);
    }

    public static Vec3i toMojang(Vector3i vec) {
        return new Vec3i(vec.x, vec.y, vec.z);
    }

    public static Vector3i toJOML(Vec3i vec) {
        return new Vector3i(vec.getX(), vec.getY(), vec.getZ());
    }

    public static BlockPos toBlockPos(Vec3 vec) {
        return BlockPos.containing(vec.x, vec.y, vec.z);
    }

    public static BlockPos toBlockPos(Vector3d vec) {
        return BlockPos.containing(vec.x, vec.y, vec.z);
    }

    public static int[] toArray(BlockPos pos) {
        return new int[]{pos.getX(), pos.getY(), pos.getZ()};
    }

    public static BlockPos toBlockPos(int[] array) {
        return new BlockPos(array[0], array[1], array[2]);
    }

    ///

    public static Vec3 vec2ToVec3(Vec2 vec) {
        return new Vec3(vec.x, vec.y, 0);
    }
}
