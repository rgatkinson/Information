package com.qualcomm.robotcore.sensor;

import java.util.Iterator;
import com.qualcomm.robotcore.util.PoseUtils;
import com.qualcomm.robotcore.util.MatrixD;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.qualcomm.robotcore.util.Pose;

public class SensorImageLocalizer extends SensorBase<Pose> implements SensorListener<List<TrackedTargetInfo>>
{
    private final boolean a;
    private final String b;
    private final Map<String, TargetInfo> c;
    private Pose d;
    private final HashMap<String, a> e;
    private a f;
    
    public SensorImageLocalizer(final List<SensorListener<Pose>> list) {
        super(list);
        this.a = false;
        this.b = "SensorImageLocalizer";
        this.e = new HashMap<String, a>();
        this.c = new HashMap<String, TargetInfo>();
    }
    
    private boolean a(final TrackedTargetInfo trackedTargetInfo) {
        final long n = System.currentTimeMillis() / 1000L;
        a a;
        if (this.e.containsKey(trackedTargetInfo.mTargetInfo.mTargetName)) {
            a = this.e.get(trackedTargetInfo.mTargetInfo.mTargetName);
            a.b = trackedTargetInfo.mTimeTracked;
            a.e = trackedTargetInfo.mConfidence;
            if (n - a.b > 120L) {
                a.c = 1;
            }
            else {
                ++a.c;
            }
        }
        else {
            a = new a();
            a.e = trackedTargetInfo.mConfidence;
            a.d = trackedTargetInfo.mTargetInfo.mTargetName;
            a.b = trackedTargetInfo.mTimeTracked;
            a.c = 1;
            this.e.put(trackedTargetInfo.mTargetInfo.mTargetName, a);
        }
        if (this.f != null && this.f.d != a.d && n - this.f.a < 10L) {
            Log.d("SensorImageLocalizer", "Ignoring target " + trackedTargetInfo.mTargetInfo.mTargetName + " Time diff " + (n - this.f.a));
            return false;
        }
        return true;
    }
    
    public void AddListener(final SensorListener<Pose> sensorListener) {
        synchronized (this.mListeners) {
            if (!this.mListeners.contains(sensorListener)) {
                this.mListeners.add((SensorListener<T>)sensorListener);
            }
        }
    }
    
    public void RemoveListener(final SensorListener<Pose> sensorListener) {
        synchronized (this.mListeners) {
            if (this.mListeners.contains(sensorListener)) {
                this.mListeners.remove(sensorListener);
            }
        }
    }
    
    public boolean addRobotToCameraRef(final double n, final double n2, final double n3, final double n4) {
        new MatrixD(3, 3);
        final MatrixD rotationY = Pose.makeRotationY(-n4);
        final MatrixD matrixD = new MatrixD(3, 4);
        matrixD.setSubmatrix(rotationY, 3, 3, 0, 0);
        matrixD.data()[0][3] = n2;
        matrixD.data()[1][3] = -n3;
        matrixD.data()[2][3] = n;
        this.d = new Pose(matrixD);
        return true;
    }
    
    public boolean addTargetReference(final String s, final double n, final double n2, final double n3, final double n4, final double n5, final double n6) {
        if (s == null) {
            throw new IllegalArgumentException("Null targetInfoWorldRef");
        }
        if (this.c.containsKey(s)) {
            return false;
        }
        final MatrixD rotationY = Pose.makeRotationY(Math.toRadians(n4));
        final MatrixD matrixD = new MatrixD(3, 4);
        matrixD.setSubmatrix(rotationY, 3, 3, 0, 0);
        matrixD.data()[0][3] = n2;
        matrixD.data()[1][3] = n3;
        matrixD.data()[2][3] = n;
        final Pose pose = new Pose(matrixD);
        Log.d("SensorImageLocalizer", "Target Pose \n" + matrixD);
        this.c.put(s, new TargetInfo(s, pose, new TargetSize(s, n5, n6)));
        return true;
    }
    
    @Override
    public boolean initialize() {
        return true;
    }
    
    @Override
    public void onUpdate(final List<TrackedTargetInfo> list) {
        Log.d("SensorImageLocalizer", "SensorImageLocalizer onUpdate");
        if (list == null || list.size() < 1) {
            Log.d("SensorImageLocalizer", "SensorImageLocalizer onUpdate NULL");
            this.update(null);
            return;
        }
        int n = 0;
        double n2 = Double.MIN_VALUE;
        final long a = System.currentTimeMillis() / 1000L;
        TrackedTargetInfo trackedTargetInfo = null;
        a f = null;
    Label_0204_Outer:
        for (TrackedTargetInfo trackedTargetInfo2 : list) {
            while (true) {
                Label_0268: {
                    if (!this.c.containsKey(trackedTargetInfo2.mTargetInfo.mTargetName)) {
                        break Label_0268;
                    }
                    if (!this.a(trackedTargetInfo2) || trackedTargetInfo2.mConfidence <= n2) {
                        Log.d("SensorImageLocalizer", "Ignoring target " + trackedTargetInfo2.mTargetInfo.mTargetName + " Confidence " + trackedTargetInfo2.mConfidence);
                        break Label_0268;
                    }
                    f = this.e.get(trackedTargetInfo2.mTargetInfo.mTargetName);
                    final double mConfidence = trackedTargetInfo2.mConfidence;
                    final int n3 = 1;
                    Log.d("SensorImageLocalizer", "Potential target " + trackedTargetInfo2.mTargetInfo.mTargetName + " Confidence " + trackedTargetInfo2.mConfidence);
                    final double n4 = mConfidence;
                    trackedTargetInfo = trackedTargetInfo2;
                    n = n3;
                    n2 = n4;
                    continue Label_0204_Outer;
                }
                trackedTargetInfo2 = trackedTargetInfo;
                final double n5 = n2;
                final int n3 = n;
                final double mConfidence = n5;
                continue;
            }
        }
        if (n == 0) {
            this.update(null);
            return;
        }
        final TargetInfo targetInfo = this.c.get(trackedTargetInfo.mTargetInfo.mTargetName);
        f.a = a;
        this.f = f;
        Log.d("SensorImageLocalizer", "Selected target " + trackedTargetInfo.mTargetInfo.mTargetName + " time " + a);
        final Pose d = this.d;
        MatrixD submatrix = null;
        if (d != null) {
            submatrix = this.d.poseMatrix.submatrix(3, 3, 0, 0);
        }
        final MatrixD transpose = trackedTargetInfo.mTargetInfo.mTargetPose.poseMatrix.submatrix(3, 3, 0, 0).transpose();
        final MatrixD submatrix2 = targetInfo.mTargetPose.poseMatrix.submatrix(3, 3, 0, 0);
        final MatrixD times = Pose.makeRotationX(Math.toRadians(90.0)).times(Pose.makeRotationY(Math.toRadians(90.0)));
        final MatrixD times2 = times.times(submatrix2).times(transpose);
        MatrixD times3;
        if (submatrix != null) {
            times3 = times2.times(submatrix);
        }
        else {
            times3 = times2;
        }
        final MatrixD matrixD = new MatrixD(3, 1);
        matrixD.data()[0][0] = targetInfo.mTargetSize.mLongSide;
        matrixD.data()[1][0] = targetInfo.mTargetSize.mShortSide;
        matrixD.data()[2][0] = 0.0;
        final MatrixD times4 = transpose.times(trackedTargetInfo.mTargetInfo.mTargetPose.getTranslationMatrix());
        MatrixD translationMatrix = new MatrixD(3, 1);
        if (this.d != null) {
            translationMatrix = this.d.getTranslationMatrix();
        }
        final MatrixD times5 = times.times(targetInfo.mTargetPose.getTranslationMatrix().subtract(submatrix2.times(times4.add(transpose.times(translationMatrix)).add(matrixD))));
        final MatrixD matrixD2 = new MatrixD(3, 4);
        matrixD2.setSubmatrix(times3, 3, 3, 0, 0);
        matrixD2.setSubmatrix(times5, 3, 1, 0, 3);
        final Pose pose = new Pose(matrixD2);
        final double[] anglesAroundZ = PoseUtils.getAnglesAroundZ(pose);
        Log.d("SensorImageLocalizer", String.format("POSE_HEADING: x %8.4f z %8.4f up %8.4f", anglesAroundZ[0], anglesAroundZ[1], anglesAroundZ[2]));
        final MatrixD translationMatrix2 = pose.getTranslationMatrix();
        Log.d("SensorImageLocalizer", String.format("POSE_TRANS: x %8.4f y %8.4f z %8.4f", translationMatrix2.data()[0][0], translationMatrix2.data()[1][0], translationMatrix2.data()[2][0]));
        this.update(pose);
    }
    
    @Override
    public boolean pause() {
        return true;
    }
    
    public boolean removeTargetReference(final String s) {
        if (s == null) {
            throw new IllegalArgumentException("Null targetName");
        }
        if (this.c.containsKey(s)) {
            this.c.remove(s);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean resume() {
        return true;
    }
    
    @Override
    public boolean shutdown() {
        return true;
    }
    
    private class a
    {
        public long a;
        public long b;
        public int c;
        public String d;
        public double e;
    }
}
