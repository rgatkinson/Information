package com.qualcomm.robotcore.hardware;

import java.util.concurrent.locks.Lock;

public class I2cDevice implements HardwareDevice {
   private I2cController controller = null;
   private int port = -1;

   public I2cDevice(I2cController controller, int port) {
      this.controller = controller;
      this.port = port;
   }

   public void close() {
   }

   public void copyBufferIntoWriteBuffer(byte[] var1) {
      this.controller.copyBufferIntoWriteBuffer(this.port, var1);
   }

   public void deregisterForPortReadyCallback() {
      this.controller.deregisterForPortReadyCallback(this.port);
   }

   public void enableI2cReadMode(int var1, int var2, int var3) {
      this.controller.enableI2cReadMode(this.port, var1, var2, var3);
   }

   public void enableI2cWriteMode(int var1, int var2, int var3) {
      this.controller.enableI2cWriteMode(this.port, var1, var2, var3);
   }

   public String getConnectionInfo() {
      return this.controller.getConnectionInfo() + "; port " + this.port;
   }

   public byte[] getCopyOfReadBuffer() {
      return this.controller.getCopyOfReadBuffer(this.port);
   }

   public byte[] getCopyOfWriteBuffer() {
      return this.controller.getCopyOfWriteBuffer(this.port);
   }

   public String getDeviceName() {
      return "I2cDevice";
   }

   public byte[] getI2cReadCache() {
      return this.controller.getI2cReadCache(this.port);
   }

   public Lock getI2cReadCacheLock() {
      return this.controller.getI2cReadCacheLock(this.port);
   }

   public byte[] getI2cWriteCache() {
      return this.controller.getI2cWriteCache(this.port);
   }

   public Lock getI2cWriteCacheLock() {
      return this.controller.getI2cWriteCacheLock(this.port);
   }

   public int getVersion() {
      return 1;
   }

   public boolean isI2cPortActionFlagSet() {
      return this.controller.isI2cPortActionFlagSet(this.port);
   }

   public boolean isI2cPortInReadMode() {
      return this.controller.isI2cPortInReadMode(this.port);
   }

   public boolean isI2cPortInWriteMode() {
      return this.controller.isI2cPortInWriteMode(this.port);
   }

   public boolean isI2cPortReady() {
      return this.controller.isI2cPortReady(this.port);
   }

   public void readI2cCacheFromController() {
      this.controller.readI2cCacheFromController(this.port);
   }

   @Deprecated
   public void readI2cCacheFromModule() {
      this.readI2cCacheFromController();
   }

   public void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback var1) {
      this.controller.registerForI2cPortReadyCallback(var1, this.port);
   }

   public void setI2cPortActionFlag() {
      this.controller.setI2cPortActionFlag(this.port);
   }

   public void writeI2cCacheToController() {
      this.controller.writeI2cCacheToController(this.port);
   }

   @Deprecated
   public void writeI2cCacheToModule() {
      this.writeI2cCacheToController();
   }

   public void writeI2cPortFlagOnlyToController() {
      this.controller.writeI2cPortFlagOnlyToController(this.port);
   }

   @Deprecated
   public void writeI2cPortFlagOnlyToModule() {
      this.writeI2cPortFlagOnlyToController();
   }
}
