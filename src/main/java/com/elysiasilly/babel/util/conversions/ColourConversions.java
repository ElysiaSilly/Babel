package com.elysiasilly.babel.util.conversions;

import com.elysiasilly.babel.util.resource.RGBA;
import net.minecraft.world.phys.Vec3;

public class ColourConversions {

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
