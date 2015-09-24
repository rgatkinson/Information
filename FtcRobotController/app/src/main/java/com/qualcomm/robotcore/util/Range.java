package com.qualcomm.robotcore.util;

public class Range {
   public static double clip(double var0, double var2, double var4) {
      return var0 < var2?var2:(var0 > var4?var4:var0);
   }

   public static float clip(float var0, float var1, float var2) {
      return var0 < var1?var1:(var0 > var2?var2:var0);
   }

   public static double scale(double var0, double var2, double var4, double var6, double var8) {
      double var10 = (var6 - var8) / (var2 - var4);
      return var6 - var2 * (var6 - var8) / (var2 - var4) + var10 * var0;
   }

   public static void throwIfRangeIsInvalid(double var0, double var2, double var4) throws IllegalArgumentException {
      if(var0 < var2 || var0 > var4) {
         Object[] var6 = new Object[]{Double.valueOf(var0), Double.valueOf(var2), Double.valueOf(var4)};
         throw new IllegalArgumentException(String.format("number %f is invalid; valid ranges are %f..%f", var6));
      }
   }
}
