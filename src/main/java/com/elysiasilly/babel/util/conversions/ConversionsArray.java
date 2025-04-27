package com.elysiasilly.babel.util.conversions;

import java.nio.ByteBuffer;

public class ConversionsArray {

    public static byte[] toByte(double[] doubleArray) {
        ByteBuffer buffer = ByteBuffer.allocate(doubleArray.length * 8);
        buffer.asDoubleBuffer().put(doubleArray);
        return buffer.array();
    }

    public static byte[] toByte(float[] floatArray) {
        ByteBuffer buffer = ByteBuffer.allocate(floatArray.length * 4);
        buffer.asFloatBuffer().put(floatArray);
        return buffer.array();
    }

    public static byte[] toByte(int[] intArray) {
        ByteBuffer buffer = ByteBuffer.allocate(intArray.length * 4);
        buffer.asIntBuffer().put(intArray);
        return buffer.array();
    }

    public static double[] toDouble(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        double[] doubleArray = new double[byteArray.length / 8];
        buffer.asDoubleBuffer().get(doubleArray);
        return doubleArray;
    }

    public static float[] toFloat(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        float[] floatArray = new float[byteArray.length / 4];
        buffer.asFloatBuffer().get(floatArray);
        return floatArray;
    }

    public static int[] toInt(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        int[] intArray = new int[byteArray.length / 4];
        buffer.asIntBuffer().get(intArray);
        return intArray;
    }

    ///


}
