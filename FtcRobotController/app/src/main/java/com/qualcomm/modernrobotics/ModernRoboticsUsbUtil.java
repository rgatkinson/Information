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
   private static Analytics analytics;

   private static DeviceManager.DeviceType internalGetDeviceType(byte[] var0) {
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

   private static RobotUsbDevice internalOpenUsbDevice(RobotUsbManager robotUsbManager, SerialNumber serialNumber) throws RobotCoreException {
      int numberOfDevices = robotUsbManager.scanForDevices();
      int iDevice = 0;

      String deviceDescription;
      boolean foundDevice;
      while(true) {
         if(iDevice >= numberOfDevices) {
            deviceDescription = "";
            foundDevice = false;
            break;
         }

         if(serialNumber.equals(robotUsbManager.getDeviceSerialNumberByIndex(iDevice))) {
            foundDevice = true;
            deviceDescription = robotUsbManager.getDeviceDescriptionByIndex(iDevice) + " [" + serialNumber.getSerialNumber() + "]";
            break;
         }

         ++iDevice;
      }

      if(!foundDevice) {
         logAndThrow("unable to find USB device with serial number " + serialNumber.toString());
      }

      RobotUsbDevice result;
      label29: {
         RobotUsbDevice robotUsbDevice;
         try {
            robotUsbDevice = robotUsbManager.openBySerialNumber(serialNumber);
         } catch (RobotCoreException var13) {
            logAndThrow("Unable to open USB device " + serialNumber + " - " + deviceDescription + ": " + var13.getMessage());
            result = null;
            break label29;
         }

         result = robotUsbDevice;
      }

      try {
         result.setBaudRate(250000);
         result.setDataCharacteristics((byte) 8, (byte) 0, (byte) 0);
         result.setLatencyTimer(2);
      } catch (RobotCoreException var12) {
         result.close();
         logAndThrow("Unable to open USB device " + serialNumber + " - " + deviceDescription + ": " + var12.getMessage());
      }

      try {
         Thread.sleep(400L);  // why?
         return result;
      } catch (InterruptedException var11) {
         return result;
      }
   }

   private static void logAndThrow(String var0) throws RobotCoreException {
      System.err.println(var0);
      throw new RobotCoreException(var0);
   }

   private static byte[] internalGetUsbDeviceHeader(RobotUsbDevice robotUsbDevice) throws RobotCoreException {
      byte[] rgbDeviceHeaderHeader = new byte[5];
      byte[] rgbDeviceHeaderPayload = new byte[3];
      byte[] rgbReadDeviceHeader = new byte[]{(byte)85, (byte)-86, (byte)-128, (byte)0, (byte)3};

      try {
         robotUsbDevice.purge(RobotUsbDevice.Channel.RX);
         robotUsbDevice.write(rgbReadDeviceHeader);
         robotUsbDevice.read(rgbDeviceHeaderHeader);
      } catch (RobotCoreException var5) {
         logAndThrow("error reading Modern Robotics USB device headers");
      }

      if(!analytics.a(rgbDeviceHeaderHeader, 3)) {
         return rgbDeviceHeaderPayload;
      } else {
         robotUsbDevice.read(rgbDeviceHeaderPayload);
         return rgbDeviceHeaderPayload;
      }
   }

   public static DeviceManager.DeviceType getDeviceType(byte[] var0) {
      return internalGetDeviceType(var0);
   }

   public static byte[] getUsbDeviceHeader(RobotUsbDevice robotUsbDevice) throws RobotCoreException {
      return internalGetUsbDeviceHeader(robotUsbDevice);
   }

   public static void init(Context var0, HardwareMap var1) {
      if (analytics == null) {
         analytics = new Analytics(var0, "update_rc", var1);
      }
   }

   public static RobotUsbDevice openUsbDevice(RobotUsbManager robotUsbManager, SerialNumber serialNumber) throws RobotCoreException {
      return internalOpenUsbDevice(robotUsbManager, serialNumber);
   }
}
