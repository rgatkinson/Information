package com.qualcomm.robotcore.sensor;

public class TargetSize {
    public double mLongSide;
    public double mShortSide;
    public String mTargetName;

    public TargetSize() {
        this("", 0.0D, 0.0D);
    }

    public TargetSize(String var1, double var2, double var4) {
        this.mTargetName = var1;
        this.mLongSide = var2;
        this.mShortSide = var4;
    }
}
