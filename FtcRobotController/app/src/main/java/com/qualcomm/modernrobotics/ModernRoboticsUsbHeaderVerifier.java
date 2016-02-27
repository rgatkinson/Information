package com.qualcomm.modernrobotics;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;

class ModernRoboticsUsbHeaderVerifier
   {
   static boolean verifyHeader(byte[] header, int cbPacketExpected) {
      if(header.length < 5) {
         RobotLog.w("Modern Robotics USB header length is too short");
         return false;
      } else if(header[0] == 51 && header[1] == -52) {
         if(TypeConversion.unsignedByteToInt(header[4]) != cbPacketExpected) {
            RobotLog.w("Modern Robotics USB header reported unexpected packet size");
            return false;
         } else {
            return true;
         }
      } else {
         RobotLog.w("Modern Robotics USB header sync bytes are incorrect");
         return false;
      }
   }
}
