package com.qualcomm.ftccommon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.qualcomm.wirelessP2p.WifiDirectChannelSelection;

import java.io.IOException;

public class FtcWifiChannelSelectorActivity extends Activity implements OnClickListener, OnItemSelectedListener {
   private static int a = 0;
   private Button b;
   private Button c;
   private Spinner d;
   private ProgressDialog e;
   private WifiDirectChannelSelection f;
   private int g = -1;
   private int h = -1;
   private Context i;

   private void a() {
      Object[] var1 = new Object[]{Integer.valueOf(this.g), Integer.valueOf(this.h)};
      DbgLog.msg(String.format("configure p2p channel - class %d channel %d", var1));

      try {
         this.e = ProgressDialog.show(this, "Configuring Channel", "Please Wait", true);
         this.f.config(this.g, this.h);
         (new Thread(new Runnable() {
            public void run() {
               try {
                  Thread.sleep(5000L);
               } catch (InterruptedException var2) {
               }

               FtcWifiChannelSelectorActivity.this.runOnUiThread(new Runnable() {
                  public void run() {
                     FtcWifiChannelSelectorActivity.this.setResult(-1);
                     FtcWifiChannelSelectorActivity.this.e.dismiss();
                     FtcWifiChannelSelectorActivity.this.finish();
                  }
               });
            }
         })).start();
      } catch (IOException var3) {
         this.a("Failed - root is required", 0);
         var3.printStackTrace();
      }
   }

   private void a(final String var1, final int var2) {
      this.runOnUiThread(new Runnable() {
         public void run() {
            Toast.makeText(FtcWifiChannelSelectorActivity.this.i, var1, var2).show();
         }
      });
   }

   public void onClick(View var1) {
      if(var1.getId() == R.id.buttonConfigure) {
         a = this.d.getSelectedItemPosition();
         this.a();
      } else if(var1.getId() == R.id.buttonWifiSettings) {
         DbgLog.msg("launch wifi settings");
         this.startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
         return;
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.activity_ftc_wifi_channel_selector);
      this.i = this;
      this.d = (Spinner)this.findViewById(R.id.spinnerChannelSelect);
      ArrayAdapter var2 = ArrayAdapter.createFromResource(this, R.array.wifi_direct_channels, R.layout.activity_ftc_wifi_channel_selector);
      var2.setDropDownViewResource(/* 17367049 */ R.layout.activity_ftc_wifi_channel_selector);
      this.d.setAdapter(var2);
      this.d.setOnItemSelectedListener(this);
      this.b = (Button)this.findViewById(R.id.buttonConfigure);
      this.b.setOnClickListener(this);
      this.c = (Button)this.findViewById(R.id.buttonWifiSettings);
      this.c.setOnClickListener(this);
      this.f = new WifiDirectChannelSelection(this, (WifiManager) this.getSystemService(WIFI_SERVICE));
   }

   public void onItemSelected(AdapterView<?> var1, View var2, int var3, long var4) {
      switch(var3) {
      case 0:
         this.g = -1;
         this.h = -1;
         return;
      case 1:
         this.g = 81;
         this.h = 1;
         return;
      case 2:
         this.g = 81;
         this.h = 6;
         return;
      case 3:
         this.g = 81;
         this.h = 11;
         return;
      case 4:
         this.g = 124;
         this.h = 149;
         return;
      case 5:
         this.g = 124;
         this.h = 153;
         return;
      case 6:
         this.g = 124;
         this.h = 157;
         return;
      case 7:
         this.g = 124;
         this.h = 161;
         return;
      default:
      }
   }

   public void onNothingSelected(AdapterView<?> var1) {
   }

   protected void onStart() {
      super.onStart();
      this.d.setSelection(a);
   }
}
