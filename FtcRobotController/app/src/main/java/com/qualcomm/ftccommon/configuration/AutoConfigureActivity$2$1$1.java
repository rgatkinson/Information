package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Map;
import java.util.HashMap;
import com.qualcomm.ftccommon.R;

class AutoConfigureActivity$2$1$1 implements Runnable {
    @Override
    public void run() {
        AutoConfigureActivity.b(Runnable.this.a.a).resetCount();
        if (Runnable.this.a.a.scannedDevices.size() == 0) {
            AutoConfigureActivity.b(Runnable.this.a.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
            AutoConfigureActivity.b(Runnable.this.a.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
            AutoConfigureActivity.c(Runnable.this.a.a);
        }
        Runnable.this.a.a.entries = Runnable.this.a.a.scannedDevices.entrySet();
        AutoConfigureActivity.a(Runnable.this.a.a, new HashMap());
        AutoConfigureActivity.b(Runnable.this.a.a).createLists(Runnable.this.a.a.entries, AutoConfigureActivity.d(Runnable.this.a.a));
        if (AutoConfigureActivity.h(Runnable.this.a.a)) {
            AutoConfigureActivity.a(Runnable.this.a.a, "K9USBBot");
            return;
        }
        AutoConfigureActivity.b(Runnable.this.a.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
        AutoConfigureActivity.b(Runnable.this.a.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        AutoConfigureActivity.i(Runnable.this.a.a);
    }
}