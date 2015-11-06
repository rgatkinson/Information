package com.qualcomm.ftccommon;

import android.content.Context;

import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.BatteryChecker;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Map.Entry;

public class FtcEventLoopHandler implements BatteryChecker.BatteryWatcher {
   public static final String NO_VOLTAGE_SENSOR = "no voltage sensor found";
   EventLoopManager eventLoopManager;
   BatteryChecker batteryChecker;
   Context context;
   ElapsedTime elapsed = new ElapsedTime();
   double msTelemetryTransmissionThreshold = 0.25D;
   UpdateUI.Callback callback;
   HardwareFactory hardwareFactory;
   HardwareMap hardwareMap = new HardwareMap();

   public FtcEventLoopHandler(HardwareFactory var1, UpdateUI.Callback var2, Context var3) {
      this.hardwareFactory = var1;
      this.callback = var2;
      this.context = var3;
      this.batteryChecker = new BatteryChecker(var3, this, 180000L);
      this.batteryChecker.startBatteryMonitoring();
   }

   private void sendRCBatteryLevel() {
      this.sendTelemetry("RobotController Battery Level", String.valueOf(this.batteryChecker.getBatteryLevel()));
   }

   private void sendRobotBatteryLevel() {
      Iterator var1 = this.hardwareMap.voltageSensor.iterator();

      double var2;
      double var6;
      for(var2 = Double.MAX_VALUE; var1.hasNext(); var2 = var6) {
         VoltageSensor var5 = (VoltageSensor)var1.next();
         if(var5.getVoltage() < var2) {
            var6 = var5.getVoltage();
         } else {
            var6 = var2;
         }
      }

      String var4;
      if(this.hardwareMap.voltageSensor.size() == 0) {
         var4 = "no voltage sensor found";
      } else {
         var4 = String.valueOf((new BigDecimal(var2)).setScale(2, RoundingMode.HALF_UP).doubleValue());
      }

      this.sendTelemetry("Robot Battery Level", var4);
   }

   public void displayGamePadInfo(String var1) {
      Gamepad[] var2 = this.eventLoopManager.getGamepads();
      this.callback.updateUi(var1, var2);
   }

   public Gamepad[] getGamepads() {
      return this.eventLoopManager.getGamepads();
   }

   public HardwareMap getHardwareMap() throws RobotCoreException, InterruptedException {
      this.hardwareMap = this.hardwareFactory.createHardwareMap(this.eventLoopManager);
      return this.hardwareMap;
   }

   public String getOpMode(String proposedOpMode) {
      if (this.eventLoopManager.state != RobotState.RUNNING) {
         proposedOpMode = "Stop Robot";
      }

      return proposedOpMode;
   }

   public void init(EventLoopManager var1) {
      this.eventLoopManager = var1;
   }

   public void resetGamepads() {
      this.eventLoopManager.resetGamepads();
   }

   public void restartRobot() {
      this.batteryChecker.endBatteryMonitoring();
      this.callback.restartRobot();
   }

   public void sendBatteryInfo() {
      this.sendRCBatteryLevel();
      this.sendRobotBatteryLevel();
   }

   public void sendCommand(Command command) {
      this.eventLoopManager.sendCommand(command);
   }

   public void sendTelemetry(String tag, String data) {
      Telemetry telemetry = new Telemetry();
      telemetry.setTag(tag);
      telemetry.addData(tag, data);
      if(this.eventLoopManager != null) {
         this.eventLoopManager.sendTelemetryData(telemetry);
         telemetry.clearData();
      }

   }

   /** send the *user's* telemetry data */
   public void sendTelemetryData(Telemetry telemetry) {
      if(this.elapsed.time() > this.msTelemetryTransmissionThreshold) {
         this.elapsed.reset();
         if(telemetry.hasData()) {
            this.eventLoopManager.sendTelemetryData(telemetry);
         }

         telemetry.clearData();
      }

   }

   public void shutdownCoreInterfaceDeviceModules() {
      Iterator var1 = this.hardwareMap.deviceInterfaceModule.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         String var3 = (String)var2.getKey();
         DeviceInterfaceModule var4 = (DeviceInterfaceModule)var2.getValue();
         DbgLog.msg("Stopping Core Interface Device Module " + var3);
         var4.close();
      }

   }

   public void shutdownLegacyModules() {
      Iterator var1 = this.hardwareMap.legacyModule.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         String var3 = (String)var2.getKey();
         LegacyModule var4 = (LegacyModule)var2.getValue();
         DbgLog.msg("Stopping Legacy Module " + var3);
         var4.close();
      }

   }

   public void shutdownMotorControllers() {
      Iterator var1 = this.hardwareMap.dcMotorController.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         String var3 = (String)var2.getKey();
         DcMotorController var4 = (DcMotorController)var2.getValue();
         DbgLog.msg("Stopping DC Motor Controller " + var3);
         var4.close();
      }

   }

   public void shutdownServoControllers() {
      Iterator var1 = this.hardwareMap.servoController.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         String var3 = (String)var2.getKey();
         ServoController var4 = (ServoController)var2.getValue();
         DbgLog.msg("Stopping Servo Controller " + var3);
         var4.close();
      }

   }

   public void updateBatteryLevel(float var1) {
      this.sendTelemetry("RobotController Battery Level", String.valueOf(var1));
   }
}
