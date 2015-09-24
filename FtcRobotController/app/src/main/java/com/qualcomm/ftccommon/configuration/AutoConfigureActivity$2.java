package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Map;
import java.util.HashMap;
import com.qualcomm.ftccommon.R;
import com.qualcomm.ftccommon.DbgLog;
import android.view.View;
import android.view.View$OnClickListener;

class AutoConfigureActivity$2 implements View$OnClickListener {
    public void onClick(final View view) {
        AutoConfigureActivity.a(AutoConfigureActivity.this, new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        DbgLog.msg("Scanning USB bus");
                        AutoConfigureActivity.this.scannedDevices = AutoConfigureActivity.a(AutoConfigureActivity.this).scanForUsbDevices();
                        AutoConfigureActivity.this.runOnUiThread((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                AutoConfigureActivity.b(AutoConfigureActivity.this).resetCount();
                                if (AutoConfigureActivity.this.scannedDevices.size() == 0) {
                                    AutoConfigureActivity.b(AutoConfigureActivity.this).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                                    AutoConfigureActivity.b(AutoConfigureActivity.this).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                                    AutoConfigureActivity.c(AutoConfigureActivity.this);
                                }
                                AutoConfigureActivity.this.entries = AutoConfigureActivity.this.scannedDevices.entrySet();
                                AutoConfigureActivity.a(AutoConfigureActivity.this, new HashMap());
                                AutoConfigureActivity.b(AutoConfigureActivity.this).createLists(AutoConfigureActivity.this.entries, AutoConfigureActivity.d(AutoConfigureActivity.this));
                                if (AutoConfigureActivity.h(AutoConfigureActivity.this)) {
                                    AutoConfigureActivity.a(AutoConfigureActivity.this, "K9USBBot");
                                    return;
                                }
                                AutoConfigureActivity.b(AutoConfigureActivity.this).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                                AutoConfigureActivity.b(AutoConfigureActivity.this).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                                AutoConfigureActivity.i(AutoConfigureActivity.this);
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
        }));
        AutoConfigureActivity.g(AutoConfigureActivity.this).start();
    }
}