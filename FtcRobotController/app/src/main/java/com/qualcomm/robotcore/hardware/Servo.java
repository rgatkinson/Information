package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.Range;

public class Servo implements HardwareDevice {
   public static final double MAX_POSITION = 1.0D;
   public static final double MIN_POSITION;
   protected ServoController controller;
   protected Servo.Direction direction;
   protected double maxPosition;
   protected double minPosition;
   protected int portNumber;

   public Servo(ServoController var1, int var2) {
      this(var1, var2, Servo.Direction.FORWARD);
   }

   public Servo(ServoController var1, int var2, Servo.Direction var3) {
      this.controller = null;
      this.portNumber = -1;
      this.direction = Servo.Direction.FORWARD;
      this.minPosition = 0.0D;
      this.maxPosition = 1.0D;
      this.direction = var3;
      this.controller = var1;
      this.portNumber = var2;
   }

   private double a(double var1) {
      return 0.0D + (1.0D - var1);
   }

   public void close() {
   }

   public String getConnectionInfo() {
      return this.controller.getConnectionInfo() + "; port " + this.portNumber;
   }

   public ServoController getController() {
      return this.controller;
   }

   public String getDeviceName() {
      return "Servo";
   }

   public Servo.Direction getDirection() {
      return this.direction;
   }

   public int getPortNumber() {
      return this.portNumber;
   }

   public double getPosition() {
      double var1 = this.controller.getServoPosition(this.portNumber);
      if(this.direction == Servo.Direction.REVERSE) {
         var1 = this.a(var1);
      }

      return Range.clip(Range.scale(var1, this.minPosition, this.maxPosition, 0.0D, 1.0D), 0.0D, 1.0D);
   }

   public int getVersion() {
      return 1;
   }

   public void scaleRange(double var1, double var3) throws IllegalArgumentException {
      Range.throwIfRangeIsInvalid(var1, 0.0D, 1.0D);
      Range.throwIfRangeIsInvalid(var3, 0.0D, 1.0D);
      if(var1 >= var3) {
         throw new IllegalArgumentException("min must be less than max");
      } else {
         this.minPosition = var1;
         this.maxPosition = var3;
      }
   }

   public void setDirection(Servo.Direction var1) {
      this.direction = var1;
   }

   public void setPosition(double var1) {
      if(this.direction == Servo.Direction.REVERSE) {
         var1 = this.a(var1);
      }

      double var3 = this.minPosition;
      double var5 = this.maxPosition;
      double var7 = Range.scale(var1, 0.0D, 1.0D, var3, var5);
      this.controller.setServoPosition(this.portNumber, var7);
   }

   public static enum Direction {
      FORWARD,
      REVERSE;

      static {
         Servo.Direction[] var0 = new Servo.Direction[]{FORWARD, REVERSE};
      }
   }
}
