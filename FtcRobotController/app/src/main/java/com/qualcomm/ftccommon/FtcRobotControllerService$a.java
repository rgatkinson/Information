package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.eventloop.EventLoopManager;

private class a implements EventLoopMonitor
{
    @Override
    public void onStateChange(final State state) {
        if (FtcRobotControllerService.a(FtcRobotControllerService.this) == null) {
            return;
        }
        switch (FtcRobotControllerService$1.a[state.ordinal()]) {
            default: {}
            case 1: {
                FtcRobotControllerService.a(FtcRobotControllerService.this).robotUpdate("Robot Status: init");
            }
            case 2: {
                FtcRobotControllerService.a(FtcRobotControllerService.this).robotUpdate("Robot Status: not started");
            }
            case 3: {
                FtcRobotControllerService.a(FtcRobotControllerService.this).robotUpdate("Robot Status: running");
            }
            case 4: {
                FtcRobotControllerService.a(FtcRobotControllerService.this).robotUpdate("Robot Status: stopped");
            }
            case 5: {
                FtcRobotControllerService.a(FtcRobotControllerService.this).robotUpdate("Robot Status: EMERGENCY STOP");
            }
            case 6: {
                FtcRobotControllerService.a(FtcRobotControllerService.this).robotUpdate("Robot Status: dropped connection");
            }
        }
    }
}
