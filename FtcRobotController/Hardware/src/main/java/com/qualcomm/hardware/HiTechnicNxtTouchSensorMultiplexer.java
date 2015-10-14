package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
import com.qualcomm.robotcore.util.TypeConversion;

import java.nio.ByteOrder;

public class HiTechnicNxtTouchSensorMultiplexer extends TouchSensorMultiplexer {
   public static final int INVALID = -1;
   public static final int[] MASK_MAP = new int[]{-1, 1, 2, 4, 8};
   public static final int MASK_TOUCH_SENSOR_1 = 1;
   public static final int MASK_TOUCH_SENSOR_2 = 2;
   public static final int MASK_TOUCH_SENSOR_3 = 4;
   public static final int MASK_TOUCH_SENSOR_4 = 8;
   int a = 4;
   private final ModernRoboticsUsbLegacyModule b;
   private final int c;

   public HiTechnicNxtTouchSensorMultiplexer(ModernRoboticsUsbLegacyModule var1, int var2) {
      var1.enableAnalogReadMode(var2);
      this.b = var1;
      this.c = var2;
   }

   private int a() {
      int var1 = 1023 - TypeConversion.byteArrayToShort(this.b.readAnalog(3), ByteOrder.LITTLE_ENDIAN);
      return (5 + var1 * 339 / (1023 - var1)) / 10;
   }

   private void a(int var1) {
      if(var1 <= 0 || var1 > this.a) {
         Object[] var2 = new Object[]{Integer.valueOf(var1), Integer.valueOf(this.a)};
         throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", var2));
      }
   }

   public void close() {
   }

   public String getConnectionInfo() {
      return this.b.getConnectionInfo() + "; port " + this.c;
   }

   public String getDeviceName() {
      return "NXT Touch Sensor Multiplexer";
   }

   public int getSwitches() {
      return this.a();
   }

   public int getVersion() {
      return 1;
   }

   public boolean isTouchSensorPressed(int var1) {
      this.a(var1);
      return (this.a() & MASK_MAP[var1]) > 0;
   }

   public String status() {
      Object[] var1 = new Object[]{this.b.getSerialNumber().toString(), Integer.valueOf(this.c)};
      return String.format("NXT Touch Sensor Multiplexer, connected via device %s, port %d", var1);
   }
}
