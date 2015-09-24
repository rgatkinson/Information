package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Map;
import java.util.HashMap;
import com.qualcomm.ftccommon.R;
import com.qualcomm.ftccommon.DbgLog;

class AutoConfigureActivity$1$1 implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                DbgLog.msg("Scanning USB bus");
                View$OnClickListener.this.a.scannedDevices = AutoConfigureActivity.a(View$OnClickListener.this.a).scanForUsbDevices();
                View$OnClickListener.this.a.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        AutoConfigureActivity.b(View$OnClickListener.this.a).resetCount();
                        if (View$OnClickListener.this.a.scannedDevices.size() == 0) {
                            AutoConfigureActivity.b(View$OnClickListener.this.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                            AutoConfigureActivity.b(View$OnClickListener.this.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                            AutoConfigureActivity.c(View$OnClickListener.this.a);
                        }
                        View$OnClickListener.this.a.entries = View$OnClickListener.this.a.scannedDevices.entrySet();
                        AutoConfigureActivity.a(View$OnClickListener.this.a, new HashMap());
                        AutoConfigureActivity.b(View$OnClickListener.this.a).createLists(View$OnClickListener.this.a.entries, AutoConfigureActivity.d(View$OnClickListener.this.a));
                        if (AutoConfigureActivity.e(View$OnClickListener.this.a)) {
                            AutoConfigureActivity.a(View$OnClickListener.this.a, "K9LegacyBot");
                            return;
                        }
                        AutoConfigureActivity.b(View$OnClickListener.this.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                        AutoConfigureActivity.b(View$OnClickListener.this.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                        AutoConfigureActivity.f(View$OnClickListener.this.a);
                    }
                });
            }
            catch (RobotCoreException ex) {
                DbgLog.error("Device scan failed");
                continue;
            }
            break;
        }
    }
}