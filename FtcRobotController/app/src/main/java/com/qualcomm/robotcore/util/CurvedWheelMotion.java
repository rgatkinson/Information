package com.qualcomm.robotcore.util;

public class CurvedWheelMotion
{
    public static double getDiffDriveRobotRotVelocity(final int n, final int n2, final int n3) {
        return Math.toDegrees((n2 - n) / n3);
    }
    
    public static int getDiffDriveRobotTransVelocity(final int n, final int n2) {
        return (n + n2) / 2;
    }
    
    public static int getDiffDriveRobotWheelVelocity(final int n, final double n2, final int n3, final int n4, final boolean b) {
        final double radians = Math.toRadians(n2);
        double n5;
        if (b) {
            n5 = (n * 2 - radians * n4) / (n3 * 2);
        }
        else {
            n5 = (n * 2 + radians * n4) / (n3 * 2);
        }
        return (int)(n5 * n3);
    }
    
    public static int velocityForRotationMmPerSec(final int n, final int n2, final double n3, final int n4, final int n5) {
        final int n6 = (int)(n3 * (6.283185307179586 * (int)Math.sqrt(Math.pow(n4 - n, 2.0) + Math.pow(n5 - n2, 2.0)) / 360.0));
        RobotLog.d("CurvedWheelMotion rX " + n + ", theta " + n3 + ", velocity " + n6);
        return n6;
    }
}
