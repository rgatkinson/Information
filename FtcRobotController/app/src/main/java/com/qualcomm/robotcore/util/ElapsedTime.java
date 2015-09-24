package com.qualcomm.robotcore.util;

import com.qualcomm.robotcore.util.RobotLog;

public class ElapsedTime {
   private long a = 0L;

   public ElapsedTime() {
      this.reset();
   }

   public ElapsedTime(long var1) {
      this.a = var1;
   }

   public void log(String var1) {
      Object[] var2 = new Object[]{var1, Double.valueOf(this.time())};
      RobotLog.v(String.format("TIMER: %20s - %1.3f", var2));
   }

   public void reset() {
      this.a = System.nanoTime();
   }

   public double startTime() {
      return (double)this.a / 1.0E9D;
   }

   public double time() {
      return (double)(System.nanoTime() - this.a) / 1.0E9D;
   }

   public String toString() {
      Object[] var1 = new Object[]{Double.valueOf(this.time())};
      return String.format("%1.4f seconds", var1);
   }
}
