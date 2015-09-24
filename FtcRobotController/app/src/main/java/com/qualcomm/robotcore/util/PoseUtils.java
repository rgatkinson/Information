package com.qualcomm.robotcore.util;

import android.util.Log;

public class PoseUtils
{
    public static double[] getAnglesAroundZ(final MatrixD matrixD) {
        if (matrixD.numRows() != 3 || matrixD.numCols() != 3) {
            throw new IllegalArgumentException("Invalid Matrix Dimension: Expected (3,3) got (" + matrixD.numRows() + "," + matrixD.numCols() + ")");
        }
        final MatrixD times = matrixD.times(new MatrixD(new double[][] { { 0.0 }, { 0.0 }, { 1.0 } }));
        return new double[] { Math.toDegrees(Math.atan2(times.data()[1][0], times.data()[0][0])), Math.toDegrees(Math.atan2(times.data()[0][0], times.data()[1][0])), Math.toDegrees(Math.asin(times.data()[2][0] / times.length())) };
    }
    
    public static double[] getAnglesAroundZ(final Pose pose) {
        if (pose != null && pose.poseMatrix != null) {
            return getAnglesAroundZ(pose.poseMatrix.submatrix(3, 3, 0, 0));
        }
        Log.e("PoseUtils", "null input");
        return null;
    }
    
    public static double smallestAngularDifferenceDegrees(final double n, final double n2) {
        final double n3 = 3.141592653589793 * (n - n2) / 180.0;
        return 180.0 * Math.atan2(Math.sin(n3), Math.cos(n3)) / 3.141592653589793;
    }
}
