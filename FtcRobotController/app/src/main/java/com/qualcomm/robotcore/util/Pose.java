package com.qualcomm.robotcore.util;

import com.qualcomm.robotcore.util.MatrixD;
import com.qualcomm.robotcore.util.PoseUtils;
import java.lang.reflect.Array;

public class Pose {
   public MatrixD poseMatrix;
   public double transX;
   public double transY;
   public double transZ;

   public Pose() {
      this.transX = 0.0D;
      this.transY = 0.0D;
      this.transZ = 0.0D;
   }

   public Pose(double var1, double var3, double var5) {
      this.transX = var1;
      this.transY = var3;
      this.transZ = var5;
      this.poseMatrix = new MatrixD(3, 4);
      double[] var7 = this.poseMatrix.data()[0];
      double[] var8 = this.poseMatrix.data()[1];
      this.poseMatrix.data()[2][2] = 1.0D;
      var8[1] = 1.0D;
      var7[0] = 1.0D;
      this.poseMatrix.data()[0][3] = var1;
      this.poseMatrix.data()[1][3] = var3;
      this.poseMatrix.data()[2][3] = var5;
   }

   public Pose(MatrixD var1) {
      if(var1 == null) {
         throw new IllegalArgumentException("Attempted to construct Pose from null matrix");
      } else if(var1.numRows() == 3 && var1.numCols() == 4) {
         this.poseMatrix = var1;
         this.transX = var1.data()[0][3];
         this.transY = var1.data()[1][3];
         this.transZ = var1.data()[2][3];
      } else {
         throw new IllegalArgumentException("Invalid matrix size ( " + var1.numRows() + ", " + var1.numCols() + " )");
      }
   }

   public static MatrixD makeRotationX(double var0) {
      int[] var2 = new int[]{3, 3};
      double[][] var3 = (double[][])Array.newInstance(Double.TYPE, var2);
      double var4 = Math.cos(var0);
      double var6 = Math.sin(var0);
      var3[0][0] = 1.0D;
      double[] var8 = var3[0];
      double[] var9 = var3[0];
      double[] var10 = var3[1];
      var3[2][0] = 0.0D;
      var10[0] = 0.0D;
      var9[2] = 0.0D;
      var8[1] = 0.0D;
      double[] var11 = var3[1];
      var3[2][2] = var4;
      var11[1] = var4;
      var3[1][2] = -var6;
      var3[2][1] = var6;
      return new MatrixD(var3);
   }

   public static MatrixD makeRotationY(double var0) {
      int[] var2 = new int[]{3, 3};
      double[][] var3 = (double[][])Array.newInstance(Double.TYPE, var2);
      double var4 = Math.cos(var0);
      double var6 = Math.sin(var0);
      double[] var8 = var3[0];
      double[] var9 = var3[1];
      double[] var10 = var3[1];
      var3[2][1] = 0.0D;
      var10[2] = 0.0D;
      var9[0] = 0.0D;
      var8[1] = 0.0D;
      var3[1][1] = 1.0D;
      double[] var11 = var3[0];
      var3[2][2] = var4;
      var11[0] = var4;
      var3[0][2] = var6;
      var3[2][0] = -var6;
      return new MatrixD(var3);
   }

   public static MatrixD makeRotationZ(double var0) {
      int[] var2 = new int[]{3, 3};
      double[][] var3 = (double[][])Array.newInstance(Double.TYPE, var2);
      double var4 = Math.cos(var0);
      double var6 = Math.sin(var0);
      var3[2][2] = 1.0D;
      double[] var8 = var3[2];
      double[] var9 = var3[2];
      double[] var10 = var3[0];
      var3[1][2] = 0.0D;
      var10[2] = 0.0D;
      var9[1] = 0.0D;
      var8[0] = 0.0D;
      double[] var11 = var3[0];
      var3[1][1] = var4;
      var11[0] = var4;
      var3[0][1] = -var6;
      var3[1][0] = var6;
      return new MatrixD(var3);
   }

   public double getDistanceInMm() {
      return Math.sqrt(Math.pow(this.transX, 2.0D) + Math.pow(this.transY, 2.0D) + Math.pow(this.transZ, 2.0D));
   }

   public MatrixD getTranslationMatrix() {
      double[][] var1 = new double[3][];
      double[] var2 = new double[]{this.transX};
      var1[0] = var2;
      double[] var3 = new double[]{this.transY};
      var1[1] = var3;
      double[] var4 = new double[]{this.transZ};
      var1[2] = var4;
      return new MatrixD(var1);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      double[] var2 = PoseUtils.getAnglesAroundZ(this);
      Object[] var3 = new Object[]{Double.valueOf(this.transX)};
      var1.append(String.format("(XYZ %1$,.2f ", var3));
      Object[] var5 = new Object[]{Double.valueOf(this.transY)};
      var1.append(String.format(" %1$,.2f ", var5));
      Object[] var7 = new Object[]{Double.valueOf(this.transZ)};
      var1.append(String.format(" %1$,.2f mm)", var7));
      Object[] var9 = new Object[]{Double.valueOf(var2[0])};
      var1.append(String.format("(Angles %1$,.2f, ", var9));
      Object[] var11 = new Object[]{Double.valueOf(var2[1])};
      var1.append(String.format(" %1$,.2f, ", var11));
      Object[] var13 = new Object[]{Double.valueOf(var2[2])};
      var1.append(String.format(" %1$,.2f ", var13));
      var1.append('°');
      var1.append(")");
      return var1.toString();
   }
}
