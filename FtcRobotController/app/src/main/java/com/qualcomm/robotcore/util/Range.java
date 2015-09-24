package com.qualcomm.robotcore.util;

public class Range
{
    public static double clip(final double n, final double n2, final double n3) {
        if (n < n2) {
            return n2;
        }
        if (n > n3) {
            return n3;
        }
        return n;
    }
    
    public static float clip(final float n, final float n2, final float n3) {
        if (n < n2) {
            return n2;
        }
        if (n > n3) {
            return n3;
        }
        return n;
    }
    
    public static double scale(final double n, final double n2, final double n3, final double n4, final double n5) {
        return n4 - n2 * (n4 - n5) / (n2 - n3) + (n4 - n5) / (n2 - n3) * n;
    }
    
    public static void throwIfRangeIsInvalid(final double n, final double n2, final double n3) throws IllegalArgumentException {
        if (n < n2 || n > n3) {
            throw new IllegalArgumentException(String.format("number %f is invalid; valid ranges are %f..%f", n, n2, n3));
        }
    }
}
