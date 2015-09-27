package com.qualcomm.modernrobotics;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;

public class ModernRoboticsPacket {
   static boolean validatePacket(byte[] packet, int cbExpected) {
      return validatePacketInternal(packet, cbExpected);
   }

   private static boolean validatePacketInternal(byte[] packet, int cbExpected) {
      if(packet.length < 5) {
         RobotLog.w("header length is too short");
         return false;
      } else if(packet[0] == 51 /*0x33*/ && packet[1] == -52 /*0xCC*/) {
         if(TypeConversion.unsignedByteToInt(packet[4]) != cbExpected) {
            RobotLog.w("header reported unexpected packet size");
            return false;
         } else {
            return true;
         }
      } else {
         RobotLog.w("header sync bytes are incorrect");
         return false;
      }
   }
}
