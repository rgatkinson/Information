package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.factory.RobotFactory;
import com.qualcomm.robotcore.robot.Robot;

private class b implements Runnable
{
    final /* synthetic */ FtcRobotControllerService a;
    
    @Override
    public void run() {
    Label_0161:
        while (true) {
            try {
                if (FtcRobotControllerService.b(FtcRobotControllerService.this) != null) {
                    FtcRobotControllerService.b(FtcRobotControllerService.this).shutdown();
                    FtcRobotControllerService.a(FtcRobotControllerService.this, (Robot)null);
                }
                FtcRobotControllerService.a(FtcRobotControllerService.this, "Robot Status: scanning for USB devices");
                try {
                    Thread.sleep(2000L);
                    FtcRobotControllerService.a(FtcRobotControllerService.this, RobotFactory.createRobot());
                    FtcRobotControllerService.a(FtcRobotControllerService.this, "Robot Status: waiting on network");
                    FtcRobotControllerService.c(FtcRobotControllerService.this).reset();
                    Block_8: {
                        while (!FtcRobotControllerService.d(FtcRobotControllerService.this).isConnected()) {
                            final long n = 1000L;
                            Thread.sleep(n);
                            final b b = this;
                            final FtcRobotControllerService ftcRobotControllerService = b.a;
                            final ElapsedTime elapsedTime = FtcRobotControllerService.c(ftcRobotControllerService);
                            final double n2 = elapsedTime.time();
                            final double n3 = 120.0;
                            final double n4 = dcmpl(n2, n3);
                            if (n4 > 0) {
                                break Block_8;
                            }
                        }
                        break Label_0161;
                    }
                    final b b2 = this;
                    final FtcRobotControllerService ftcRobotControllerService2 = b2.a;
                    final String s = "Robot Status: network timed out";
                    FtcRobotControllerService.a(ftcRobotControllerService2, s);
                    return;
                }
                catch (InterruptedException ex3) {
                    FtcRobotControllerService.a(FtcRobotControllerService.this, "Robot Status: abort due to interrupt");
                    return;
                }
            }
            catch (RobotCoreException ex) {
                FtcRobotControllerService.a(FtcRobotControllerService.this, "Robot Status: Unable to create robot!");
                RobotLog.setGlobalErrorMsg(ex.getMessage());
                return;
            }
            try {
                final long n = 1000L;
                Thread.sleep(n);
                final b b = this;
                final FtcRobotControllerService ftcRobotControllerService = b.a;
                final ElapsedTime elapsedTime = FtcRobotControllerService.c(ftcRobotControllerService);
                final double n2 = elapsedTime.time();
                final double n3 = 120.0;
                final double n4 = dcmpl(n2, n3);
                if (n4 > 0) {
                    final b b2 = this;
                    final FtcRobotControllerService ftcRobotControllerService2 = b2.a;
                    final String s = "Robot Status: network timed out";
                    FtcRobotControllerService.a(ftcRobotControllerService2, s);
                    return;
                }
                continue;
            }
            catch (InterruptedException ex4) {
                DbgLog.msg("interrupt waiting for network; aborting setup");
                return;
            }
            break;
        }
        FtcRobotControllerService.a(FtcRobotControllerService.this, "Robot Status: starting robot");
        try {
            FtcRobotControllerService.b(FtcRobotControllerService.this).eventLoopManager.setMonitor((EventLoopManager.EventLoopMonitor)FtcRobotControllerService.e(FtcRobotControllerService.this));
            FtcRobotControllerService.b(FtcRobotControllerService.this).start(FtcRobotControllerService.d(FtcRobotControllerService.this).getGroupOwnerAddress(), FtcRobotControllerService.f(FtcRobotControllerService.this));
        }
        catch (RobotCoreException ex2) {
            FtcRobotControllerService.a(FtcRobotControllerService.this, "Robot Status: failed to start robot");
            RobotLog.setGlobalErrorMsg(ex2.getMessage());
        }
    }
}
