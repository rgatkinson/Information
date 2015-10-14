package com.qualcomm.robotcore.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class TypeConversion {
    private static final Charset a = Charset.forName("UTF-8");

    public static int byteArrayToInt(byte[] var0) {
        return byteArrayToInt(var0, ByteOrder.BIG_ENDIAN);
    }

    public static int byteArrayToInt(byte[] var0, ByteOrder var1) {
        return ByteBuffer.wrap(var0).order(var1).getInt();
    }

    public static long byteArrayToLong(byte[] var0) {
        return byteArrayToLong(var0, ByteOrder.BIG_ENDIAN);
    }

    public static long byteArrayToLong(byte[] var0, ByteOrder var1) {
        return ByteBuffer.wrap(var0).order(var1).getLong();
    }

    public static short byteArrayToShort(byte[] var0) {
        return byteArrayToShort(var0, ByteOrder.BIG_ENDIAN);
    }

    public static short byteArrayToShort(byte[] var0, ByteOrder var1) {
        return ByteBuffer.wrap(var0).order(var1).getShort();
    }

    public static byte[] intToByteArray(int var0) {
        return intToByteArray(var0, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] intToByteArray(int var0, ByteOrder var1) {
        return ByteBuffer.allocate(4).order(var1).putInt(var0).array();
    }

    public static byte[] longToByteArray(long var0) {
        return longToByteArray(var0, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] longToByteArray(long var0, ByteOrder var2) {
        return ByteBuffer.allocate(8).order(var2).putLong(var0).array();
    }

    public static byte[] shortToByteArray(short var0) {
        return shortToByteArray(var0, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] shortToByteArray(short var0, ByteOrder var1) {
        return ByteBuffer.allocate(2).order(var1).putShort(var0).array();
    }

    public static byte[] stringToUtf8(String var0) {
        byte[] var1 = var0.getBytes(a);
        if (!var0.equals(new String(var1, a))) {
            Object[] var2 = new Object[]{a.name(), var0, new String(var1, a)};
            throw new IllegalArgumentException(String.format("string cannot be cleanly encoded into %s - \'%s\' -> \'%s\'", var2));
        } else {
            return var1;
        }
    }

    public static double unsignedByteToDouble(byte var0) {
        return (double) (var0 & 255);
    }

    public static int unsignedByteToInt(byte var0) {
        return var0 & 255;
    }

    public static long unsignedIntToLong(int var0) {
        return 4294967295L & (long) var0;
    }

    public static String utf8ToString(byte[] var0) {
        return new String(var0, a);
    }
}
