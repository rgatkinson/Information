package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;

public class I2cDeviceReader {
   private final I2cDevice a;

   public I2cDeviceReader(I2cDevice var1, int var2, int var3, int var4) {
      this.a = var1;
      var1.enableI2cReadMode(var2, var3, var4);
      var1.setI2cPortActionFlag();
      var1.writeI2cCacheToModule();
      var1.registerForI2cPortReadyCallback(new I2cController.I2cPortReadyCallback() {
         public void portIsReady(int var1) {
            I2cDeviceReader.this.a();
         }
      });
   }

   private void a() {
      this.a.setI2cPortActionFlag();
      this.a.readI2cCacheFromModule();
      this.a.writeI2cPortFlagOnlyToModule();
   }

   public byte[] getReadBuffer() {
      return this.a.getCopyOfReadBuffer();
   }
}
