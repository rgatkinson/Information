package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.exception.RobotCoreException;
import java.util.Map;
import com.qualcomm.ftccommon.R;
import com.qualcomm.ftccommon.DbgLog;

class FtcConfigurationActivity$6$1 implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                DbgLog.msg("Scanning USB bus");
                View$OnClickListener.this.a.scannedDevices = FtcConfigurationActivity.b(View$OnClickListener.this.a).scanForUsbDevices();
                View$OnClickListener.this.a.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        FtcConfigurationActivity.a(View$OnClickListener.this.a).resetCount();
                        FtcConfigurationActivity.c(View$OnClickListener.this.a);
                        FtcConfigurationActivity.d(View$OnClickListener.this.a);
                        FtcConfigurationActivity.a(View$OnClickListener.this.a).updateHeader(FtcConfigurationActivity.e(View$OnClickListener.this.a), R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                        View$OnClickListener.this.a.scannedEntries = View$OnClickListener.this.a.scannedDevices.entrySet();
                        FtcConfigurationActivity.a(View$OnClickListener.this.a, FtcConfigurationActivity.f(View$OnClickListener.this.a));
                        FtcConfigurationActivity.g(View$OnClickListener.this.a);
                        FtcConfigurationActivity.h(View$OnClickListener.this.a);
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
}