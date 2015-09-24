package com.qualcomm.robotcore.util;

import com.qualcomm.robotcore.util.RobotLog;

public class CurvedWheelMotion {
   public static double getDiffDriveRobotRotVelocity(int var0, int var1, int var2) {
      return Math.toDegrees((double)((var1 - var0) / var2));
   }

   public static int getDiffDriveRobotTransVelocity(int var0, int var1) {
      return (var0 + var1) / 2;
   }

   public static int getDiffDriveRobotWheelVelocity(int var0, double var1, int var3, int var4, boolean var5) {
      double var6 = Math.toRadians(var1);
      double var8;
      if(var5) {
         var8 = ((double)(var0 * 2) - var6 * (double)var4) / (double)(var3 * 2);
      } else {
         var8 = ((double)(var0 * 2) + var6 * (double)var4) / (double)(var3 * 2);
      }

      return (int)(var8 * (double)var3);
   }

   public static int velocityForRotationMmPerSec(int var0, int var1, double var2, int var4, int var5) {
      int var6 = (int)(var2 * (6.283185307179586D * (double)((int)Math.sqrt(Math.pow((double)(var4 - var0), 2.0D) + Math.pow((double)(var5 - var1), 2.0D))) / 360.0D));
      RobotLog.d("CurvedWheelMotion rX " + var0 + ", theta " + var2 + ", velocity " + var6);
      return var6;
   }
}
