package com.elysiasilly.babel.util;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class RGBA {

    public int red;
    public int green;
    public int blue;
    public int alpha = 255;

    public RGBA(int R, int G, int B, int A) {
        this.red = R;
        this.green = G;
        this.blue = B;
        this.alpha = A;
    }

    public RGBA(int R, int G, int B) {
        this.red = R;
        this.green = G;
        this.blue = B;
    }

    public RGBA(int gray) {
        this.red = gray;
        this.green = gray;
        this.blue = gray;
    }

    public RGBA(float gray) {
        int i = cast(gray);

        this.red = i;
        this.green = i;
        this.blue = i;
    }

    public RGBA(float R, float G, float B) {
        this.red = cast(R);
        this.green = cast(G);
        this.blue = cast(B);
    }

    public RGBA(float R, float G, float B, float A) {
        this.red = cast(R);
        this.green = cast(G);
        this.blue = cast(B);
        this.alpha = cast(A);
    }

    private int cast(float f) {
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
        return Conversions.colour.dec(this);
    }

    public int hex() {
        return Conversions.colour.hex(this);
    }

    public int abgr() {
        return Conversions.colour.abgr(this);
    }

    public Vec3 vec3() {
        return Conversions.colour.rgba(this);
    }

    public static class colours {
        public static RGBA random(RandomSource ran) {
            return new RGBA(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), ran.nextInt(255));
        }

        public static final RGBA WHITE = new RGBA(1f);
        public static final RGBA BLACK = new RGBA(0f);

        public static final RGBA RED = new RGBA(1f, 0f, 0f);
        public static final RGBA GREEN = new RGBA(0f, 1f, 0f);
        public static final RGBA BLUE = new RGBA(0f, 0f, 1f);
    }
}
