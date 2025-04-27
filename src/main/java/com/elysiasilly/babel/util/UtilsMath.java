package com.elysiasilly.babel.util;

import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class UtilsMath {

    public static final double PI = Math.PI;

    public static boolean withinBounds(Vec2 pos, Vec2 start, Vec2 end) {
        return pos.x >= start.x && pos.y >= start.y && pos.x <= end.x && pos.y <= end.y;
    }

    public static boolean withinBounds(Vec3 pos, Vec3 start, Vec3 end) {
        return pos.x >= start.x && pos.y >= start.y && pos.z >= start.z && pos.x <= end.x && pos.y <= end.y && pos.z <= end.z;
    }

    public static Vec2 getPointOnCircle(int radius, int current, int total) {
        double theta = (PI * 2) / total;
        double angle = theta * current;
        return new Vec2((float) (radius * Math.cos(angle)), (float) (radius * Math.sin(angle)));
    }

    public static Vec2 rotateAroundPoint(Vec2 centre, Vec2 position, float degrees) {
        double radian = Math.toRadians(degrees);

        double x = centre.x + (position.x - centre.x) * Math.cos(radian) - (position.y - centre.y) * Math.sin(radian);
        double y = centre.y + (position.x - centre.x) * Math.sin(radian) + (position.y - centre.y) * Math.cos(radian);
        return new Vec2((float) x, (float) y);
    }

    // ??
    public static Vector3d rotateAroundPoint(Vector3d origin, Vector3d vertex, Quaterniond rotation) {
        return new Vector3d(vertex).add(toVector3d(rotation).mul(origin.distance(vertex)));
    }

    public static Vector3d toVector3d(Quaterniond quaterniond) {
        return new Vector3d(quaterniond.x, quaterniond.y, quaterniond.z);
    }

    public static boolean roughlyEquals(Vec3 first, Vec3 second, float blur) {
        return true;
    }

    public static Vec3 offset(Vec3 position, Vec3 direction, float distance) {
        return position.add(direction.multiply(new Vec3(distance, distance, distance)));
    }

    public static Vec3 add(Vec3 vec, double value) {
        return vec.add(value, value, value);
    }

    public static Vec3 abs(Vec3 vec) {
        return new Vec3(Math.abs(vec.x), Math.abs(vec.y), Math.abs(vec.z));
    }

    public static double volume(Vec3 vec) {
        return vec.x * vec.y * vec.z;
    }

    public static Vec3 multiply(Vec3 vec, double value) {
        return vec.multiply(value, value, value);
    }

    public static Vec3 closest(Vec3 vec, Vec3...values) {
        double distance = vec.distanceTo(values[0]);// distance(vec, values[0]);
        int index = 0;
        for(int i = 1; i < values.length; i++) {
            double temp = vec.distanceTo(values[i]); //distance(vec, values[i]);
            if(temp < distance) {
                index = i;
                distance = temp;
            }
        }
        return values[index];
    }

    public static List<Vec3> sphere(int radius, float clamp) {
        List<Vec3> list = new ArrayList<>();

        for(int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if(x*x+y*y+z*z <= (radius*clamp)*(radius*clamp)) list.add(new Vec3(x, y, z));
                }
            }
        }

        return list;
    }

    public static float castToRange(float oldMin, float oldMax, float newMin, float newMax, float value) {
        return (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
    }

    public static double castToRange(double oldMin, double oldMax, double newMin, double newMax, double value) {
        return (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
    }

    public static int castToRange(int oldMin, int oldMax, int newMin, int newMax, int value) {
        return (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
    }

    public static int castToRange(float oldMin, float oldMax, int newMin, int newMax, float value) {
        return Math.round(castToRange(oldMin, oldMax, (float) newMin, (float) newMax, value));
    }

    public static float closest(float number, float...values) {
        float dif = Math.abs(values[0] - number);
        int index = 0;
        for(int i = 1; i < values.length; i++){
            float temp = Math.abs(values[i] - number);
            if(temp < dif) {
                index = i;
                dif = temp;
            }
        }
        return values[index];
    }
}
