package com.qualcomm.ftccommon.configuration;

import com.qualcomm.ftccommon.R;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;

class FtcConfigurationActivity$10 implements DialogInterface$OnClickListener {
    public void onClick(final DialogInterface dialogInterface, final int n) {
        FtcConfigurationActivity.a(FtcConfigurationActivity.this).saveToPreferences(FtcConfigurationActivity.e(FtcConfigurationActivity.this).substring(7).trim(), R.string.pref_hardware_config_filename);
        FtcConfigurationActivity.this.finish();
    }
}