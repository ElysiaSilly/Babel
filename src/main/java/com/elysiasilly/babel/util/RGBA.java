package com.elysiasilly.babel.util;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class RGBA {

    public int red;
    public int green;
    public int blue;
    public int alpha;

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
        this.alpha = 255;
    }

    public RGBA(int gray) {
        this.red = gray;
        this.green = gray;
        this.blue = gray;
        this.alpha = 255;
    }

    public RGBA(float gray) {
        this.red = (int) MathUtil.numbers.castToRange(0, 1, 0, 255, gray);
        this.green = (int) MathUtil.numbers.castToRange(0, 1, 0, 255, gray);
        this.blue = (int) MathUtil.numbers.castToRange(0, 1, 0, 255, gray);
        this.alpha = 255;
    }

    public RGBA(float R, float G, float B) {
        this.red = (int) MathUtil.numbers.castToRange(0, 1, 0, 255, R);
        this.green = (int) MathUtil.numbers.castToRange(0, 1, 0, 255, G);
        this.blue = (int) MathUtil.numbers.castToRange(0, 1, 0, 255, B);
        this.alpha = 255;
    }

    public RGBA(float R, float G, float B, float A) {
        this.red = (int) MathUtil.numbers.castToRange(0, 1, 0, 255, R);
        this.green = (int) MathUtil.numbers.castToRange(0, 1, 0, 255, G);
        this.blue = (int) MathUtil.numbers.castToRange(0, 1, 0, 255, B);
        this.alpha = (int) MathUtil.numbers.castToRange(0, 1, 0, 255, A);
    }

    public RGBA shade(float bias) {
        this.red = (int) (this.red / bias);
        this.green = (int) (this.green / bias);
        this.blue = (int) (this.blue / bias);
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

    public RGBA aberrate(float bias) { // todo
        RGBA temp = new RGBA(0, 0, 0, 0);
        temp.red = (int) (this.red > this.green && this.red > this.blue ? this.red * .1 : this.red / bias);
        temp.green = (int) (this.green > this.red && this.green > this.blue ? this.green * .1 : this.green / bias);
        temp.blue = (int) (this.blue > this.green && this.blue > this.red ? this.blue * .1 : this.blue / bias);
        this.red = temp.red;
        this.green = temp.green;
        this.blue = temp.blue;
        return this;
    }

    public static class colours {
        public static RGBA random(RandomSource ran) {
            return new RGBA(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), ran.nextInt(255));
        }

        public static final RGBA WHITE = new RGBA(1f);
        public static final RGBA BLACK = new RGBA(0f);
    }
}
