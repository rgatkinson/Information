package com.qualcomm.ftcrobotcontroller;

import android.app.ActionBar$OnMenuVisibilityListener;

class FtcRobotControllerActivity$4 implements ActionBar$OnMenuVisibilityListener {
    public void onMenuVisibilityChanged(final boolean b) {
        if (b) {
            FtcRobotControllerActivity.this.dimmer.handleDimTimer();
        }
    }
}