package com.qualcomm.ftcdriverstation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FtcPairWifiDirectActivity extends Activity implements OnClickListener, WifiDirectAssistant.WifiDirectAssistantCallback {
   private static final int WIFI_SCAN_RATE = 10000;
   private String driverStationMac;
   private SharedPreferences sharedPref;
   private WifiDirectAssistant wifiDirect;
   private Handler wifiDirectHandler = new Handler();
   private FtcPairWifiDirectActivity.WifiDirectRunnable wifiDirectRunnable = new FtcPairWifiDirectActivity.WifiDirectRunnable();

   private void updateDevicesList(List<WifiP2pDevice> var1) {
      RadioGroup var2 = (RadioGroup)this.findViewById(2131492921);
      var2.clearCheck();
      var2.removeAllViews();
      FtcPairWifiDirectActivity.PeerRadioButton var3 = new FtcPairWifiDirectActivity.PeerRadioButton(this);
      String var4 = this.getString(2131361883);
      var3.setId(0);
      var3.setText("None\nDo not pair with any device");
      var3.setPadding(0, 0, 0, 24);
      var3.setOnClickListener(this);
      var3.setPeerMacAddress(var4);
      if(this.driverStationMac.equalsIgnoreCase(var4)) {
         var3.setChecked(true);
      }

      var2.addView(var3);
      int var5 = 1;
      Map var6 = this.buildMap(var1);

      int var11;
      for(Iterator var7 = var6.keySet().iterator(); var7.hasNext(); var5 = var11) {
         String var8 = (String)var7.next();
         String var9 = (String)var6.get(var8);
         FtcPairWifiDirectActivity.PeerRadioButton var10 = new FtcPairWifiDirectActivity.PeerRadioButton(this);
         var11 = var5 + 1;
         var10.setId(var5);
         var10.setText(var8 + "\n" + var9);
         var10.setPadding(0, 0, 0, 24);
         var10.setPeerMacAddress(var9);
         if(var9.equalsIgnoreCase(this.driverStationMac)) {
            var10.setChecked(true);
         }

         var10.setOnClickListener(this);
         var2.addView(var10);
      }

   }

   public Map<String, String> buildMap(List<WifiP2pDevice> var1) {
      TreeMap var2 = new TreeMap();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         WifiP2pDevice var4 = (WifiP2pDevice)var3.next();
         var2.put(var4.deviceName, var4.deviceAddress);
      }

      return var2;
   }

   public void onClick(View var1) {
      if(var1 instanceof FtcPairWifiDirectActivity.PeerRadioButton) {
         FtcPairWifiDirectActivity.PeerRadioButton var2 = (FtcPairWifiDirectActivity.PeerRadioButton)var1;
         if(var2.getId() == 0) {
            this.driverStationMac = this.getString(2131361883);
         } else {
            this.driverStationMac = var2.getPeerMacAddress();
         }

         Editor var3 = this.sharedPref.edit();
         var3.putString(this.getString(2131361882), this.driverStationMac);
         var3.commit();
         DbgLog.msg("Setting Driver Station MAC address to " + this.driverStationMac);
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2130903045);
      this.wifiDirect = WifiDirectAssistant.getWifiDirectAssistant(this);
   }

   public void onStart() {
      super.onStart();
      DbgLog.msg("Starting Pairing with Driver Station activity");
      this.sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
      this.driverStationMac = this.sharedPref.getString(this.getString(2131361882), this.getString(2131361883));
      this.wifiDirect.enable();
      this.wifiDirect.setCallback(this);
      this.updateDevicesList(this.wifiDirect.getPeers());
      this.wifiDirectHandler.postDelayed(this.wifiDirectRunnable, 10000L);
   }

   public void onStop() {
      super.onStop();
      this.wifiDirectHandler.removeCallbacks(this.wifiDirectRunnable);
      this.wifiDirect.cancelDiscoverPeers();
      this.wifiDirect.disable();
   }

   public void onWifiDirectEvent(WifiDirectAssistant.Event var1) {
      switch(null.$SwitchMap$com$qualcomm$robotcore$wifi$WifiDirectAssistant$Event[var1.ordinal()]) {
      case 1:
         this.updateDevicesList(this.wifiDirect.getPeers());
         return;
      default:
      }
   }

   public static class PeerRadioButton extends RadioButton {
      private String peerMacAddress = "";

      public PeerRadioButton(Context var1) {
         super(var1);
      }

      public String getPeerMacAddress() {
         return this.peerMacAddress;
      }

      public void setPeerMacAddress(String var1) {
         this.peerMacAddress = var1;
      }
   }

   public class WifiDirectRunnable implements Runnable {
      public void run() {
         FtcPairWifiDirectActivity.this.wifiDirect.discoverPeers();
         FtcPairWifiDirectActivity.this.wifiDirectHandler.postDelayed(FtcPairWifiDirectActivity.this.wifiDirectRunnable, 10000L);
      }
   }
}
