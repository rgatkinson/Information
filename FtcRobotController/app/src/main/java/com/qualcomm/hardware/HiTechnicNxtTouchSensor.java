package com.qualcomm.hardware;

import com.qualcomm.hardware.ModernRoboticsUsbLegacyModule;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;

public class HiTechnicNxtTouchSensor extends TouchSensor {
   private final ModernRoboticsUsbLegacyModule a;
   private final int b;

   public HiTechnicNxtTouchSensor(ModernRoboticsUsbLegacyModule var1, int var2) {
      var1.enableAnalogReadMode(var2);
      this.a = var1;
      this.b = var2;
   }

   public void close() {
   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo() + "; port " + this.b;
   }

   public String getDeviceName() {
      return "NXT Touch Sensor";
   }

   public double getValue() {
      return (double)TypeConversion.byteArrayToShort(this.a.readAnalog(this.b), ByteOrder.LITTLE_ENDIAN) > 675.0D?0.0D:1.0D;
   }

   public int getVersion() {
      return 1;
   }

   public boolean isPressed() {
      return this.getValue() > 0.0D;
   }

   public String status() {
      Object[] var1 = new Object[]{this.a.getSerialNumber().toString(), Integer.valueOf(this.b)};
      return String.format("NXT Touch Sensor, connected via device %s, port %d", var1);
   }
}
