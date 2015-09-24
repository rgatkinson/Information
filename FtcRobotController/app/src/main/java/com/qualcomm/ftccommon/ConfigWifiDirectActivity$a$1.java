package com.qualcomm.ftccommon;

import android.app.ProgressDialog;

class ConfigWifiDirectActivity$a$1 implements Runnable {
    @Override
    public void run() {
        ConfigWifiDirectActivity.a(ConfigWifiDirectActivity.a.this.a, new ProgressDialog(ConfigWifiDirectActivity.a(ConfigWifiDirectActivity.a.this.a), R.style.CustomAlertDialog));
        ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.a.this.a).setMessage((CharSequence)"Please wait");
        ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.a.this.a).setTitle((CharSequence)"Configuring Wifi Direct");
        ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.a.this.a).setIndeterminate(true);
        ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.a.this.a).show();
    }
}