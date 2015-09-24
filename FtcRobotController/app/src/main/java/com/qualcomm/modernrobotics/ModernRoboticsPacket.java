package com.qualcomm.modernrobotics;

import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.RobotLog;

public class ModernRoboticsPacket
{
    static boolean a(final byte[] array, final int n) {
        return b(array, n);
    }
    
    private static boolean b(final byte[] array, final int n) {
        if (array.length < 5) {
            RobotLog.w("header length is too short");
            return false;
        }
        if (array[0] != 51 || array[1] != -52) {
            RobotLog.w("header sync bytes are incorrect");
            return false;
        }
        if (TypeConversion.unsignedByteToInt(array[4]) != n) {
            RobotLog.w("header reported unexpected packet size");
            return false;
        }
        return true;
    }
}
