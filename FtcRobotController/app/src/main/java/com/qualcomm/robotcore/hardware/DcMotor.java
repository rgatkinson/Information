package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareDevice;

public class DcMotor implements HardwareDevice {
   protected DcMotorController controller;
   protected DcMotorController.DeviceMode devMode;
   protected DcMotor.Direction direction;
   protected DcMotorController.RunMode mode;
   protected int portNumber;

   public DcMotor(DcMotorController var1, int var2) {
      this(var1, var2, DcMotor.Direction.FORWARD);
   }

   public DcMotor(DcMotorController var1, int var2, DcMotor.Direction var3) {
      this.direction = DcMotor.Direction.FORWARD;
      this.controller = null;
      this.portNumber = -1;
      this.mode = DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
      this.devMode = DcMotorController.DeviceMode.WRITE_ONLY;
      this.controller = var1;
      this.portNumber = var2;
      this.direction = var3;
   }

   public void close() {
      this.setPowerFloat();
   }

   @Deprecated
   public DcMotorController.RunMode getChannelMode() {
      return this.getMode();
   }

   public String getConnectionInfo() {
      return this.controller.getConnectionInfo() + "; port " + this.portNumber;
   }

   public DcMotorController getController() {
      return this.controller;
   }

   public int getCurrentPosition() {
      int var1 = this.controller.getMotorCurrentPosition(this.portNumber);
      if(this.direction == DcMotor.Direction.REVERSE) {
         var1 *= -1;
      }

      return var1;
   }

   public String getDeviceName() {
      return "DC Motor";
   }

   public DcMotor.Direction getDirection() {
      return this.direction;
   }

   public DcMotorController.RunMode getMode() {
      return this.controller.getMotorChannelMode(this.portNumber);
   }

   public int getPortNumber() {
      return this.portNumber;
   }

   public double getPower() {
      double var1 = this.controller.getMotorPower(this.portNumber);
      if(this.direction == DcMotor.Direction.REVERSE && var1 != 0.0D) {
         var1 *= -1.0D;
      }

      return var1;
   }

   public boolean getPowerFloat() {
      return this.controller.getMotorPowerFloat(this.portNumber);
   }

   public int getTargetPosition() {
      int var1 = this.controller.getMotorTargetPosition(this.portNumber);
      if(this.direction == DcMotor.Direction.REVERSE) {
         var1 *= -1;
      }

      return var1;
   }

   public int getVersion() {
      return 1;
   }

   public boolean isBusy() {
      return this.controller.isBusy(this.portNumber);
   }

   @Deprecated
   public void setChannelMode(DcMotorController.RunMode var1) {
      this.setMode(var1);
   }

   public void setDirection(DcMotor.Direction var1) {
      this.direction = var1;
   }

   public void setMode(DcMotorController.RunMode var1) {
      this.mode = var1;
      this.controller.setMotorChannelMode(this.portNumber, var1);
   }

   public void setPower(double var1) {
      if(this.direction == DcMotor.Direction.REVERSE) {
         var1 *= -1.0D;
      }

      if(this.mode == DcMotorController.RunMode.RUN_TO_POSITION) {
         var1 = Math.abs(var1);
      }

      this.controller.setMotorPower(this.portNumber, var1);
   }

   public void setPowerFloat() {
      this.controller.setMotorPowerFloat(this.portNumber);
   }

   public void setTargetPosition(int var1) {
      if(this.direction == DcMotor.Direction.REVERSE) {
         var1 *= -1;
      }

      this.controller.setMotorTargetPosition(this.portNumber, var1);
   }

   public static enum Direction {
      FORWARD,
      REVERSE;

      static {
         DcMotor.Direction[] var0 = new DcMotor.Direction[]{FORWARD, REVERSE};
      }
   }
}
