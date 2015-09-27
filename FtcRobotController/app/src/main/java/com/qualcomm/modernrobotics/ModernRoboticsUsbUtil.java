package com.qualcomm.modernrobotics;

import android.content.Context;
import com.qualcomm.analytics.Analytics;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceManager;
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

   private static RobotUsbDevice a(RobotUsbManager var0, SerialNumber var1) throws RobotCoreException {
      int var2 = var0.scanForDevices();
      int var3 = 0;

      String var4;
      boolean var5;
      while(true) {
         if(var3 >= var2) {
            var4 = "";
            var5 = false;
            break;
         }

         if(var1.equals(var0.getDeviceSerialNumberByIndex(var3))) {
            var5 = true;
            var4 = var0.getDeviceDescriptionByIndex(var3) + " [" + var1.getSerialNumber() + "]";
            break;
         }

         ++var3;
      }

      if(!var5) {
         a("unable to find USB device with serial number " + var1.toString());
      }

      RobotUsbDevice var6 = null;

      try {
         var6 = var0.openBySerialNumber(var1);
         var6.setBaudRate(250000);
         var6.setDataCharacteristics((byte)8, (byte)0, (byte)0);
         var6.setLatencyTimer(2);
      } catch (RobotCoreException var10) {
         a("Unable to open USB device " + var1 + " - " + var4 + ": " + var10.getMessage());
      }

      try {
         Thread.sleep(400L);
         return var6;
      } catch (InterruptedException var9) {
         return var6;
      }
   }

   private static void a(String var0) throws RobotCoreException {
      System.err.println(var0);
      throw new RobotCoreException(var0);
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
         a("error reading USB device headers");
      }

      if(!ModernRoboticsPacket.validatePacket(var1, 3)) {
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

   public static void init(Context var0) {
      if(a != null) {
         a = new Analytics(var0, "update_rc");
      }

   }

   public static RobotUsbDevice openUsbDevice(RobotUsbManager var0, SerialNumber var1) throws RobotCoreException {
      return a(var0, var1);
   }
}
