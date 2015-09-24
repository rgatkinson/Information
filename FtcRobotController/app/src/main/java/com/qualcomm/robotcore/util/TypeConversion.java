package com.qualcomm.robotcore.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class TypeConversion
{
    private static final Charset a;
    
    static {
        a = Charset.forName("UTF-8");
    }
    
    public static int byteArrayToInt(final byte[] array) {
        return byteArrayToInt(array, ByteOrder.BIG_ENDIAN);
    }
    
    public static int byteArrayToInt(final byte[] array, final ByteOrder byteOrder) {
        return ByteBuffer.wrap(array).order(byteOrder).getInt();
    }
    
    public static long byteArrayToLong(final byte[] array) {
        return byteArrayToLong(array, ByteOrder.BIG_ENDIAN);
    }
    
    public static long byteArrayToLong(final byte[] array, final ByteOrder byteOrder) {
        return ByteBuffer.wrap(array).order(byteOrder).getLong();
    }
    
    public static short byteArrayToShort(final byte[] array) {
        return byteArrayToShort(array, ByteOrder.BIG_ENDIAN);
    }
    
    public static short byteArrayToShort(final byte[] array, final ByteOrder byteOrder) {
        return ByteBuffer.wrap(array).order(byteOrder).getShort();
    }
    
    public static byte[] intToByteArray(final int n) {
        return intToByteArray(n, ByteOrder.BIG_ENDIAN);
    }
    
    public static byte[] intToByteArray(final int n, final ByteOrder byteOrder) {
        return ByteBuffer.allocate(4).order(byteOrder).putInt(n).array();
    }
    
    public static byte[] longToByteArray(final long n) {
        return longToByteArray(n, ByteOrder.BIG_ENDIAN);
    }
    
    public static byte[] longToByteArray(final long n, final ByteOrder byteOrder) {
        return ByteBuffer.allocate(8).order(byteOrder).putLong(n).array();
    }
    
    public static byte[] shortToByteArray(final short n) {
        return shortToByteArray(n, ByteOrder.BIG_ENDIAN);
    }
    
    public static byte[] shortToByteArray(final short n, final ByteOrder byteOrder) {
        return ByteBuffer.allocate(2).order(byteOrder).putShort(n).array();
    }
    
    public static byte[] stringToUtf8(final String s) {
        final byte[] bytes = s.getBytes(TypeConversion.a);
        if (!s.equals(new String(bytes, TypeConversion.a))) {
            throw new IllegalArgumentException(String.format("string cannot be cleanly encoded into %s - '%s' -> '%s'", TypeConversion.a.name(), s, new String(bytes, TypeConversion.a)));
        }
        return bytes;
    }
    
    public static double unsignedByteToDouble(final byte b) {
        return b & 0xFF;
    }
    
    public static int unsignedByteToInt(final byte b) {
        return b & 0xFF;
    }
    
    public static long unsignedIntToLong(final int n) {
        return 0xFFFFFFFFL & n;
    }
    
    public static String utf8ToString(final byte[] array) {
        return new String(array, TypeConversion.a);
    }
}
