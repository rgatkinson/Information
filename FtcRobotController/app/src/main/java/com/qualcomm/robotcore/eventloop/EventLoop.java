package com.qualcomm.robotcore.eventloop;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.Command;

public interface EventLoop {
   OpModeManager getOpModeManager();

   void init(EventLoopManager var1) throws RobotCoreException, InterruptedException;

   void loop() throws RobotCoreException, InterruptedException;

   void processCommand(Command var1);

   void teardown() throws RobotCoreException, InterruptedException;
}
