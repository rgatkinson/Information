package com.qualcomm.ftccommon;

import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Dimmer;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;

public class UpdateUI {
   Restarter restarter;
   FtcRobotControllerService ftcRobotControllerService;
   Activity activity;
   Dimmer dimmer;
   protected TextView textDeviceName;
   protected TextView textErrorMessage;
   protected TextView[] textGamepad = new TextView[2];
   protected TextView textOpMode;
   protected TextView textRobotStatus;
   protected TextView textWifiDirectStatus;

   public UpdateUI(Activity var1, Dimmer var2) {
      this.activity = var1;
      this.dimmer = var2;
   }

   private void a() {
      this.restarter.requestRestart();
   }

   private void a(final String var1) {
      DbgLog.msg(var1);
      this.activity.runOnUiThread(new Runnable()
      {
      public void run()
         {
         UpdateUI.this.textWifiDirectStatus.setText(var1);
         }
      });
   }

   private void b(final String var1) {
      this.activity.runOnUiThread(new Runnable()
      {
      public void run()
         {
         UpdateUI.this.textDeviceName.setText(var1);
         }
      });
   }

   public void setControllerService(FtcRobotControllerService var1) {
      this.ftcRobotControllerService = var1;
   }

   public void setRestarter(Restarter var1) {
      this.restarter = var1;
   }

   public void setTextViews(TextView var1, TextView var2, TextView[] var3, TextView var4, TextView var5, TextView var6) {
      this.textWifiDirectStatus = var1;
      this.textRobotStatus = var2;
      this.textGamepad = var3;
      this.textOpMode = var4;
      this.textErrorMessage = var5;
      this.textDeviceName = var6;
   }

   public class Callback {
      public void restartRobot() {
         UpdateUI.this.activity.runOnUiThread(new Runnable()
         {
         public void run()
            {
            Toast.makeText(UpdateUI.this.activity, "Restarting Robot", 0).show();
            }
         });
         (new Thread() {
            public void run() {
               try {
                  Thread.sleep(1500L);
               } catch (InterruptedException var2) {
                  ;
               }

               UpdateUI.this.activity.runOnUiThread(new Runnable()
               {
               public void run()
                  {
                  UpdateUI.this.a();
                  }
               });
            }
         }).start();
      }

      public void robotUpdate(final String var1) {
         DbgLog.msg(var1);
         UpdateUI.this.activity.runOnUiThread(new Runnable()
         {
         public void run()
            {
            UpdateUI.this.textRobotStatus.setText(var1);
            UpdateUI.this.textErrorMessage.setText(RobotLog.getGlobalErrorMsg());
            if (RobotLog.hasGlobalErrorMsg())
               {
               UpdateUI.this.dimmer.longBright();
               }

            }
         });
      }

      public void updateUi(final String var1, final Gamepad[] gamepads) {
         UpdateUI.this.activity.runOnUiThread(new Runnable()
         {
         public void run()
            {
            for (int var1x = 0; var1x < UpdateUI.this.textGamepad.length && var1x < gamepads.length; ++var1x)
               {
               if (gamepads[var1x].id == -1)
                  {
                  UpdateUI.this.textGamepad[var1x].setText(" ");
                  }
               else
                  {
                  UpdateUI.this.textGamepad[var1x].setText(gamepads[var1x].toString());
                  }
               }

            UpdateUI.this.textOpMode.setText("Op Mode: " + var1);
            UpdateUI.this.textErrorMessage.setText(RobotLog.getGlobalErrorMsg());
            }
         });
      }

      public void wifiDirectUpdate(WifiDirectAssistant.Event var1) {
         switch(var1.ordinal()) {
         case 1:
            UpdateUI.this.a("Wifi Direct - disconnected");
            return;
         case 2:
            UpdateUI.this.a("Wifi Direct - enabled");
            return;
         case 3:
            UpdateUI.this.a("Wifi Direct - ERROR");
            return;
         case 4:
            WifiDirectAssistant var2 = UpdateUI.this.ftcRobotControllerService.getWifiDirectAssistant();
            UpdateUI.this.b(var2.getDeviceName());
            return;
         default:
         }
      }
   }
}
