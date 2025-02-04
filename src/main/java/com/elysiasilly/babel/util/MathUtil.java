package com.elysiasilly.babel.util;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class MathUtil {

    public static final double PI = Math.PI;

    public static class vectors {

        public static Vec2 lerp(Vec2 start, Vec2 to, double delta) {
            return new Vec2((float) Mth.lerp(delta, start.x, to.x), (float) Mth.lerp(delta, start.y, to.y));
        }

        public static Vec3 random(RandomSource random) {
            return new Vec3(random.nextFloat(), random.nextFloat(), random.nextFloat());
        }

        public static Vec3 add(Vec3 vec3, double amount) {
            return vec3.add(amount, amount, amount);
        }

        public static Vec3 multiply(Vec3 vec3, double amount) {
            return new Vec3(vec3.x * amount, vec3.y * amount, vec3.z * amount);
        }

        public static float distance(Vector3f start, Vector3f end) {
            return Vector3f.distance(start.x, start.y, start.z, end.x, end.y, end.z);
        }

        public static float distance(Vec3 start, Vec3 end) {
            return distance(Conversions.vector.vector3f(start), Conversions.vector.vector3f(end));
        }

        public static Vec3 offset(Vec3 position, Vec3 direction, float distance) {
            return position.add(direction.multiply(new Vec3(distance, distance, distance)));
        }

        public static Vec3 closest(Vec3 vec3, Vec3...values) {
            float distance = vectors.distance(vec3, values[0]);
            int index = 0;
            for(int i = 1; i < values.length; i++) {
                float temp = vectors.distance(vec3, values[i]);
                if(temp < distance) {
                    index = i;
                    distance = temp;
                }
            }
            return values[index];
        }

        public static int closest(Vec3 vec3, List<Vec3> values) {
            float distance = vectors.distance(vec3, values.getFirst());
            int index = 0;
            for(int i = 1; i < values.size(); i++) {
                float temp = vectors.distance(vec3, values.get(i));
                if(temp < distance) {
                    index = i;
                    distance = temp;
                }
            }
            return index;
        } 

        public static boolean roughlyEquals(Vec3 first, Vec3 second, float blur) {
            return true;
        }
    }

    public static class numbers {

        public static float castToRange(float oldMin, float oldMax, float newMin, float newMax, float value) {
            return (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
        }

        public static int castToRangeInt(float oldMin, float oldMax, int newMin, int newMax, float value) {
            return Mth.ceil((((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin);
        }

        public static float closest(float number, float...values) {
            float distance = Math.abs(values[0] - number);
            int index = 0;
            for(int i = 1; i < values.length; i++){
                float temp = Math.abs(values[i] - number);
                if(temp < distance) {
                    index = i;
                    distance = temp;
                }
            }
            return values[index];
        }

    }

    public static class shapes {

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
    }

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
}
