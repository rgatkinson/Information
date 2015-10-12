package com.qualcomm.robotcore.factory;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.robot.Robot;

public class RobotFactory {
   public static Robot createRobot() throws RobotCoreException {
      RobocolDatagramSocket var0 = new RobocolDatagramSocket();
      EventLoopManager var1 = new EventLoopManager(var0);
      Robot var2 = new Robot();
      var2.eventLoopManager = var1;
      var2.socket = var0;
      return var2;
   }
}
