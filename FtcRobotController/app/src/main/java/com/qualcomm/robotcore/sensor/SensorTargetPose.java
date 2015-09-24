package com.qualcomm.robotcore.sensor;

import java.util.List;

public abstract class SensorTargetPose extends SensorBase<List<TrackedTargetInfo>>
{
    public SensorTargetPose(final List<SensorListener<List<TrackedTargetInfo>>> list) {
        super(list);
    }
}
