package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class TimeDemo extends OpMode {
   public void init() {
   }

   public void loop() {
      double var1 = this.getRuntime();

      try {
         Thread.sleep(200L);
      } catch (Exception var12) {
         DbgLog.error(var12.getMessage());
         return;
      }

      double var4 = this.time;
      Object[] var6 = new Object[]{Double.valueOf(var4 - var1)};
      DbgLog.msg(String.format("TIE: a) delta T = %+.04f", var6));
      double var7 = this.getRuntime();
      double var9 = this.time;
      Object[] var11 = new Object[]{Double.valueOf(var9 - var7)};
      DbgLog.msg(String.format("TIE: b) delta T = %+.04f", var11));
   }

   public void stop() {
   }
}
