package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

public abstract class LinearOpMode extends OpMode {
   private LinearOpMode.a a = null;
   private Thread b = null;
   private ElapsedTime c = new ElapsedTime();
   private volatile boolean d = false;

   public final void init() {
      this.a = new LinearOpMode.a(this);
      this.b = new Thread(this.a);
      this.b.start();
   }

   public final void init_loop() {
   }

   public final void loop() {
      // $FF: Couldn't be decompiled
   }

   public boolean opModeIsActive() {
      return this.d;
   }

   public abstract void runOpMode() throws InterruptedException;

   public void sleep(long var1) throws InterruptedException {
      Thread.sleep(var1);
   }

   public final void start() {
      // $FF: Couldn't be decompiled
   }

   public final void stop() {
      this.d = false;
      if(!this.a.c()) {
         this.b.interrupt();
      }

      this.c.reset();

      while(!this.a.c() && this.c.time() < 0.5D) {
         Thread.yield();
      }

      if(!this.a.c()) {
         RobotLog.e("*****************************************************************");
         RobotLog.e("User Linear Op Mode took too long to exit; emergency killing app.");
         RobotLog.e("Possible infinite loop in user code?");
         RobotLog.e("*****************************************************************");
         System.exit(-1);
      }

   }

   public void waitForNextHardwareCycle() throws InterruptedException {
      // $FF: Couldn't be decompiled
   }

   public void waitForStart() throws InterruptedException {
      // $FF: Couldn't be decompiled
   }

   public void waitOneFullHardwareCycle() throws InterruptedException {
      this.waitForNextHardwareCycle();
      Thread.sleep(1L);
      this.waitForNextHardwareCycle();
   }

   private static class a implements Runnable {
      private RuntimeException a = null;
      private boolean b = false;
      private final LinearOpMode c;

      public a(LinearOpMode var1) {
         this.c = var1;
      }

      public boolean a() {
         return this.a != null;
      }

      public RuntimeException b() {
         return this.a;
      }

      public boolean c() {
         return this.b;
      }

      public void run() {
         this.a = null;
         this.b = false;

         try {
            this.c.runOpMode();
            return;
         } catch (InterruptedException var7) {
            RobotLog.d("LinearOpMode received an Interrupted Exception; shutting down this linear op mode");
         } catch (RuntimeException var8) {
            this.a = var8;
            return;
         } finally {
            this.b = true;
         }

      }
   }
}
