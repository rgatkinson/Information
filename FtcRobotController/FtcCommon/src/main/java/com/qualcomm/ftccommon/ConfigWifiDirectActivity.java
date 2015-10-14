package com.qualcomm.ftccommon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.wifi.FixWifiDirectSetup;

public class ConfigWifiDirectActivity extends Activity {
   private ProgressDialog a;
   private Context b;

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.activity_config_wifi_direct);
      this.b = this;
   }

   protected void onResume() {
      super.onResume();
      (new Thread(new a(null))).start();
   }

   private class a implements Runnable {
      private a() {
      }

      // $FF: synthetic method
      a(Object var2) {
         this();
      }

      public void run() {
         DbgLog.msg("attempting to reconfigure Wifi Direct");
         ConfigWifiDirectActivity.this.runOnUiThread(new Runnable() {
            public void run() {
               ConfigWifiDirectActivity.this.a = new ProgressDialog(ConfigWifiDirectActivity.this.b, R.style.CustomAlertDialog);
               ConfigWifiDirectActivity.this.a.setMessage("Please wait");
               ConfigWifiDirectActivity.this.a.setTitle("Configuring Wifi Direct");
               ConfigWifiDirectActivity.this.a.setIndeterminate(true);
               ConfigWifiDirectActivity.this.a.show();
            }
         });
         WifiManager var1 = (WifiManager)ConfigWifiDirectActivity.this.getSystemService("wifi");

         try {
            FixWifiDirectSetup.fixWifiDirectSetup(var1);
         } catch (InterruptedException var3) {
            DbgLog.msg("Cannot fix wifi setup - interrupted");
         }

         ConfigWifiDirectActivity.this.runOnUiThread(new Runnable() {
            public void run() {
               ConfigWifiDirectActivity.this.a.dismiss();
               ConfigWifiDirectActivity.this.finish();
            }
         });
      }
   }
}
