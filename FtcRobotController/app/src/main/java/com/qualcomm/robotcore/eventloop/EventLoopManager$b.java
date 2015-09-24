package com.qualcomm.robotcore.eventloop;

import java.util.Iterator;
import java.net.InetAddress;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

private class b implements Runnable
{
    @Override
    public void run() {
        RobotLog.v("EventLoopRunnable has started");
        while (true) {
            try {
                final ElapsedTime elapsedTime = new ElapsedTime();
                if (!Thread.interrupted()) {
                    while (elapsedTime.time() < 0.001) {
                        Thread.sleep(5L);
                    }
                    goto Label_0058;
                }
                goto Label_0045;
            }
            catch (InterruptedException ex3) {
                RobotLog.v("EventLoopRunnable interrupted");
            }
            catch (RobotCoreException ex) {
                RobotLog.v("RobotCoreException in EventLoopManager: " + ex.getMessage());
                EventLoopManager.a(EventLoopManager.this, State.EMERGENCY_STOP);
                EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                goto Label_0045;
            }
            if (EventLoopManager.e(EventLoopManager.this).time() > 2.0) {
                EventLoopManager.this.handleDroppedConnection();
                EventLoopManager.a(EventLoopManager.this, (InetAddress)null);
                EventLoopManager.a(EventLoopManager.this, new ElapsedTime(0L));
                goto Label_0101;
            }
            goto Label_0101;
            try {
                EventLoopManager.g(EventLoopManager.this).loop();
                final Iterator<SyncdDevice> iterator = EventLoopManager.f(EventLoopManager.this).iterator();
                while (iterator.hasNext()) {
                    iterator.next().startBlockingWork();
                }
                continue;
            }
            catch (Exception ex2) {
                RobotLog.e("Event loop threw an exception");
                RobotLog.logStacktrace(ex2);
                RobotLog.setGlobalErrorMsg("User code threw an uncaught exception: " + ex2.getMessage());
                EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                throw new RobotCoreException("EventLoop Exception in loop()");
            }
            finally {
                final Iterator<SyncdDevice> iterator2 = EventLoopManager.f(EventLoopManager.this).iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().startBlockingWork();
                }
            }
            break;
        }
    }
}
