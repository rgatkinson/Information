package com.qualcomm.robotcore.util;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;

public class RunShellCommand {
   boolean a = false;

   private String a(String param1, boolean param2) {
      // $FF: Couldn't be decompiled
   }

   public static int getSpawnedProcessPid(String var0, String var1, RunShellCommand var2) {
      int var3 = 0;
      String var4 = var2.run("ps");
      String var5 = "invalid";
      String[] var6 = var4.split("\n");
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String var12 = var6[var8];
         if(var12.contains(var1)) {
            var5 = var12.split("\\s+")[0];
            break;
         }
      }

      String[] var9 = var4.split("\n");

      for(int var10 = var9.length; var3 < var10; ++var3) {
         String var11 = var9[var3];
         if(var11.contains(var0) && var11.contains(var5)) {
            return Integer.parseInt(var11.split("\\s+")[1]);
         }
      }

      return -1;
   }

   public static void killSpawnedProcess(String param0, String param1, RunShellCommand param2) throws RobotCoreException {
      // $FF: Couldn't be decompiled
   }

   public void enableLogging(boolean var1) {
      this.a = var1;
   }

   public String run(String var1) {
      if(this.a) {
         RobotLog.v("running command: " + var1);
      }

      String var2 = this.a(var1, false);
      if(this.a) {
         RobotLog.v("         output: " + var2);
      }

      return var2;
   }

   public String runAsRoot(String var1) {
      if(this.a) {
         RobotLog.v("running command: " + var1);
      }

      String var2 = this.a(var1, true);
      if(this.a) {
         RobotLog.v("         output: " + var2);
      }

      return var2;
   }
}
