package com.qualcomm.robotcore.eventloop;

import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ElapsedTime;

private class c implements Runnable
{
    ElapsedTime a;
    
    private c() {
        this.a = new ElapsedTime();
    }
    
    @Override
    public void run() {
        while (true) {
            final RobocolDatagram recv = EventLoopManager.b(EventLoopManager.this).recv();
            if (EventLoopManager.c(EventLoopManager.this) || EventLoopManager.b(EventLoopManager.this).isClosed()) {
                break;
            }
            if (recv == null) {
                Thread.yield();
            }
            else {
                if (RobotLog.hasGlobalErrorMsg()) {
                    EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                }
                Label_0192: {
                    Label_0181: {
                        Label_0170: {
                            Label_0159: {
                                Label_0148: {
                                    try {
                                        switch (EventLoopManager$1.a[recv.getMsgType().ordinal()]) {
                                            default: {
                                                EventLoopManager.e(EventLoopManager.this, recv);
                                                continue;
                                            }
                                            case 1: {
                                                break Label_0148;
                                            }
                                            case 2: {
                                                break Label_0159;
                                            }
                                            case 3: {
                                                break Label_0170;
                                            }
                                            case 4: {
                                                break Label_0181;
                                            }
                                            case 5: {
                                                break Label_0192;
                                            }
                                        }
                                    }
                                    catch (RobotCoreException ex) {
                                        RobotLog.w("RobotCore event loop cannot process event: " + ex.toString());
                                    }
                                    continue;
                                }
                                EventLoopManager.a(EventLoopManager.this, recv);
                                continue;
                            }
                            EventLoopManager.b(EventLoopManager.this, recv);
                            continue;
                        }
                        EventLoopManager.c(EventLoopManager.this, recv);
                        continue;
                    }
                    EventLoopManager.d(EventLoopManager.this, recv);
                    continue;
                }
                EventLoopManager.d(EventLoopManager.this);
            }
        }
    }
}
