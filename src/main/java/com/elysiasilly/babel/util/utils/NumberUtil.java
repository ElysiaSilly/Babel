package com.elysiasilly.babel.util.utils;

import java.nio.ByteBuffer;

public class NumberUtil {

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

    ///

    public static byte[] doubleToByte(double[] doubleArray) {
        ByteBuffer buffer = ByteBuffer.allocate(doubleArray.length * 8);
        buffer.asDoubleBuffer().put(doubleArray);
        return buffer.array();
    }

    public static byte[] floatToByte(float[] floatArray) {
        ByteBuffer buffer = ByteBuffer.allocate(floatArray.length * 4);
        buffer.asFloatBuffer().put(floatArray);
        return buffer.array();
    }

    public static byte[] intToByte(int[] intArray) {
        ByteBuffer buffer = ByteBuffer.allocate(intArray.length * 4);
        buffer.asIntBuffer().put(intArray);
        return buffer.array();
    }

    public static double[] byteToDouble(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        double[] doubleArray = new double[byteArray.length / 8];
        buffer.asDoubleBuffer().get(doubleArray);
        return doubleArray;
    }

    public static float[] byteToFloat(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        float[] floatArray = new float[byteArray.length / 4];
        buffer.asFloatBuffer().get(floatArray);
        return floatArray;
    }

    public static int[] byteToInt(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        int[] intArray = new int[byteArray.length / 4];
        buffer.asIntBuffer().get(intArray);
        return intArray;
    }
}
