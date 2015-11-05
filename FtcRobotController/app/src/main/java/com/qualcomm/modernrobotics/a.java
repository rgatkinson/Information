package com.qualcomm.modernrobotics;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;

class a {
   static boolean a(byte[] var0, int var1) {
      if(var0.length < 5) {
         RobotLog.w("Modern Robotics USB header length is too short");
         return false;
      } else if(var0[0] == 51 && var0[1] == -52) {
         if(TypeConversion.unsignedByteToInt(var0[4]) != var1) {
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
