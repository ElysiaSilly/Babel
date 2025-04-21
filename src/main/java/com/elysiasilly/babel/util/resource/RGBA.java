package com.elysiasilly.babel.util.resource;

import com.elysiasilly.babel.util.conversions.ColourConversions;
import com.elysiasilly.babel.util.utils.CodecUtil;
import com.elysiasilly.babel.util.utils.NumberUtil;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;

import java.util.Objects;

public class RGBA {

    private int red = -1, green = -1, blue = -1, alpha = -1;

    private RGBA() {}

    public RGBA(int r, int g, int b, int a) {
        test(r, g, b, a);
        this.red = r; this.green = g; this.blue = b; this.alpha = a;
    }

    private static void test(int r, int g, int b, int a) {
        boolean error = false;
        String string = "";

        if(r < 0 || r > 255) {
            error = true;
            string = string + String.format(" Red [%s]", r);
        }
        if(g < 0 || g > 255) {
            error = true;
            string = string + String.format(" Green [%s]", g);
        }
        if(b < 0 || b > 255) {
            error = true;
            string = string + String.format(" Blue [%s]", b);
        }
        if(a < 0 || a > 255) {
            error = true;
            string = string + String.format(" Alpha [%s]", a);
        }
        if(error) {
            throw new IllegalArgumentException("RGBA parameter(s) outside of expected range:" + string);
        }
    }

    public RGBA(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public RGBA(int gray) {
        this(gray, gray, gray);
    }

    public RGBA(float gray) {
        this(cast(gray));
    }

    public RGBA(float r, float g, float B) {
        this(cast(r), cast(g), cast(B));
    }

    public RGBA(float r, float g, float b, float a) {
        this(cast(r), cast(g), cast(b), cast(a));
    }

    public RGBA(RandomSource random) {
        this(random.nextFloat(), random.nextFloat(), random.nextFloat());
    }

    private static int cast(float f) {
        return NumberUtil.castToRange(0, 1, 0, 255, f);
    }

    public boolean isTransparent() {
        return a() == 0;
    }

    public RGBA shade(float shade) {
        return new RGBA(round(r() * shade), round(g() * shade), round(b() * shade), a());
    }

    private static int round(float value) {
        return Math.round(value);
    }

    public RGBA lerp(RGBA to, float delta) {
        return new RGBA(lerp(delta, r(), to.r()), lerp(delta, g(), to.g()), lerp(delta, b(), to.b()), lerp(delta, a(), to.a()));
    }

    private static int lerp(float delta, int start, int end) {
        return Math.round(start + delta * (end - start));
    }

    public int dec() {
        return ColourConversions.dec(this);
    }

    public int hex() {
        return ColourConversions.hex(this);
    }

    public int abgr() {
        return ColourConversions.abgr(this);
    }

    public int argb() {
        return ColourConversions.argb(this);
    }

    public int[] array() {
        return ColourConversions.array(this);
    }

    ///

    public int r() {
        return this.red;
    }

    public void r(int r) {
        test(r, "Red");
        this.red = r;
    }

    public int g() {
        return this.green;
    }

    public void g(int g) {
        test(g, "Green");
        this.green = g;
    }

    public int b() {
        return this.blue;
    }

    public void b(int b) {
        test(b, "Blue");
        this.blue = b;
    }

    public int a() {
        return this.alpha;
    }

    public void a(int a) {
        test(a, "Alpha");
        this.alpha = a;
    }

    private static void test(int value, String name) {
        boolean error = false;
        String string = "";

        if(value < 0 || value > 255) {
            error = true;
            string = string + String.format(" %s [%s]", name, value);
        }
        if(error) {
            throw new IllegalArgumentException("RGBA parameter outside of expected range:" + string);
        }
    }

    ///

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RGBA other && r() == other.r() && this.g() == other.g() && this.b() == other.b() && this.a() == other.a();
    }

    @Override
    public int hashCode() {
        return Objects.hash(r(), g(), b(), a());
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %s, %s]", r(), g(), b(), a());
    }

    ///

    public static final Codec<RGBA> CODEC = CodecUtil.RGBA_CODEC;

    public static final StreamCodec<ByteBuf, RGBA> STREAM_CODEC = CodecUtil.RGBA_STREAM_CODEC;

    ///

    public static final RGBA NULL = new RGBA();

    public static final RGBA WHITE = new RGBA(1f);
    public static final RGBA BLACK = new RGBA(0f);

    public static final RGBA RED = new RGBA(1f, 0f, 0f);
    public static final RGBA GREEN = new RGBA(0f, 1f, 0f);
    public static final RGBA BLUE = new RGBA(0f, 0f, 1f);

}
