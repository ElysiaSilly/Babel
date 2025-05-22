package com.elysiasilly.babel.util.conversions;

import com.elysiasilly.babel.util.type.RGBA;

public class ConversionsColour {

    public static int toABGR(RGBA rgba) {
        return rgba.a() << 24 | rgba.b() << 16 | rgba.g() << 8 | rgba.r();
    }

    public static RGBA fromABGR(int abgr) {
        return new RGBA(abgr & 0xFF, abgr >> 8 & 0xFF, abgr >> 16 & 0xFF, abgr >>> 24);
    }

    //

    public static int toARGB(RGBA rgba) {
        return rgba.a() << 24 | rgba.r() << 16 | rgba.g() << 8 | rgba.b();
    }

    public static RGBA fromARGB(int argb) {
        return new RGBA(argb >> 16 & 0xFF, argb >> 8 & 0xFF, argb & 0xFF, argb >>> 24);
    }

    //

    public static int[] toArray(RGBA rgba) {
        return new int[]{rgba.a(), rgba.r(), rgba.g(), rgba.b()};
    }

    public static RGBA fromArray(int[] array) {
        return new RGBA(array[0], array[1], array[2], array[3]);
    }
}
