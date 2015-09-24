package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.exception.RobotCoreException;
import java.util.Map;
import com.qualcomm.ftccommon.R;
import com.qualcomm.ftccommon.DbgLog;
import android.view.View;
import android.view.View$OnClickListener;

class FtcConfigurationActivity$6 implements View$OnClickListener {
    public void onClick(final View view) {
        FtcConfigurationActivity.a(FtcConfigurationActivity.this, new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        DbgLog.msg("Scanning USB bus");
                        FtcConfigurationActivity.this.scannedDevices = FtcConfigurationActivity.b(FtcConfigurationActivity.this).scanForUsbDevices();
                        FtcConfigurationActivity.this.runOnUiThread((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                FtcConfigurationActivity.a(FtcConfigurationActivity.this).resetCount();
                                FtcConfigurationActivity.c(FtcConfigurationActivity.this);
                                FtcConfigurationActivity.d(FtcConfigurationActivity.this);
                                FtcConfigurationActivity.a(FtcConfigurationActivity.this).updateHeader(FtcConfigurationActivity.e(FtcConfigurationActivity.this), R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                                FtcConfigurationActivity.this.scannedEntries = FtcConfigurationActivity.this.scannedDevices.entrySet();
                                FtcConfigurationActivity.a(FtcConfigurationActivity.this, FtcConfigurationActivity.f(FtcConfigurationActivity.this));
                                FtcConfigurationActivity.g(FtcConfigurationActivity.this);
                                FtcConfigurationActivity.h(FtcConfigurationActivity.this);
                            }
                        });
                    }
                    catch (RobotCoreException ex) {
                        DbgLog.error("Device scan failed: " + ex.toString());
                        continue;
                    }
                    break;
                }
            }
        }));
        FtcConfigurationActivity.i(FtcConfigurationActivity.this);
    }
}