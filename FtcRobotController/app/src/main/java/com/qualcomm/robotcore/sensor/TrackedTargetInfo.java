package com.qualcomm.robotcore.sensor;

public class TrackedTargetInfo
{
    public double mConfidence;
    public TargetInfo mTargetInfo;
    public long mTimeTracked;
    
    public TrackedTargetInfo(final TargetInfo mTargetInfo, final double mConfidence, final long mTimeTracked) {
        this.mTargetInfo = mTargetInfo;
        this.mConfidence = mConfidence;
        this.mTimeTracked = mTimeTracked;
    }
}
