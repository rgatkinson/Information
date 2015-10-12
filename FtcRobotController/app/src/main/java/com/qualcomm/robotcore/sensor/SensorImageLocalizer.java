package com.qualcomm.robotcore.sensor;

import android.util.Log;
import com.qualcomm.robotcore.sensor.SensorBase;
import com.qualcomm.robotcore.sensor.SensorListener;
import com.qualcomm.robotcore.sensor.TargetInfo;
import com.qualcomm.robotcore.sensor.TargetSize;
import com.qualcomm.robotcore.sensor.TrackedTargetInfo;
import com.qualcomm.robotcore.util.MatrixD;
import com.qualcomm.robotcore.util.Pose;
import com.qualcomm.robotcore.util.PoseUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SensorImageLocalizer extends SensorBase<Pose> implements SensorListener<List<TrackedTargetInfo>> {
   private final boolean a = false;
   private final String b = "SensorImageLocalizer";
   private final Map<String, TargetInfo> c = new HashMap();
   private Pose d;
   private final HashMap<String, SensorImageLocalizer.a> e = new HashMap();
   private SensorImageLocalizer.a f;

   public SensorImageLocalizer(List<SensorListener<Pose>> var1) {
      super(var1);
   }

   private boolean a(TrackedTargetInfo var1) {
      long var2 = System.currentTimeMillis() / 1000L;
      SensorImageLocalizer.a var4;
      if(this.e.containsKey(var1.mTargetInfo.mTargetName)) {
         var4 = (SensorImageLocalizer.a)this.e.get(var1.mTargetInfo.mTargetName);
         var4.b = var1.mTimeTracked;
         var4.e = var1.mConfidence;
         if(var2 - var4.b > 120L) {
            var4.c = 1;
         } else {
            ++var4.c;
         }
      } else {
         var4 = new SensorImageLocalizer.a(null);
         var4.e = var1.mConfidence;
         var4.d = var1.mTargetInfo.mTargetName;
         var4.b = var1.mTimeTracked;
         var4.c = 1;
         this.e.put(var1.mTargetInfo.mTargetName, var4);
      }

      if(this.f != null && this.f.d != var4.d && var2 - this.f.a < 10L) {
         Log.d("SensorImageLocalizer", "Ignoring target " + var1.mTargetInfo.mTargetName + " Time diff " + (var2 - this.f.a));
         return false;
      } else {
         return true;
      }
   }

   public void AddListener(SensorListener<Pose> param1) {
      // $FF: Couldn't be decompiled
   }

   public void RemoveListener(SensorListener<Pose> param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean addRobotToCameraRef(double var1, double var3, double var5, double var7) {
      new MatrixD(3, 3);
      MatrixD var10 = Pose.makeRotationY(-var7);
      MatrixD var11 = new MatrixD(3, 4);
      var11.setSubmatrix(var10, 3, 3, 0, 0);
      var11.data()[0][3] = var3;
      var11.data()[1][3] = -var5;
      var11.data()[2][3] = var1;
      this.d = new Pose(var11);
      return true;
   }

   public boolean addTargetReference(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      if(var1 == null) {
         throw new IllegalArgumentException("Null targetInfoWorldRef");
      } else if(this.c.containsKey(var1)) {
         return false;
      } else {
         MatrixD var14 = Pose.makeRotationY(Math.toRadians(var8));
         MatrixD var15 = new MatrixD(3, 4);
         var15.setSubmatrix(var14, 3, 3, 0, 0);
         var15.data()[0][3] = var4;
         var15.data()[1][3] = var6;
         var15.data()[2][3] = var2;
         Pose var17 = new Pose(var15);
         Log.d("SensorImageLocalizer", "Target Pose \n" + var15);
         TargetInfo var19 = new TargetInfo(var1, var17, new TargetSize(var1, var10, var12));
         this.c.put(var1, var19);
         return true;
      }
   }

   public boolean initialize() {
      return true;
   }

   public void onUpdate(List<TrackedTargetInfo> var1) {
      Log.d("SensorImageLocalizer", "SensorImageLocalizer onUpdate");
      if(var1 != null && var1.size() >= 1) {
         boolean var4 = false;
         double var5 = Double.MIN_VALUE;
         long var7 = System.currentTimeMillis() / 1000L;
         TrackedTargetInfo var9 = null;
         SensorImageLocalizer.a var10 = null;

         double var40;
         for(Iterator var11 = var1.iterator(); var11.hasNext(); var5 = var40) {
            TrackedTargetInfo var36;
            boolean var39;
            label40: {
               var36 = (TrackedTargetInfo)var11.next();
               if(this.c.containsKey(var36.mTargetInfo.mTargetName)) {
                  if(this.a(var36) && var36.mConfidence > var5) {
                     var10 = (SensorImageLocalizer.a)this.e.get(var36.mTargetInfo.mTargetName);
                     var40 = var36.mConfidence;
                     var39 = true;
                     Log.d("SensorImageLocalizer", "Potential target " + var36.mTargetInfo.mTargetName + " Confidence " + var36.mConfidence);
                     break label40;
                  }

                  Log.d("SensorImageLocalizer", "Ignoring target " + var36.mTargetInfo.mTargetName + " Confidence " + var36.mConfidence);
               }

               var36 = var9;
               var39 = var4;
               var40 = var5;
            }

            var9 = var36;
            var4 = var39;
         }

         if(!var4) {
            this.update((Object)null);
         } else {
            TargetInfo var12 = (TargetInfo)this.c.get(var9.mTargetInfo.mTargetName);
            var10.a = var7;
            this.f = var10;
            Log.d("SensorImageLocalizer", "Selected target " + var9.mTargetInfo.mTargetName + " time " + var7);
            Pose var14 = this.d;
            MatrixD var15 = null;
            if(var14 != null) {
               var15 = this.d.poseMatrix.submatrix(3, 3, 0, 0);
            }

            MatrixD var16 = var9.mTargetInfo.mTargetPose.poseMatrix.submatrix(3, 3, 0, 0).transpose();
            MatrixD var17 = var12.mTargetPose.poseMatrix.submatrix(3, 3, 0, 0);
            MatrixD var18 = Pose.makeRotationX(Math.toRadians(90.0D)).times(Pose.makeRotationY(Math.toRadians(90.0D)));
            MatrixD var19 = var18.times(var17).times(var16);
            MatrixD var20;
            if(var15 != null) {
               var20 = var19.times(var15);
            } else {
               var20 = var19;
            }

            MatrixD var21 = new MatrixD(3, 1);
            var21.data()[0][0] = var12.mTargetSize.mLongSide;
            var21.data()[1][0] = var12.mTargetSize.mShortSide;
            var21.data()[2][0] = 0.0D;
            MatrixD var22 = var16.times(var9.mTargetInfo.mTargetPose.getTranslationMatrix());
            MatrixD var23 = new MatrixD(3, 1);
            if(this.d != null) {
               var23 = this.d.getTranslationMatrix();
            }

            MatrixD var24 = var17.times(var22.add(var16.times(var23)).add(var21));
            MatrixD var25 = var18.times(var12.mTargetPose.getTranslationMatrix().subtract(var24));
            MatrixD var26 = new MatrixD(3, 4);
            var26.setSubmatrix(var20, 3, 3, 0, 0);
            var26.setSubmatrix(var25, 3, 1, 0, 3);
            Pose var29 = new Pose(var26);
            double[] var30 = PoseUtils.getAnglesAroundZ(var29);
            Object[] var31 = new Object[]{Double.valueOf(var30[0]), Double.valueOf(var30[1]), Double.valueOf(var30[2])};
            Log.d("SensorImageLocalizer", String.format("POSE_HEADING: x %8.4f z %8.4f up %8.4f", var31));
            MatrixD var33 = var29.getTranslationMatrix();
            Object[] var34 = new Object[]{Double.valueOf(var33.data()[0][0]), Double.valueOf(var33.data()[1][0]), Double.valueOf(var33.data()[2][0])};
            Log.d("SensorImageLocalizer", String.format("POSE_TRANS: x %8.4f y %8.4f z %8.4f", var34));
            this.update(var29);
         }
      } else {
         Log.d("SensorImageLocalizer", "SensorImageLocalizer onUpdate NULL");
         this.update((Object)null);
      }
   }

   public boolean pause() {
      return true;
   }

   public boolean removeTargetReference(String var1) {
      if(var1 == null) {
         throw new IllegalArgumentException("Null targetName");
      } else if(this.c.containsKey(var1)) {
         this.c.remove(var1);
         return true;
      } else {
         return false;
      }
   }

   public boolean resume() {
      return true;
   }

   public boolean shutdown() {
      return true;
   }

   private class a {
      public long a;
      public long b;
      public int c;
      public String d;
      public double e;

      private a() {
      }

      // $FF: synthetic method
      a(Object var2) {
         this();
      }
   }
}
