package com.qualcomm.ftccommon;

import android.content.Context;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftccommon.FtcEventLoopHandler;
import com.qualcomm.ftccommon.UpdateUI;
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
   FtcEventLoopHandler a;
   OpModeManager b = new OpModeManager(new HardwareMap());
   OpModeRegister c;

   public FtcEventLoop(HardwareFactory var1, OpModeRegister var2, UpdateUI.Callback var3, Context var4) {
      this.a = new FtcEventLoopHandler(var1, var3, var4);
      this.c = var2;
   }

   private void a() {
      this.a.restartRobot();
   }

   private void a(String var1) {
      String var2 = this.a.getOpMode(var1);
      this.b.initActiveOpMode(var2);
      this.a.sendCommand(new Command("CMD_INIT_OP_MODE_RESP", var2));
   }

   private void b() {
      String var1 = "";
      Iterator var2 = this.b.getOpModes().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if(!var3.equals("Stop Robot")) {
            if(!var1.isEmpty()) {
               var1 = var1 + Util.ASCII_RECORD_SEPARATOR;
            }

            var1 = var1 + var3;
         }
      }

      this.a.sendCommand(new Command("CMD_REQUEST_OP_MODE_LIST_RESP", var1));
   }

   private void c() {
      this.b.startActiveOpMode();
      this.a.sendCommand(new Command("CMD_RUN_OP_MODE_RESP", this.b.getActiveOpModeName()));
   }

   public OpModeManager getOpModeManager() {
      return this.b;
   }

   public void init(EventLoopManager var1) throws RobotCoreException, InterruptedException {
      DbgLog.msg("======= INIT START =======");
      this.b.registerOpModes(this.c);
      this.a.init(var1);
      HardwareMap var2 = this.a.getHardwareMap();
      this.b.setHardwareMap(var2);
      var2.logDevices();
      DbgLog.msg("======= INIT FINISH =======");
   }

   public void loop() throws RobotCoreException {
      this.a.displayGamePadInfo(this.b.getActiveOpModeName());
      Gamepad[] var1 = this.a.getGamepads();
      this.b.runActiveOpMode(var1);
      this.a.sendTelemetryData(this.b.getActiveOpMode().telemetry);
   }

   public void processCommand(Command var1) {
      DbgLog.msg("Processing Command: " + var1.getName() + " " + var1.getExtra());
      this.a.sendBatteryInfo();
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
      this.b.stopActiveOpMode();
      this.a.shutdownMotorControllers();
      this.a.shutdownServoControllers();
      this.a.shutdownLegacyModules();
      this.a.shutdownCoreInterfaceDeviceModules();
      DbgLog.msg("======= TEARDOWN COMPLETE =======");
   }
}
