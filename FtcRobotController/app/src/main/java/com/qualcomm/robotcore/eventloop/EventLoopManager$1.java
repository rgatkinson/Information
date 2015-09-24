package com.qualcomm.robotcore.eventloop;

import com.qualcomm.robotcore.robocol.RobocolParsable;

static class EventLoopManager$1 {
    static {
        a = new int[RobocolParsable.MsgType.values().length];
        while (true) {
            try {
                EventLoopManager$1.a[RobocolParsable.MsgType.GAMEPAD.ordinal()] = 1;
                try {
                    EventLoopManager$1.a[RobocolParsable.MsgType.HEARTBEAT.ordinal()] = 2;
                    try {
                        EventLoopManager$1.a[RobocolParsable.MsgType.PEER_DISCOVERY.ordinal()] = 3;
                        try {
                            EventLoopManager$1.a[RobocolParsable.MsgType.COMMAND.ordinal()] = 4;
                            try {
                                EventLoopManager$1.a[RobocolParsable.MsgType.EMPTY.ordinal()] = 5;
                            }
                            catch (NoSuchFieldError noSuchFieldError) {}
                        }
                        catch (NoSuchFieldError noSuchFieldError2) {}
                    }
                    catch (NoSuchFieldError noSuchFieldError3) {}
                }
                catch (NoSuchFieldError noSuchFieldError4) {}
            }
            catch (NoSuchFieldError noSuchFieldError5) {
                continue;
            }
            break;
        }
    }
}