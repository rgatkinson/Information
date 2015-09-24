package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.robocol.Telemetry;
import java.util.concurrent.locks.Lock;

public class LinearI2cAddressChange extends LinearOpMode {
   public static final int ADDRESS_MEMORY_START = 0;
   public static final int ADDRESS_SET_NEW_I2C_ADDRESS = 112;
   public static final int BUFFER_CHANGE_ADDRESS_LENGTH = 3;
   public static final byte FIRMWARE_REV = 18;
   public static final byte IR_SEEKER_V3_FIRMWARE_REV = 18;
   public static final byte IR_SEEKER_V3_ORIGINAL_ADDRESS = 56;
   public static final byte IR_SEEKER_V3_SENSOR_ID = 73;
   public static final byte MANUFACTURER_CODE = 77;
   public static final int READ_MODE = 128;
   public static final byte SENSOR_ID = 73;
   public static final int TOTAL_MEMORY_LENGTH = 12;
   public static final byte TRIGGER_BYTE_1 = 85;
   public static final byte TRIGGER_BYTE_2 = -86;
   int currentAddress = 56;
   DeviceInterfaceModule dim;
   int newAddress = 66;
   int port = 3;
   byte[] readCache;
   Lock readLock;
   byte[] writeCache;
   Lock writeLock;

   private boolean foundExpectedBytes(int[] param1, Lock param2, byte[] param3) {
      // $FF: Couldn't be decompiled
   }

   private void performAction(String var1, int var2, int var3, int var4, int var5) {
      if(var1.equalsIgnoreCase("read")) {
         this.dim.enableI2cReadMode(var2, var3, var4, var5);
      }

      if(var1.equalsIgnoreCase("write")) {
         this.dim.enableI2cWriteMode(var2, var3, var4, var5);
      }

      this.dim.setI2cPortActionFlag(var2);
      this.dim.writeI2cCacheToController(var2);
      this.dim.readI2cCacheFromController(var2);
   }

   private void writeNewAddress() {
      try {
         this.writeLock.lock();
         this.writeCache[4] = (byte)this.newAddress;
         this.writeCache[5] = 85;
         this.writeCache[6] = -86;
      } finally {
         this.writeLock.unlock();
      }

   }

   public void runOpMode() throws InterruptedException {
      this.dim = (DeviceInterfaceModule)this.hardwareMap.deviceInterfaceModule.get("dim");
      this.readCache = this.dim.getI2cReadCache(this.port);
      this.readLock = this.dim.getI2cReadCacheLock(this.port);
      this.writeCache = this.dim.getI2cWriteCache(this.port);
      this.writeLock = this.dim.getI2cWriteCacheLock(this.port);
      IrSeekerSensor.throwIfModernRoboticsI2cAddressIsInvalid(this.newAddress);
      this.waitForStart();
      this.performAction("read", this.port, this.currentAddress, 0, 12);

      while(!this.dim.isI2cPortReady(this.port)) {
         this.telemetry.addData("I2cAddressChange", "waiting for the port to be ready...");
         this.sleep(1000L);
      }

      this.dim.readI2cCacheFromController(this.port);
      int var1 = 0;
      int[] var2 = new int[]{128, this.currentAddress, 0, 12, 18, 77, 73};

      while(!this.foundExpectedBytes(var2, this.readLock, this.readCache)) {
         this.telemetry.addData("I2cAddressChange", "Confirming that we\'re reading the correct bytes...");
         this.dim.readI2cCacheFromController(this.port);
         this.sleep(1000L);
         ++var1;
         if(var1 >= 10) {
            Telemetry var8 = this.telemetry;
            Object[] var9 = new Object[]{Integer.valueOf(this.currentAddress)};
            var8.addData("I2cAddressChange", String.format("Looping too long with no change, probably have the wrong address. Current address: %02x", var9));
            HardwareMap.DeviceMapping var10 = this.hardwareMap.irSeekerSensor;
            Object[] var11 = new Object[]{Integer.valueOf(this.currentAddress)};
            var10.get(String.format("Looping too long with no change, probably have the wrong address. Current address: %02x", var11));
         }
      }

      this.performAction("write", this.port, this.currentAddress, 112, 3);
      this.waitOneFullHardwareCycle();
      this.writeNewAddress();
      this.dim.setI2cPortActionFlag(this.port);
      this.dim.writeI2cCacheToController(this.port);
      this.telemetry.addData("I2cAddressChange", "Giving the hardware some time to make the change...");

      for(int var3 = 0; var3 < 5000; ++var3) {
         this.waitOneFullHardwareCycle();
      }

      this.dim.enableI2cReadMode(this.port, this.newAddress, 0, 12);
      this.dim.setI2cPortActionFlag(this.port);
      this.dim.writeI2cCacheToController(this.port);
      int[] var4 = new int[]{128, this.newAddress, 0, 12, 18, 77, 73};

      while(!this.foundExpectedBytes(var4, this.readLock, this.readCache)) {
         this.telemetry.addData("I2cAddressChange", "Have not confirmed the changes yet...");
         this.dim.readI2cCacheFromController(this.port);
         this.sleep(1000L);
      }

      Telemetry var5 = this.telemetry;
      StringBuilder var6 = (new StringBuilder()).append("Successfully changed the I2C address.");
      Object[] var7 = new Object[]{Integer.valueOf(this.newAddress)};
      var5.addData("I2cAddressChange", var6.append(String.format("New address: %02x", var7)).toString());
   }
}
