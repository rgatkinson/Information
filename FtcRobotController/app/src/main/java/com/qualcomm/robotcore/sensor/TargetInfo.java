package com.qualcomm.robotcore.sensor;

import com.qualcomm.robotcore.sensor.TargetSize;
import com.qualcomm.robotcore.util.Pose;

public class TargetInfo {
   public String mTargetName;
   public Pose mTargetPose;
   public TargetSize mTargetSize;

   public TargetInfo() {
   }

   public TargetInfo(String var1, Pose var2, TargetSize var3) {
      this.mTargetName = var1;
      this.mTargetPose = var2;
      this.mTargetSize = var3;
   }
}
