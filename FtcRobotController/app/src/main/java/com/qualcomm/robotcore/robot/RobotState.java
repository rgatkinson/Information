package com.qualcomm.robotcore.robot;

import com.qualcomm.robotcore.util.RobotLog;

public enum RobotState {
   DROPPED_CONNECTION(5),
   EMERGENCY_STOP(4),
   INIT(1),
   NOT_STARTED(0),
   RUNNING(2),
   STOPPED(3);

   private static final RobotState[] b;
   private int a;

   static {
      RobotState[] var0 = new RobotState[]{NOT_STARTED, INIT, RUNNING, STOPPED, EMERGENCY_STOP, DROPPED_CONNECTION};
      b = values();
   }

   private RobotState(int var3) {
      this.a = var3;
   }

   public static RobotState fromByte(byte var0) {
      RobotState var1 = NOT_STARTED;

      try {
         RobotState var4 = b[var0];
         return var4;
      } catch (ArrayIndexOutOfBoundsException var5) {
         Object[] var3 = new Object[]{Byte.valueOf(var0), var5.toString()};
         RobotLog.w(String.format("Cannot convert %d to RobotState: %s", var3));
         return var1;
      }
   }

   public byte asByte() {
      return (byte)this.a;
   }
}
