package com.qualcomm.robotcore.sensor;

public class TargetSize
{
    public double mLongSide;
    public double mShortSide;
    public String mTargetName;
    
    public TargetSize() {
        this("", 0.0, 0.0);
    }
    
    public TargetSize(final String mTargetName, final double mLongSide, final double mShortSide) {
        this.mTargetName = mTargetName;
        this.mLongSide = mLongSide;
        this.mShortSide = mShortSide;
    }
}
