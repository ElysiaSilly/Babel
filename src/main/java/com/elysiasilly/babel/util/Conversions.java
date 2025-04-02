package com.elysiasilly.babel.util;

import com.elysiasilly.babel.util.resource.RGBA;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.text.DecimalFormat;

public class Conversions {

    public static class Vec {

        // crying

        public static Vector2f toJOML(Vec2 vec) {
            return new Vector2f(vec.x, vec.y);
        }

        public static Vec2 toMojang(Vector2f vec) {
            return new Vec2(vec.x, vec.y);
        }

        /// rahhhh
        public static Vector3f toJOML(Vec3 vec) {

            //double d = 1.234567;
            //DecimalFormat df = new DecimalFormat("#.##");


            return new Vector3f( ((Double) vec.x).floatValue(), ((Double) vec.y).floatValue(), ((Double) vec.z).floatValue());
        }

        public static Vec3 toMojang(Vector3f vec) {
            return new Vec3(vec.x, vec.y, vec.z);
        }

        ///

        public static BlockPos blockPos(Vec3 vec3) {
            return BlockPos.containing(vec3.x, vec3.y, vec3.z);
        }

        public static Vec3 vec3(BlockPos pos) {
            return pos.getCenter();
        }

        public static Vector3i vector3i(Vec3 vec3) {
            return new Vector3i((int) Math.floor(vec3.x), (int) Math.floor(vec3.y), (int) Math.floor(vec3.z));
        }

        public static Vec3 vec3(Vec3i vec3i) {
            return new Vec3(vec3i.getX(), vec3i.getY(), vec3i.getZ());
        }

        public static Vec3 vec3(Vec2 vec2) {
            return new Vec3(vec2.x, vec2.y, 0);
        }

        public static Vec2 Vec2(Vec3 vec3) {
            return new Vec2((float) vec3.x, (float) vec3.y);
        }

    }

    public static class Col {

        // insanity

        public static RGBA rgbaA(int abgr) {
            return new RGBA(
                    abgr & 0xFF,
                    abgr >> 8 & 0xFF,
                    abgr >> 16 & 0xFF,
                    abgr >> 24 & 0xFF
            );
        }

        public static Vec3 rgba(RGBA rgba) {
            return new Vec3(rgba.red, rgba.green, rgba.blue);
        }

        public static int abgr(RGBA rgba) { // TODO : where alpha
            return rgba.red + (rgba.green * 256) + (rgba.blue * 256 * 256);
        }

        public static RGBA rgbaD(int dec) {
            int B = dec / 65536;
            int G = (dec - B * 65536) / 256;
            int R = dec - B * 65536 - G * 256;
            return new RGBA(B, G, R, 255);
        }

        public static int dec(RGBA rgba) {
            return rgba.red * 65536 + rgba.green * 256 + rgba.blue;
        }

        public static int hex(RGBA rgba) {
            return (rgba.red << 16) + (rgba.green << 8) + (rgba.blue);
        }

        public static RGBA rgbaH(int hex) {
            //int c = 0xff03c0; // 16712640
            int R = (hex & 0xff0000) >> 16;
            int G = (hex & 0x00ff00) >> 8;
            int B = (hex & 0x0000ff);
            return new RGBA(R, G, B, 255);
        }

        public static int dec(int hex) {
            return dec(rgbaH(hex));
        }

    }
}
