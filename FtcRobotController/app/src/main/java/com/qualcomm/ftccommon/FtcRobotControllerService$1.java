package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;

static class FtcRobotControllerService$1 {
    static {
        b = new int[Event.values().length];
        while (true) {
            try {
                FtcRobotControllerService$1.b[Event.CONNECTED_AS_GROUP_OWNER.ordinal()] = 1;
                try {
                    FtcRobotControllerService$1.b[Event.CONNECTED_AS_PEER.ordinal()] = 2;
                    try {
                        FtcRobotControllerService$1.b[Event.CONNECTION_INFO_AVAILABLE.ordinal()] = 3;
                        try {
                            FtcRobotControllerService$1.b[Event.ERROR.ordinal()] = 4;
                            a = new int[EventLoopManager.State.values().length];
                            try {
                                FtcRobotControllerService$1.a[EventLoopManager.State.INIT.ordinal()] = 1;
                                try {
                                    FtcRobotControllerService$1.a[EventLoopManager.State.NOT_STARTED.ordinal()] = 2;
                                    try {
                                        FtcRobotControllerService$1.a[EventLoopManager.State.RUNNING.ordinal()] = 3;
                                        try {
                                            FtcRobotControllerService$1.a[EventLoopManager.State.STOPPED.ordinal()] = 4;
                                            try {
                                                FtcRobotControllerService$1.a[EventLoopManager.State.EMERGENCY_STOP.ordinal()] = 5;
                                                try {
                                                    FtcRobotControllerService$1.a[EventLoopManager.State.DROPPED_CONNECTION.ordinal()] = 6;
                                                }
                                                catch (NoSuchFieldError noSuchFieldError) {}
                                            }
                                            catch (NoSuchFieldError noSuchFieldError2) {}
                                        }
                                        catch (NoSuchFieldError noSuchFieldError3) {}
                                    }
                                    catch (NoSuchFieldError noSuchFieldError4) {}
                                }
                                catch (NoSuchFieldError noSuchFieldError5) {}
                            }
                            catch (NoSuchFieldError noSuchFieldError6) {}
                        }
                        catch (NoSuchFieldError noSuchFieldError7) {}
                    }
                    catch (NoSuchFieldError noSuchFieldError8) {}
                }
                catch (NoSuchFieldError noSuchFieldError9) {}
            }
            catch (NoSuchFieldError noSuchFieldError10) {
                continue;
            }
            break;
        }
    }
}