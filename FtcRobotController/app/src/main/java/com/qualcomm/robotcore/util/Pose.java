package com.qualcomm.robotcore.util;

import java.lang.reflect.Array;

public class Pose
{
    public MatrixD poseMatrix;
    public double transX;
    public double transY;
    public double transZ;
    
    public Pose() {
        this.transX = 0.0;
        this.transY = 0.0;
        this.transZ = 0.0;
    }
    
    public Pose(final double transX, final double transY, final double transZ) {
        this.transX = transX;
        this.transY = transY;
        this.transZ = transZ;
        this.poseMatrix = new MatrixD(3, 4);
        final double[] array = this.poseMatrix.data()[0];
        final double[] array2 = this.poseMatrix.data()[1];
        this.poseMatrix.data()[2][2] = 1.0;
        array[0] = (array2[1] = 1.0);
        this.poseMatrix.data()[0][3] = transX;
        this.poseMatrix.data()[1][3] = transY;
        this.poseMatrix.data()[2][3] = transZ;
    }
    
    public Pose(final MatrixD poseMatrix) {
        if (poseMatrix == null) {
            throw new IllegalArgumentException("Attempted to construct Pose from null matrix");
        }
        if (poseMatrix.numRows() != 3 || poseMatrix.numCols() != 4) {
            throw new IllegalArgumentException("Invalid matrix size ( " + poseMatrix.numRows() + ", " + poseMatrix.numCols() + " )");
        }
        this.poseMatrix = poseMatrix;
        this.transX = poseMatrix.data()[0][3];
        this.transY = poseMatrix.data()[1][3];
        this.transZ = poseMatrix.data()[2][3];
    }
    
    public static MatrixD makeRotationX(final double n) {
        final double[][] array = (double[][])Array.newInstance(Double.TYPE, 3, 3);
        final double cos = Math.cos(n);
        final double sin = Math.sin(n);
        array[0][0] = 1.0;
        final double[] array2 = array[0];
        final double[] array3 = array[0];
        array[1][0] = (array[2][0] = 0.0);
        array2[1] = (array3[2] = 0.0);
        array[1][1] = (array[2][2] = cos);
        array[1][2] = -sin;
        array[2][1] = sin;
        return new MatrixD(array);
    }
    
    public static MatrixD makeRotationY(final double n) {
        final double[][] array = (double[][])Array.newInstance(Double.TYPE, 3, 3);
        final double cos = Math.cos(n);
        final double sin = Math.sin(n);
        final double[] array2 = array[0];
        final double[] array3 = array[1];
        array[1][2] = (array[2][1] = 0.0);
        array2[1] = (array3[0] = 0.0);
        array[1][1] = 1.0;
        array[0][0] = (array[2][2] = cos);
        array[0][2] = sin;
        array[2][0] = -sin;
        return new MatrixD(array);
    }
    
    public static MatrixD makeRotationZ(final double n) {
        final double[][] array = (double[][])Array.newInstance(Double.TYPE, 3, 3);
        final double cos = Math.cos(n);
        final double sin = Math.sin(n);
        array[2][2] = 1.0;
        final double[] array2 = array[2];
        final double[] array3 = array[2];
        array[0][2] = (array[1][2] = 0.0);
        array2[0] = (array3[1] = 0.0);
        array[0][0] = (array[1][1] = cos);
        array[0][1] = -sin;
        array[1][0] = sin;
        return new MatrixD(array);
    }
    
    public double getDistanceInMm() {
        return Math.sqrt(Math.pow(this.transX, 2.0) + Math.pow(this.transY, 2.0) + Math.pow(this.transZ, 2.0));
    }
    
    public MatrixD getTranslationMatrix() {
        return new MatrixD(new double[][] { { this.transX }, { this.transY }, { this.transZ } });
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final double[] anglesAroundZ = PoseUtils.getAnglesAroundZ(this);
        sb.append(String.format("(XYZ %1$,.2f ", this.transX));
        sb.append(String.format(" %1$,.2f ", this.transY));
        sb.append(String.format(" %1$,.2f mm)", this.transZ));
        sb.append(String.format("(Angles %1$,.2f, ", anglesAroundZ[0]));
        sb.append(String.format(" %1$,.2f, ", anglesAroundZ[1]));
        sb.append(String.format(" %1$,.2f ", anglesAroundZ[2]));
        sb.append('°');
        sb.append(")");
        return sb.toString();
    }
}
