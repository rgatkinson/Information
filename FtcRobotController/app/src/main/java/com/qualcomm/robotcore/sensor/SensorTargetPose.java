package com.qualcomm.robotcore.sensor;

import com.qualcomm.robotcore.sensor.SensorBase;
import com.qualcomm.robotcore.sensor.SensorListener;
import com.qualcomm.robotcore.sensor.TrackedTargetInfo;
import java.util.List;

public abstract class SensorTargetPose extends SensorBase<List<TrackedTargetInfo>> {
   public SensorTargetPose(List<SensorListener<List<TrackedTargetInfo>>> var1) {
      super(var1);
   }
}
