package com.qualcomm.robotcore.util;

import com.qualcomm.robotcore.exception.RobotCoreException;

static final class RobotLog$2 extends Thread {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000L);
                try {
                    RobotLog.v("closing logcat file " + this.a);
                    RunShellCommand.killSpawnedProcess("logcat", this.b, new RunShellCommand());
                }
                catch (RobotCoreException ex) {
                    RobotLog.v("Unable to cancel writing log file to disk: " + ex.toString());
                }
            }
            catch (InterruptedException ex2) {
                continue;
            }
            break;
        }
    }
}