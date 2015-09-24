package com.qualcomm.ftccommon.configuration;

import java.util.Map;
import com.qualcomm.ftccommon.R;

class FtcConfigurationActivity$6$1$1 implements Runnable {
    @Override
    public void run() {
        FtcConfigurationActivity.a(Runnable.this.a.a).resetCount();
        FtcConfigurationActivity.c(Runnable.this.a.a);
        FtcConfigurationActivity.d(Runnable.this.a.a);
        FtcConfigurationActivity.a(Runnable.this.a.a).updateHeader(FtcConfigurationActivity.e(Runnable.this.a.a), R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        Runnable.this.a.a.scannedEntries = Runnable.this.a.a.scannedDevices.entrySet();
        FtcConfigurationActivity.a(Runnable.this.a.a, FtcConfigurationActivity.f(Runnable.this.a.a));
        FtcConfigurationActivity.g(Runnable.this.a.a);
        FtcConfigurationActivity.h(Runnable.this.a.a);
    }
}