package com.qualcomm.modernrobotics;

import android.content.Context;
import com.qualcomm.analytics.Analytics;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.util.SerialNumber;

public class ModernRoboticsUsbUtil {
   public static final int DEVICE_ID_DC_MOTOR_CONTROLLER = 77;
   public static final int DEVICE_ID_DEVICE_INTERFACE_MODULE = 65;
   public static final int DEVICE_ID_LEGACY_MODULE = 73;
   public static final int DEVICE_ID_SERVO_CONTROLLER = 83;
   public static final int MFG_CODE_MODERN_ROBOTICS = 77;
   private static Analytics a;

   private static DeviceManager.DeviceType a(byte[] var0) {
      if(var0[1] != 77) {
         return DeviceManager.DeviceType.FTDI_USB_UNKNOWN_DEVICE;
      } else {
         switch(var0[2]) {
         case 65:
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE;
         case 73:
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE;
         case 77:
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER;
         case 83:
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER;
         default:
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_UNKNOWN_DEVICE;
         }
      }
   }

   private static RobotUsbDevice a(RobotUsbManager robotUsbManager, SerialNumber serialNumber) throws RobotCoreException {
      int var2 = robotUsbManager.scanForDevices();
      int var3 = 0;

      String var4;
      boolean var5;
      while(true) {
         if(var3 >= var2) {
            var4 = "";
            var5 = false;
            break;
         }

         if(serialNumber.equals(robotUsbManager.getDeviceSerialNumberByIndex(var3))) {
            var5 = true;
            var4 = robotUsbManager.getDeviceDescriptionByIndex(var3) + " [" + serialNumber.getSerialNumber() + "]";
            break;
         }

         ++var3;
      }

      if(!var5) {
         throwMessage("unable to find USB device with serial number " + serialNumber.toString());
      }

      RobotUsbDevice var6 = null;

      try {
         var6 = robotUsbManager.openBySerialNumber(serialNumber);
         var6.setBaudRate(250000);
         var6.setDataCharacteristics((byte)8, (byte)0, (byte)0);
         var6.setLatencyTimer(2);
      } catch (RobotCoreException e) {
         throwMessage("Unable to open USB device " + serialNumber + " - " + var4 + ": " + e.getMessage());
      }

      try {
         Thread.sleep(400L);
         return var6;
      } catch (InterruptedException var9) {
         return var6;
      }
   }

   private static void throwMessage(String message) throws RobotCoreException {
      System.err.println(message);
      throw new RobotCoreException(message);
   }

   private static byte[] a(RobotUsbDevice var0) throws RobotCoreException {
      byte[] var1 = new byte[5];
      byte[] var2 = new byte[3];
      byte[] var3 = new byte[]{(byte)85, (byte)-86, (byte)-128, (byte)0, (byte)3};

      try {
         var0.purge(RobotUsbDevice.Channel.RX);
         var0.write(var3);
         var0.read(var1);
      } catch (RobotCoreException var5) {
         throwMessage("error reading USB device headers");
      }

      if(!ModernRoboticsPacket.a(var1, 3)) {
         return var2;
      } else {
         var0.read(var2);
         return var2;
      }
   }

   public static DeviceManager.DeviceType getDeviceType(byte[] var0) {
      return a(var0);
   }

   public static byte[] getUsbDeviceHeader(RobotUsbDevice var0) throws RobotCoreException {
      return a(var0);
   }

   public static void init(Context var0, HardwareMap var1) {
      if(a == null) {
         a = new Analytics(var0, "update_rc", var1);
      }

   }

   public static RobotUsbDevice openUsbDevice(RobotUsbManager var0, SerialNumber var1) throws RobotCoreException {
      return a(var0, var1);
   }
}
