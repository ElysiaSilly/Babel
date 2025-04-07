package com.elysiasilly.babel.util.resource;

import com.elysiasilly.babel.util.MathUtil;
import com.elysiasilly.babel.util.conversions.ColourConversions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class RGBA {

    public int red;
    public int green;
    public int blue;
    public int alpha;

    public RGBA(int R, int G, int B, int A) {
        this.red = R; this.green = G; this.blue = B; this.alpha = A;
    }

    public RGBA(int R, int G, int B) {
        this(R, G, B, 255);
    }

    public RGBA(int gray) {
        this(gray, gray, gray);
    }

    public RGBA(float gray) {
        this(cast(gray));
    }

    public RGBA(float R, float G, float B) {
        this(cast(R), cast(G), cast(B));
    }

    public RGBA(float R, float G, float B, float A) {
        this(cast(R), cast(G), cast(B), cast(A));
    }

    public RGBA(RandomSource random) {
        this(random.nextFloat(), random.nextFloat(), random.nextFloat());
    }

    private static int cast(float f) {
        return MathUtil.numbers.castToRangeInt(0, 1, 0, 255, f);
    }

    public boolean isTransparent() {
        return this.alpha == 0;
    }

    public RGBA shade(float val) {
        this.red = (int) (this.red / val);
        this.green = (int) (this.green / val);
        this.blue = (int) (this.blue / val);
        return this;
    }

    public RGBA lerp(RGBA to, double delta) {
        return new RGBA((int) Mth.lerp(delta, this.red, to.red), (int) Mth.lerp(delta, this.green, to.green), (int) Mth.lerp(delta, this.blue, to.blue), (int) Mth.lerp(delta, this.alpha, to.alpha));
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

    public Vec3 vec3() {
        return ColourConversions.rgba(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RGBA other && this.red == other.red && this.green == other.green && this.blue == other.blue && this.alpha == other.alpha;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue, alpha);
    }

    ///

    public static final RGBA NULL = new RGBA(-1);

    public static final RGBA WHITE = new RGBA(1f);
    public static final RGBA BLACK = new RGBA(0f);

    public static final RGBA RED = new RGBA(1f, 0f, 0f);
    public static final RGBA GREEN = new RGBA(0f, 1f, 0f);
    public static final RGBA BLUE = new RGBA(0f, 0f, 1f);

}
