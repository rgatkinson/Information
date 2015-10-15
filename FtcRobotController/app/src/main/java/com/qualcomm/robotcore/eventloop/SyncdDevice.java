package com.qualcomm.robotcore.eventloop;

import com.qualcomm.robotcore.exception.RobotCoreException;

public interface SyncdDevice {

   // Block activity for a while. Used to turn off communication
   // with the device before the OpMode loop() is called.
   void blockUntilReady() throws RobotCoreException, InterruptedException;

   // Called to resume work after loop() is called
   void startBlockingWork();
}
