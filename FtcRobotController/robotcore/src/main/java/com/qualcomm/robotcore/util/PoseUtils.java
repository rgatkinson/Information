package com.qualcomm.robotcore.util;

import android.util.Log;

public class PoseUtils {
    public static double[] getAnglesAroundZ(MatrixD var0) {
        if (var0.numRows() == 3 && var0.numCols() == 3) {
            MatrixD var1 = var0.times(new MatrixD(new double[][]{{0.0D}, {0.0D}, {1.0D}}));
            double var2 = Math.toDegrees(Math.atan2(var1.data()[1][0], var1.data()[0][0]));
            double var4 = Math.toDegrees(Math.atan2(var1.data()[0][0], var1.data()[1][0]));
            double var6 = var1.length();
            return new double[]{var2, var4, Math.toDegrees(Math.asin(var1.data()[2][0] / var6))};
        } else {
            throw new IllegalArgumentException("Invalid Matrix Dimension: Expected (3,3) got (" + var0.numRows() + "," + var0.numCols() + ")");
        }
    }

    public static double[] getAnglesAroundZ(Pose var0) {
        if (var0 != null && var0.poseMatrix != null) {
            return getAnglesAroundZ(var0.poseMatrix.submatrix(3, 3, 0, 0));
        } else {
            Log.e("PoseUtils", "null input");
            return null;
        }
    }

    public static double smallestAngularDifferenceDegrees(double var0, double var2) {
        double var4 = 3.141592653589793D * (var0 - var2) / 180.0D;
        return 180.0D * Math.atan2(Math.sin(var4), Math.cos(var4)) / 3.141592653589793D;
    }
}
