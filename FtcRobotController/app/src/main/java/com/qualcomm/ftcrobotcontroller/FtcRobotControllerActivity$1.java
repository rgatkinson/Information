package com.qualcomm.ftcrobotcontroller;

import com.qualcomm.ftccommon.FtcRobotControllerService;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;

class FtcRobotControllerActivity$1 implements ServiceConnection {
    public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
        FtcRobotControllerActivity.this.onServiceBind(((FtcRobotControllerService.FtcRobotControllerBinder)binder).getService());
    }
    
    public void onServiceDisconnected(final ComponentName componentName) {
        FtcRobotControllerActivity.this.controllerService = null;
    }
}