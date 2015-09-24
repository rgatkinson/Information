package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Color;
import android.view.View;

class ColorSensorDriver$1 implements Runnable {
    final /* synthetic */ View val$relativeLayout;
    final /* synthetic */ float[] val$values;
    
    @Override
    public void run() {
        this.val$relativeLayout.setBackgroundColor(Color.HSVToColor(255, this.val$values));
    }
}