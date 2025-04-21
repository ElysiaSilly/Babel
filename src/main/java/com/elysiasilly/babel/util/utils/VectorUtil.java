package com.elysiasilly.babel.util.utils;

import com.elysiasilly.babel.util.conversions.VectorConversions;
import net.minecraft.world.phys.Vec3;

public class VectorUtil {

    public static Vec3 abs(Vec3 vec) {
        return new Vec3(Math.abs(vec.x), Math.abs(vec.y), Math.abs(vec.z));
    }

    public static double volume(Vec3 vec) {
        return vec.x * vec.y * vec.z;
    }

    /// dist

    public static double dist(Vec3 start, Vec3 end) {
        return VectorConversions.toJOML(start).distance(VectorConversions.toJOML(end));
    }

    /// within
}
