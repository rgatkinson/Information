package com.qualcomm.hardware;

import com.qualcomm.hardware.ModernRoboticsUsbLegacyModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;

public class HiTechnicNxtGyroSensor extends GyroSensor {
   private final ModernRoboticsUsbLegacyModule a;
   private final int b;

   HiTechnicNxtGyroSensor(ModernRoboticsUsbLegacyModule var1, int var2) {
      var1.enableAnalogReadMode(var2);
      this.a = var1;
      this.b = var2;
   }

   public void calibrate() {
      this.notSupported();
   }

   public void close() {
   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo() + "; port " + this.b;
   }

   public String getDeviceName() {
      return "NXT Gyro Sensor";
   }

   public int getHeading() {
      this.notSupported();
      return 0;
   }

   public double getRotation() {
      return (double)TypeConversion.byteArrayToShort(this.a.readAnalog(this.b), ByteOrder.LITTLE_ENDIAN);
   }

   public int getVersion() {
      return 1;
   }

   public boolean isCalibrating() {
      this.notSupported();
      return false;
   }

   public int rawX() {
      this.notSupported();
      return 0;
   }

   public int rawY() {
      this.notSupported();
      return 0;
   }

   public int rawZ() {
      this.notSupported();
      return 0;
   }

   public void resetZAxisIntegrator() {
      this.notSupported();
   }

   public String status() {
      Object[] var1 = new Object[]{this.a.getSerialNumber().toString(), Integer.valueOf(this.b)};
      return String.format("NXT Gyro Sensor, connected via device %s, port %d", var1);
   }
}
