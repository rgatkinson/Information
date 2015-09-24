package com.qualcomm.robotcore.util;

import com.qualcomm.robotcore.exception.RobotCoreException;

static final class RobotLog$1 extends Thread {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ int c;
    
    @Override
    public void run() {
        try {
            RobotLog.v("saving logcat to " + this.a);
            final RunShellCommand runShellCommand = new RunShellCommand();
            RunShellCommand.killSpawnedProcess("logcat", this.b, runShellCommand);
            runShellCommand.run(String.format("logcat -f %s -r%d -n%d -v time %s", this.a, this.c, 1, "UsbRequestJNI:S UsbRequest:S *:V"));
        }
        catch (RobotCoreException ex) {
            RobotLog.v("Error while writing log file to disk: " + ex.toString());
        }
        finally {
            RobotLog.a(false);
        }
    }
}