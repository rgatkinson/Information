package com.qualcomm.ftccommon;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareFactory;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.Util;
import java.util.Iterator;

public class FtcEventLoop implements EventLoop {
   FtcEventLoopHandler ftcEventLoopHandler;
   OpModeManager opModeManager = new OpModeManager(new HardwareMap());
   OpModeRegister opModeRegister;

   public FtcEventLoop(HardwareFactory hardwareFactory, OpModeRegister opModeRegister, UpdateUI.Callback updateUICallback, Context context) {
      this.ftcEventLoopHandler = new FtcEventLoopHandler(hardwareFactory, updateUICallback, context);
      this.opModeRegister = opModeRegister;
   }

   private void restartRobot() {
      this.ftcEventLoopHandler.restartRobot();
   }

   private void onInitOpModeCommand(String var1) {
      String var2 = this.ftcEventLoopHandler.getOpMode(var1);
      this.opModeManager.initActiveOpMode(var2);
      this.ftcEventLoopHandler.sendCommand(new Command("CMD_INIT_OP_MODE_RESP", var2));
   }

   private void onOpModeListRequestedCommand() {
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

   private void onRunOpModeCommand() {
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
      HardwareMap hwmap = this.ftcEventLoopHandler.getHardwareMap();
      this.opModeManager.setHardwareMap(hwmap);
      hwmap.logDevices();
      DbgLog.msg("======= INIT FINISH =======");
   }

   public void loop() throws RobotCoreException {
      this.ftcEventLoopHandler.displayGamePadInfo(this.opModeManager.getActiveOpModeName());
      Gamepad[] gamepads = this.ftcEventLoopHandler.getGamepads();
      this.opModeManager.runActiveOpMode(gamepads);
      this.ftcEventLoopHandler.sendTelemetryData(this.opModeManager.getActiveOpMode().telemetry);
   }

   public void processCommand(Command command) {
      DbgLog.msg("Processing Command: " + command.getName() + " " + command.getExtra());
      this.ftcEventLoopHandler.sendBatteryInfo();
      String commandName = command.getName();
      String extra = command.getExtra();
      if(commandName.equals("CMD_RESTART_ROBOT")) {
         this.restartRobot();
      } else if(commandName.equals("CMD_REQUEST_OP_MODE_LIST")) {
         this.onOpModeListRequestedCommand();
      } else if(commandName.equals("CMD_INIT_OP_MODE")) {
         this.onInitOpModeCommand(extra);
      } else if(commandName.equals("CMD_RUN_OP_MODE")) {
         this.onRunOpModeCommand();
      } else {
         DbgLog.msg("Unknown command: " + commandName);
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
