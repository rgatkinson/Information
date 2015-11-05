package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class OpModeManager {
   public static final OpMode DEFAULT_OP_MODE = new OpModeManager.a();
   public static final String DEFAULT_OP_MODE_NAME = "Stop Robot";
   private Map<String, Class<?>> a = new LinkedHashMap();
   private Map<String, OpMode> b = new LinkedHashMap();
   private String c = "Stop Robot";
   private OpMode d;
   private String e;
   private HardwareMap f;
   private HardwareMap g;
   private OpModeManager.b h;
   private boolean i;
   private boolean j;
   private boolean k;

   public OpModeManager(HardwareMap var1) {
      this.d = DEFAULT_OP_MODE;
      this.e = "Stop Robot";
      this.f = new HardwareMap();
      this.g = new HardwareMap();
      this.h = OpModeManager.b.a;
      this.i = false;
      this.j = false;
      this.k = false;
      this.f = var1;
      this.register("Stop Robot", OpModeManager.a.class);
      this.initActiveOpMode("Stop Robot");
   }

   private void a() {
      // $FF: Couldn't be decompiled
   }

   private void a(Exception var1) {
      RobotLog.e("Unable to start op mode " + this.c);
      RobotLog.logStacktrace(var1);
      this.c = "Stop Robot";
      this.d = DEFAULT_OP_MODE;
   }

   private boolean a(String var1) {
      return this.getOpModes().contains(var1);
   }

   public OpMode getActiveOpMode() {
      return this.d;
   }

   public String getActiveOpModeName() {
      return this.c;
   }

   public HardwareMap getHardwareMap() {
      return this.f;
   }

   public Set<String> getOpModes() {
      LinkedHashSet var1 = new LinkedHashSet();
      var1.addAll(this.a.keySet());
      var1.addAll(this.b.keySet());
      return var1;
   }

   public void initActiveOpMode(String var1) {
      this.e = var1;
      this.i = true;
      this.j = true;
      this.h = OpModeManager.b.a;
   }

   public void logOpModes() {
      int var1 = this.a.size() + this.b.size();
      RobotLog.i("There are " + var1 + " Op Modes");
      Iterator var2 = this.a.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var5 = (Entry)var2.next();
         RobotLog.i("   Op Mode: " + (String)var5.getKey());
      }

      Iterator var3 = this.b.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         RobotLog.i("   Op Mode: " + (String)var4.getKey());
      }

   }

   public void register(String var1, OpMode var2) {
      if(this.a(var1)) {
         throw new IllegalArgumentException("Cannot register the same op mode name twice");
      } else {
         this.b.put(var1, var2);
      }
   }

   public void register(String var1, Class var2) {
      if(this.a(var1)) {
         throw new IllegalArgumentException("Cannot register the same op mode name twice");
      } else {
         this.a.put(var1, var2);
      }
   }

   public void registerOpModes(OpModeRegister var1) {
      var1.register(this);
   }

   public void runActiveOpMode(Gamepad[] var1) {
      this.d.time = this.d.getRuntime();
      this.d.gamepad1 = var1[0];
      this.d.gamepad2 = var1[1];
      if(this.i) {
         this.d.stop();
         this.a();
         this.h = OpModeManager.b.a;
         this.j = true;
      }

      if(this.h == OpModeManager.b.a) {
         if(this.j) {
            this.d.hardwareMap = this.f;
            this.d.resetStartTime();
            this.d.init();
            this.j = false;
         }

         this.d.init_loop();
      } else {
         if(this.k) {
            this.d.start();
            this.k = false;
         }

         this.d.loop();
      }
   }

   public void setHardwareMap(HardwareMap var1) {
      this.f = var1;
   }

   public void startActiveOpMode() {
      this.h = OpModeManager.b.b;
      this.k = true;
   }

   public void stopActiveOpMode() {
      this.d.stop();
      this.initActiveOpMode("Stop Robot");
   }

   private static class a extends OpMode {
      private void a() {
         Iterator var1 = this.hardwareMap.servoController.iterator();

         while(var1.hasNext()) {
            ((ServoController)var1.next()).pwmDisable();
         }

         Iterator var2 = this.hardwareMap.dcMotorController.iterator();

         while(var2.hasNext()) {
            ((DcMotorController)var2.next()).setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
         }

         Iterator var3 = this.hardwareMap.dcMotor.iterator();

         while(var3.hasNext()) {
            DcMotor var5 = (DcMotor)var3.next();
            var5.setPower(0.0D);
            var5.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
         }

         Iterator var4 = this.hardwareMap.lightSensor.iterator();

         while(var4.hasNext()) {
            ((LightSensor)var4.next()).enableLed(false);
         }

      }

      public void init() {
         this.a();
      }

      public void init_loop() {
         this.a();
         this.telemetry.addData("Status", "Robot is stopped");
      }

      public void loop() {
         this.a();
         this.telemetry.addData("Status", "Robot is stopped");
      }

      public void stop() {
      }
   }

   private static enum b {
      a,
      b;

      static {
         OpModeManager.b[] var0 = new OpModeManager.b[]{a, b};
      }
   }
}
