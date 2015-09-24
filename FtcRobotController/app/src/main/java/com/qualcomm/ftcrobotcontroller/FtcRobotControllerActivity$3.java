package com.qualcomm.ftcrobotcontroller;

import android.view.MotionEvent;
import android.view.View;
import android.view.View$OnTouchListener;

class FtcRobotControllerActivity$3 implements View$OnTouchListener {
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        FtcRobotControllerActivity.this.dimmer.handleDimTimer();
        return false;
    }
}