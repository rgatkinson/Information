package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.wifi.FixWifiDirectSetup;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.content.Context;
import android.app.ProgressDialog;
import android.app.Activity;

public class ConfigWifiDirectActivity extends Activity
{
    private ProgressDialog a;
    private Context b;
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_config_wifi_direct);
        this.b = (Context)this;
    }
    
    protected void onResume() {
        super.onResume();
        new Thread(new a()).start();
    }
    
    private class a implements Runnable
    {
        @Override
        public void run() {
            DbgLog.msg("attempting to reconfigure Wifi Direct");
            ConfigWifiDirectActivity.this.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    ConfigWifiDirectActivity.this.a = new ProgressDialog(ConfigWifiDirectActivity.this.b, R.style.CustomAlertDialog);
                    ConfigWifiDirectActivity.this.a.setMessage((CharSequence)"Please wait");
                    ConfigWifiDirectActivity.this.a.setTitle((CharSequence)"Configuring Wifi Direct");
                    ConfigWifiDirectActivity.this.a.setIndeterminate(true);
                    ConfigWifiDirectActivity.this.a.show();
                }
            });
            final WifiManager wifiManager = (WifiManager)ConfigWifiDirectActivity.this.getSystemService("wifi");
            while (true) {
                try {
                    FixWifiDirectSetup.fixWifiDirectSetup(wifiManager);
                    ConfigWifiDirectActivity.this.runOnUiThread((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            ConfigWifiDirectActivity.this.a.dismiss();
                            ConfigWifiDirectActivity.this.finish();
                        }
                    });
                }
                catch (InterruptedException ex) {
                    DbgLog.msg("Cannot fix wifi setup - interrupted");
                    continue;
                }
                break;
            }
        }
    }
}
