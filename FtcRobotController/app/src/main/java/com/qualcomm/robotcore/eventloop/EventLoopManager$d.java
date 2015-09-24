package com.qualcomm.robotcore.eventloop;

import java.util.Iterator;
import java.util.Collection;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.HashSet;
import com.qualcomm.robotcore.robocol.Command;
import java.util.Set;

private class d implements Runnable
{
    private Set<Command> b;
    
    private d() {
        this.b = new HashSet<Command>();
    }
    
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            for (final Command command : EventLoopManager.a(EventLoopManager.this)) {
                if (command.getAttempts() > 10) {
                    RobotLog.w("Failed to send command, too many attempts: " + command.toString());
                    this.b.add(command);
                }
                else if (command.isAcknowledged()) {
                    RobotLog.v("Command " + command.getName() + " has been acknowledged by remote device");
                    this.b.add(command);
                }
                else {
                    try {
                        RobotLog.v("Sending command: " + command.getName() + ", attempt " + command.getAttempts());
                        EventLoopManager.b(EventLoopManager.this).send(new RobocolDatagram(command.toByteArray()));
                    }
                    catch (RobotCoreException ex) {
                        RobotLog.w("Failed to send command " + command.getName());
                        RobotLog.logStacktrace(ex);
                    }
                }
            }
            EventLoopManager.a(EventLoopManager.this).removeAll(this.b);
            this.b.clear();
            try {
                Thread.sleep(100L);
                continue;
            }
            catch (InterruptedException ex2) {}
            break;
        }
    }
}
