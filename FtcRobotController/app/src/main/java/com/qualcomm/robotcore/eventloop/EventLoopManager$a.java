package com.qualcomm.robotcore.eventloop;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;

private static class a implements EventLoop
{
    @Override
    public OpModeManager getOpModeManager() {
        return null;
    }
    
    @Override
    public void init(final EventLoopManager eventLoopManager) {
    }
    
    @Override
    public void loop() {
    }
    
    @Override
    public void processCommand(final Command command) {
        RobotLog.w("Dropping command " + command.getName() + ", no active event loop");
    }
    
    @Override
    public void teardown() {
    }
}
