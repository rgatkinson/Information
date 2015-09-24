package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.EventLoopManager;

public interface HardwareFactory
{
    HardwareMap createHardwareMap(EventLoopManager p0) throws RobotCoreException, InterruptedException;
}
