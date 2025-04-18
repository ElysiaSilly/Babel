package com.elysiasilly.babel.util.conversions;

import com.elysiasilly.babel.util.resource.RGBA;
import net.minecraft.util.FastColor;

public class ColourConversions {

    public static RGBA abgr(int abgr) {
        return new RGBA(FastColor.ABGR32.red(abgr), FastColor.ABGR32.green(abgr), FastColor.ABGR32.blue(abgr), FastColor.ABGR32.alpha(abgr));
    }

    public static RGBA argb(int argb) {
        return new RGBA(FastColor.ARGB32.red(argb), FastColor.ARGB32.green(argb), FastColor.ARGB32.blue(argb), FastColor.ARGB32.alpha(argb));
    }

    public static int abgr(RGBA rgba) {
        return FastColor.ABGR32.color(rgba.a(), rgba.b(), rgba.g(), rgba.r());
    }

    public static int argb(RGBA rgba) {
        return FastColor.ARGB32.color(rgba.a(), rgba.r(), rgba.g(), rgba.b());
    }

    public static int[] array(RGBA rgba) {
        return new int[]{rgba.a(), rgba.r(), rgba.g(), rgba.b()};
    }

    public static RGBA rgba(int[] array) {
        return new RGBA(array[0], array[1], array[2], array[3]);
    }

    /// TEMP

    public static RGBA rgbaD(int dec) {
        int B = dec / 65536;
        int G = (dec - B * 65536) / 256;
        int R = dec - B * 65536 - G * 256;
        return new RGBA(B, G, R, 255);
    }

    public static int dec(RGBA rgba) {
        return rgba.r() * 65536 + rgba.g() * 256 + rgba.b();
    }

    public static int hex(RGBA rgba) {
        return (rgba.r() << 16) + (rgba.g() << 8) + (rgba.b());
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
