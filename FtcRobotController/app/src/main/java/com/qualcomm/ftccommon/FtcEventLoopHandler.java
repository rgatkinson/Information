package com.qualcomm.ftccommon;

import android.content.Context;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftccommon.UpdateUI;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareFactory;
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
   EventLoopManager a;
   BatteryChecker b;
   Context c;
   ElapsedTime d = new ElapsedTime();
   double e = 0.25D;
   UpdateUI.Callback f;
   HardwareFactory g;
   HardwareMap h = new HardwareMap();

   public FtcEventLoopHandler(HardwareFactory var1, UpdateUI.Callback var2, Context var3) {
      this.g = var1;
      this.f = var2;
      this.c = var3;
      this.b = new BatteryChecker(var3, this, 180000L);
      this.b.startBatteryMonitoring();
   }

   private void a() {
      this.sendTelemetry("RobotController Battery Level", String.valueOf(this.b.getBatteryLevel()));
   }

   private void b() {
      Iterator var1 = this.h.voltageSensor.iterator();

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
      if(this.h.voltageSensor.size() == 0) {
         var4 = "no voltage sensor found";
      } else {
         var4 = String.valueOf((new BigDecimal(var2)).setScale(2, RoundingMode.HALF_UP).doubleValue());
      }

      this.sendTelemetry("Robot Battery Level", var4);
   }

   public void displayGamePadInfo(String var1) {
      Gamepad[] var2 = this.a.getGamepads();
      this.f.updateUi(var1, var2);
   }

   public Gamepad[] getGamepads() {
      return this.a.getGamepads();
   }

   public HardwareMap getHardwareMap() throws RobotCoreException, InterruptedException {
      this.h = this.g.createHardwareMap(this.a);
      return this.h;
   }

   public String getOpMode(String var1) {
      if(this.a.state != RobotState.RUNNING) {
         var1 = "Stop Robot";
      }

      return var1;
   }

   public void init(EventLoopManager var1) {
      this.a = var1;
   }

   public void restartRobot() {
      this.b.endBatteryMonitoring();
      this.f.restartRobot();
   }

   public void sendBatteryInfo() {
      this.a();
      this.b();
   }

   public void sendCommand(Command var1) {
      this.a.sendCommand(var1);
   }

   public void sendTelemetry(String var1, String var2) {
      Telemetry var3 = new Telemetry();
      var3.setTag(var1);
      var3.addData(var1, var2);
      if(this.a != null) {
         this.a.sendTelemetryData(var3);
         var3.clearData();
      }

   }

   public void sendTelemetryData(Telemetry telemetry) {
      if(this.d.time() > this.e) {
         this.d.reset();
         if(telemetry.hasData()) {
            this.a.sendTelemetryData(telemetry);
         }

         telemetry.clearData();
      }

   }

   public void shutdownCoreInterfaceDeviceModules() {
      Iterator var1 = this.h.deviceInterfaceModule.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         String var3 = (String)var2.getKey();
         DeviceInterfaceModule var4 = (DeviceInterfaceModule)var2.getValue();
         DbgLog.msg("Stopping Core Interface Device Module " + var3);
         var4.close();
      }

   }

   public void shutdownLegacyModules() {
      Iterator var1 = this.h.legacyModule.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         String var3 = (String)var2.getKey();
         LegacyModule var4 = (LegacyModule)var2.getValue();
         DbgLog.msg("Stopping Legacy Module " + var3);
         var4.close();
      }

   }

   public void shutdownMotorControllers() {
      Iterator var1 = this.h.dcMotorController.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         String var3 = (String)var2.getKey();
         DcMotorController var4 = (DcMotorController)var2.getValue();
         DbgLog.msg("Stopping DC Motor Controller " + var3);
         var4.close();
      }

   }

   public void shutdownServoControllers() {
      Iterator var1 = this.h.servoController.entrySet().iterator();

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
