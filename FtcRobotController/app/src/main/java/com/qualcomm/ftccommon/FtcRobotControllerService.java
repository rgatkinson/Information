package com.qualcomm.ftccommon;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftccommon.UpdateUI;
import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;

public class FtcRobotControllerService extends Service implements WifiDirectAssistant.WifiDirectAssistantCallback {
   private final IBinder a = new FtcRobotControllerService.FtcRobotControllerBinder();
   private WifiDirectAssistant b;
   private Robot c;
   private EventLoop d;
   private WifiDirectAssistant.Event e;
   private String f;
   private UpdateUI.Callback g;
   private final FtcRobotControllerService.a h;
   private final ElapsedTime i;
   private Thread j;

   public FtcRobotControllerService() {
      this.e = WifiDirectAssistant.Event.DISCONNECTED;
      this.f = "Robot Status: null";
      this.g = null;
      this.h = new FtcRobotControllerService.a(null);
      this.i = new ElapsedTime();
      this.j = null;
   }

   // $FF: synthetic method
   static Robot a(FtcRobotControllerService var0, Robot var1) {
      var0.c = var1;
      return var1;
   }

   // $FF: synthetic method
   static void a(FtcRobotControllerService var0, String var1) {
      var0.a(var1);
   }

   private void a(WifiDirectAssistant.Event var1) {
      this.e = var1;
      if(this.g != null) {
         this.g.wifiDirectUpdate(this.e);
      }

   }

   private void a(String var1) {
      this.f = var1;
      if(this.g != null) {
         this.g.robotUpdate(var1);
      }

   }

   // $FF: synthetic method
   static Robot b(FtcRobotControllerService var0) {
      return var0.c;
   }

   // $FF: synthetic method
   static ElapsedTime c(FtcRobotControllerService var0) {
      return var0.i;
   }

   // $FF: synthetic method
   static WifiDirectAssistant d(FtcRobotControllerService var0) {
      return var0.b;
   }

   // $FF: synthetic method
   static FtcRobotControllerService.a e(FtcRobotControllerService var0) {
      return var0.h;
   }

   // $FF: synthetic method
   static EventLoop f(FtcRobotControllerService var0) {
      return var0.d;
   }

   public String getRobotStatus() {
      return this.f;
   }

   public WifiDirectAssistant getWifiDirectAssistant() {
      return this.b;
   }

   public WifiDirectAssistant.Event getWifiDirectStatus() {
      return this.e;
   }

   public IBinder onBind(Intent var1) {
      DbgLog.msg("Starting FTC Controller Service");
      DbgLog.msg("Android device is " + Build.MANUFACTURER + ", " + Build.MODEL);
      this.b = WifiDirectAssistant.getWifiDirectAssistant(this);
      this.b.setCallback(this);
      this.b.enable();
      if(Build.MODEL.equals("FL7007")) {
         this.b.discoverPeers();
      } else {
         this.b.createGroup();
      }

      return this.a;
   }

   public boolean onUnbind(Intent var1) {
      DbgLog.msg("Stopping FTC Controller Service");
      this.b.disable();
      this.shutdownRobot();
      return false;
   }

   public void onWifiDirectEvent(WifiDirectAssistant.Event var1) {
      switch(null.b[var1.ordinal()]) {
      case 1:
         DbgLog.msg("Wifi Direct - Group Owner");
         this.b.cancelDiscoverPeers();
         break;
      case 2:
         DbgLog.error("Wifi Direct - connected as peer, was expecting Group Owner");
         break;
      case 3:
         DbgLog.msg("Wifi Direct Passphrase: " + this.b.getPassphrase());
         break;
      case 4:
         DbgLog.error("Wifi Direct Error: " + this.b.getFailureReason());
      }

      this.a(var1);
   }

   public void setCallback(UpdateUI.Callback var1) {
      synchronized(this){}

      try {
         this.g = var1;
      } finally {
         ;
      }

   }

   public void setupRobot(EventLoop param1) {
      // $FF: Couldn't be decompiled
   }

   public void shutdownRobot() {
      synchronized(this){}

      try {
         if(this.j != null && this.j.isAlive()) {
            this.j.interrupt();
         }

         if(this.c != null) {
            this.c.shutdown();
         }

         this.c = null;
         this.a("Robot Status: null");
      } finally {
         ;
      }

   }

   public class FtcRobotControllerBinder extends Binder {
      public FtcRobotControllerService getService() {
         return FtcRobotControllerService.this;
      }
   }

   private class a implements EventLoopManager.EventLoopMonitor {
      private a() {
      }

      // $FF: synthetic method
      a(Object var2) {
         this();
      }

      public void onStateChange(RobotState var1) {
         if(FtcRobotControllerService.this.g != null) {
            switch(null.a[var1.ordinal()]) {
            case 1:
               FtcRobotControllerService.this.g.robotUpdate("Robot Status: init");
               return;
            case 2:
               FtcRobotControllerService.this.g.robotUpdate("Robot Status: not started");
               return;
            case 3:
               FtcRobotControllerService.this.g.robotUpdate("Robot Status: running");
               return;
            case 4:
               FtcRobotControllerService.this.g.robotUpdate("Robot Status: stopped");
               return;
            case 5:
               FtcRobotControllerService.this.g.robotUpdate("Robot Status: EMERGENCY STOP");
               return;
            case 6:
               FtcRobotControllerService.this.g.robotUpdate("Robot Status: dropped connection");
               return;
            default:
            }
         }
      }
   }

   private class b implements Runnable {
      private b() {
      }

      // $FF: synthetic method
      b(Object var2) {
         this();
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }
   }
}
