package com.qualcomm.robotcore.factory;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.robot.Robot;

public class RobotFactory
{
    public static Robot createRobot() throws RobotCoreException {
        final RobocolDatagramSocket socket = new RobocolDatagramSocket();
        final EventLoopManager eventLoopManager = new EventLoopManager(socket);
        final Robot robot = new Robot();
        robot.eventLoopManager = eventLoopManager;
        robot.socket = socket;
        return robot;
    }
}
