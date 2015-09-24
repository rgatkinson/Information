package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.wifi.FixWifiDirectSetup;
import android.net.wifi.WifiManager;
import android.app.ProgressDialog;

private class a implements Runnable
{
    @Override
    public void run() {
        DbgLog.msg("attempting to reconfigure Wifi Direct");
        ConfigWifiDirectActivity.this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                ConfigWifiDirectActivity.a(ConfigWifiDirectActivity.this, new ProgressDialog(ConfigWifiDirectActivity.a(ConfigWifiDirectActivity.this), R.style.CustomAlertDialog));
                ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.this).setMessage((CharSequence)"Please wait");
                ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.this).setTitle((CharSequence)"Configuring Wifi Direct");
                ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.this).setIndeterminate(true);
                ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.this).show();
            }
        });
        final WifiManager wifiManager = (WifiManager)ConfigWifiDirectActivity.this.getSystemService("wifi");
        while (true) {
            try {
                FixWifiDirectSetup.fixWifiDirectSetup(wifiManager);
                ConfigWifiDirectActivity.this.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.this).dismiss();
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
