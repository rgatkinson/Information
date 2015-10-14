package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;

public interface HardwareFactory {
    HardwareMap createHardwareMap(EventLoopManager var1) throws RobotCoreException, InterruptedException;
}
