package com.qualcomm.robotcore.sensor;

import com.qualcomm.robotcore.sensor.TargetInfo;

public class TrackedTargetInfo {
   public double mConfidence;
   public TargetInfo mTargetInfo;
   public long mTimeTracked;

   public TrackedTargetInfo(TargetInfo var1, double var2, long var4) {
      this.mTargetInfo = var1;
      this.mConfidence = var2;
      this.mTimeTracked = var4;
   }
}
