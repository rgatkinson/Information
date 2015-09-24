package com.qualcomm.robotcore.sensor;

import com.qualcomm.robotcore.util.Pose;

public class TargetInfo
{
    public String mTargetName;
    public Pose mTargetPose;
    public TargetSize mTargetSize;
    
    public TargetInfo() {
    }
    
    public TargetInfo(final String mTargetName, final Pose mTargetPose, final TargetSize mTargetSize) {
        this.mTargetName = mTargetName;
        this.mTargetPose = mTargetPose;
        this.mTargetSize = mTargetSize;
    }
}
