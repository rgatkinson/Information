package com.qualcomm.ftccommon;

import android.content.Context;

import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.modernrobotics.ModernRoboticsUsbUtil;
import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.Util;
import java.util.Iterator;

public class FtcEventLoop implements EventLoop {
   FtcEventLoopHandler ftcEventLoopHandler;
   OpModeManager opModeManager = new OpModeManager(new HardwareMap());
   OpModeRegister opModeRegister;

   public FtcEventLoop(HardwareFactory var1, OpModeRegister var2, UpdateUI.Callback var3, Context var4) {
      this.ftcEventLoopHandler = new FtcEventLoopHandler(var1, var3, var4);
      this.opModeRegister = var2;
   }

   private void a() {
      this.ftcEventLoopHandler.restartRobot();
   }

   private void a(String var1) {
      String var2 = this.ftcEventLoopHandler.getOpMode(var1);
      this.ftcEventLoopHandler.resetGamepads();
      this.opModeManager.initActiveOpMode(var2);
      this.ftcEventLoopHandler.sendCommand(new Command("CMD_INIT_OP_MODE_RESP", var2));
   }

   private void b() {
      String var1 = "";
      Iterator var2 = this.opModeManager.getOpModes().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if(!var3.equals("Stop Robot")) {
            if(!var1.isEmpty()) {
               var1 = var1 + Util.ASCII_RECORD_SEPARATOR;
            }

            var1 = var1 + var3;
         }
      }

      this.ftcEventLoopHandler.sendCommand(new Command("CMD_REQUEST_OP_MODE_LIST_RESP", var1));
   }

   private void c() {
      this.opModeManager.startActiveOpMode();
      this.ftcEventLoopHandler.sendCommand(new Command("CMD_RUN_OP_MODE_RESP", this.opModeManager.getActiveOpModeName()));
   }

   public OpModeManager getOpModeManager() {
      return this.opModeManager;
   }

   public void init(EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException {
      DbgLog.msg("======= INIT START =======");
      this.opModeManager.registerOpModes(this.opModeRegister);
      this.ftcEventLoopHandler.init(eventLoopManager);
      HardwareMap hardwareMap = this.ftcEventLoopHandler.getHardwareMap();
      ModernRoboticsUsbUtil.init(hardwareMap.appContext, hardwareMap);
      this.opModeManager.setHardwareMap(hardwareMap);
      hardwareMap.logDevices();
      DbgLog.msg("======= INIT FINISH =======");
   }

   public void loop() throws RobotCoreException {
      this.ftcEventLoopHandler.displayGamePadInfo(this.opModeManager.getActiveOpModeName());
      Gamepad[] gamepads = this.ftcEventLoopHandler.getGamepads();
      this.opModeManager.runActiveOpMode(gamepads);
      this.ftcEventLoopHandler.sendTelemetryData(this.opModeManager.getActiveOpMode().telemetry);
   }

   public void processCommand(Command var1) {
      DbgLog.msg("Processing Command: " + var1.getName() + " " + var1.getExtra());
      this.ftcEventLoopHandler.sendBatteryInfo();
      String var2 = var1.getName();
      String var3 = var1.getExtra();
      if(var2.equals("CMD_RESTART_ROBOT")) {
         this.a();
      } else if(var2.equals("CMD_REQUEST_OP_MODE_LIST")) {
         this.b();
      } else if(var2.equals("CMD_INIT_OP_MODE")) {
         this.a(var3);
      } else if(var2.equals("CMD_RUN_OP_MODE")) {
         this.c();
      } else {
         DbgLog.msg("Unknown command: " + var2);
      }
   }

   public void teardown() throws RobotCoreException {
      DbgLog.msg("======= TEARDOWN =======");
      this.opModeManager.stopActiveOpMode();
      this.ftcEventLoopHandler.shutdownMotorControllers();
      this.ftcEventLoopHandler.shutdownServoControllers();
      this.ftcEventLoopHandler.shutdownLegacyModules();
      this.ftcEventLoopHandler.shutdownCoreInterfaceDeviceModules();
      DbgLog.msg("======= TEARDOWN COMPLETE =======");
   }
}
