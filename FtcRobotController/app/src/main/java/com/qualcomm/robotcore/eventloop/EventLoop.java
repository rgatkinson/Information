package com.qualcomm.robotcore.eventloop;

import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;

public interface EventLoop
{
    OpModeManager getOpModeManager();
    
    void init(EventLoopManager p0) throws RobotCoreException, InterruptedException;
    
    void loop() throws RobotCoreException, InterruptedException;
    
    void processCommand(Command p0);
    
    void teardown() throws RobotCoreException, InterruptedException;
}
