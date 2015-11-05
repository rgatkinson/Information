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

   private static DeviceManager.DeviceType deviceTypeFromHeaderBytes(byte[] var0) {
      if(var0[1] != MFG_CODE_MODERN_ROBOTICS) {
         return DeviceManager.DeviceType.FTDI_USB_UNKNOWN_DEVICE;
      } else {
         switch(var0[2]) {
         case DEVICE_ID_DEVICE_INTERFACE_MODULE:
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE;
         case DEVICE_ID_LEGACY_MODULE:
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE;
         case DEVICE_ID_DC_MOTOR_CONTROLLER:
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER;
         case DEVICE_ID_SERVO_CONTROLLER:
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER;
         default:
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_UNKNOWN_DEVICE;
         }
      }
   }

   private static RobotUsbDevice findAndOpenDeviceBySerialNumber(RobotUsbManager robotUsbManager, SerialNumber serialNumber) throws RobotCoreException {

      int numberOfDevices = robotUsbManager.scanForDevices();
      int index = 0;

      String deviceDescription;
      boolean found;
      while(true) {
         if(index >= numberOfDevices) {
            deviceDescription = "";
            found = false;
            break;
         }

         if(serialNumber.equals(robotUsbManager.getDeviceSerialNumberByIndex(index))) {
            found = true;
            deviceDescription = robotUsbManager.getDeviceDescriptionByIndex(index) + " [" + serialNumber.getSerialNumber() + "]";
            break;
         }

         ++index;
      }

      if(!found) {
         throwMessage("unable to find USB device with serial number " + serialNumber.toString());
      }

      RobotUsbDevice robotUsbDevice = null;

      try {
         robotUsbDevice = robotUsbManager.openBySerialNumber(serialNumber);
         robotUsbDevice.setBaudRate(250000);
         robotUsbDevice.setDataCharacteristics((byte) 8, (byte) 0, (byte) 0);
         robotUsbDevice.setLatencyTimer(2);
      } catch (RobotCoreException e) {
         throwMessage("Unable to open USB device " + serialNumber + " - " + deviceDescription + ": " + e.getMessage());
      }

      // Odd little sleep!
      try {
         Thread.sleep(400L);
         return robotUsbDevice;
      } catch (InterruptedException e) {
         return robotUsbDevice;
      }
   }

   private static void throwMessage(String message) throws RobotCoreException {
      System.err.println(message);
      throw new RobotCoreException(message);
   }

   private static byte[] readUsbDeviceHeader(RobotUsbDevice robotUsbDevice) throws RobotCoreException {
      byte[] var1 = new byte[5];
      byte[] var2 = new byte[3];
      byte[] var3 = new byte[]{(byte)85, (byte)-86, (byte)-128, (byte)0, (byte)3};

      try {
         robotUsbDevice.purge(RobotUsbDevice.Channel.RX);
         robotUsbDevice.write(var3);
         robotUsbDevice.read(var1);
      } catch (RobotCoreException var5) {
         throwMessage("error reading USB device headers");
      }

      if(!ModernRoboticsPacket.a(var1, 3)) {
         return var2;
      } else {
         robotUsbDevice.read(var2);
         return var2;
      }
   }

   public static DeviceManager.DeviceType getDeviceType(byte[] headerBytes) {
      return deviceTypeFromHeaderBytes(headerBytes);
   }

   public static byte[] getUsbDeviceHeader(RobotUsbDevice robotUsbDevice) throws RobotCoreException {
      return readUsbDeviceHeader(robotUsbDevice);
   }

   public static void init(Context var0, HardwareMap var1) {
      if(a == null) {
         a = new Analytics(var0, "update_rc", var1);
      }

   }

   public static RobotUsbDevice openUsbDevice(RobotUsbManager robotUsbManager, SerialNumber serialNumber) throws RobotCoreException {
      return findAndOpenDeviceBySerialNumber(robotUsbManager, serialNumber);
   }
}
