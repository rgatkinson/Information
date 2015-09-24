package com.qualcomm.ftccommon;

import android.os.Binder;

public class FtcRobotControllerBinder extends Binder
{
    public FtcRobotControllerService getService() {
        return FtcRobotControllerService.this;
    }
}
