package com.qualcomm.hardware.hardware;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ModernRoboticsUsbDevice implements ReadWriteRunnable.Callback {
   public static final int DEVICE_ID_DC_MOTOR_CONTROLLER = 77;
   public static final int DEVICE_ID_DEVICE_INTERFACE_MODULE = 65;
   public static final int DEVICE_ID_LEGACY_MODULE = 73;
   public static final int DEVICE_ID_SERVO_CONTROLLER = 83;
   public static final int MFG_CODE_MODERN_ROBOTICS = 77;
   protected ReadWriteRunnable readWriteRunnable;
   protected ExecutorService readWriteService = Executors.newSingleThreadExecutor();
   protected SerialNumber serialNumber;

   public ModernRoboticsUsbDevice(SerialNumber var1, EventLoopManager var2, ReadWriteRunnable var3) throws RobotCoreException, InterruptedException {
      this.serialNumber = var1;
      this.readWriteRunnable = var3;
      RobotLog.v("Starting up device " + var1.toString());
      this.readWriteService.execute(var3);
      var3.blockUntilReady();
      var3.setCallback(this);
      var2.registerSyncdDevice(var3);
   }

   public void close() {
      RobotLog.v("Shutting down device " + this.serialNumber.toString());
      this.readWriteService.shutdown();
      this.readWriteRunnable.close();
   }

   public abstract String getDeviceName();

   public SerialNumber getSerialNumber() {
      return this.serialNumber;
   }

   public int getVersion() {
      return this.read(0);
   }

   public byte read(int var1) {
      return this.read(var1, 1)[0];
   }

   public byte[] read(int var1, int var2) {
      return this.readWriteRunnable.read(var1, var2);
   }

   public void readComplete() throws InterruptedException {
   }

   public byte readFromWriteCache(int var1) {
      return this.readFromWriteCache(var1, 1)[0];
   }

   public byte[] readFromWriteCache(int var1, int var2) {
      return this.readWriteRunnable.readFromWriteCache(var1, var2);
   }

   public void write(int var1, byte var2) {
      this.write(var1, new byte[]{var2});
   }

   public void write(int var1, double var2) {
      byte[] var4 = new byte[]{(byte)((int)var2)};
      this.write(var1, var4);
   }

   public void write(int var1, int var2) {
      byte[] var3 = new byte[]{(byte)var2};
      this.write(var1, var3);
   }

   public void write(int var1, byte[] var2) {
      this.readWriteRunnable.write(var1, var2);
   }

   public void writeComplete() throws InterruptedException {
   }
}
