package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class LinearTimeDemo extends LinearOpMode {
   public void runOpMode() throws InterruptedException {
      double var1 = this.time;
      double var3 = this.getRuntime();
      Object[] var5 = new Object[]{Double.valueOf(var1)};
      DbgLog.msg(String.format("TIE: T0 = %+.04f", var5));
      Object[] var6 = new Object[]{Double.valueOf(var3)};
      DbgLog.msg(String.format("TIE: T1 = %+.04f", var6));
      this.waitForStart();
      double var7 = this.time;
      double var9 = this.getRuntime();
      Object[] var11 = new Object[]{Double.valueOf(var7)};
      DbgLog.msg(String.format("TIE: T2 = %+.04f", var11));
      Object[] var12 = new Object[]{Double.valueOf(var9)};
      DbgLog.msg(String.format("TIE: T3 = %+.04f", var12));
      Thread.sleep(2000L);
      double var13 = this.time;
      double var15 = this.getRuntime();
      Object[] var17 = new Object[]{Double.valueOf(var13)};
      DbgLog.msg(String.format("TIE: T4 = %+.04f", var17));
      Object[] var18 = new Object[]{Double.valueOf(var15)};
      DbgLog.msg(String.format("TIE: T5 = %+.04f", var18));
   }
}
