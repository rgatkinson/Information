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

   private void doRestartRebot() {
      this.ftcEventLoopHandler.restartRobot();
   }

   private void doInitOpMode(String opModeName) {
      String opModeToInit = this.ftcEventLoopHandler.getOpMode(opModeName);
      this.ftcEventLoopHandler.resetGamepads();
      this.opModeManager.initActiveOpMode(opModeToInit);
      this.ftcEventLoopHandler.sendCommand(new Command("CMD_INIT_OP_MODE_RESP", opModeToInit));
   }

   private void sendOpModeList() {
      String result = "";
      Iterator iterator = this.opModeManager.getOpModes().iterator();
      while(iterator.hasNext()) {
         String opModeName = (String)iterator.next();
         if(!opModeName.equals("Stop Robot")) {
            if(!result.isEmpty()) {
               result = result + Util.ASCII_RECORD_SEPARATOR;
            }

            result = result + opModeName;
         }
      }

      this.ftcEventLoopHandler.sendCommand(new Command("CMD_REQUEST_OP_MODE_LIST_RESP", result));
   }

   private void doRunOpMode() {
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

   public void processCommand(Command command) {
      DbgLog.msg("Processing Command: " + command.getName() + " " + command.getExtra());
      this.ftcEventLoopHandler.sendBatteryInfo();
      String commandName = command.getName();
      String opModeName = command.getExtra();
      if(commandName.equals("CMD_RESTART_ROBOT")) {
         this.doRestartRebot();
      } else if(commandName.equals("CMD_REQUEST_OP_MODE_LIST")) {
         this.sendOpModeList();
      } else if(commandName.equals("CMD_INIT_OP_MODE")) {
         this.doInitOpMode(opModeName);
      } else if(commandName.equals("CMD_RUN_OP_MODE")) {
         this.doRunOpMode();
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
