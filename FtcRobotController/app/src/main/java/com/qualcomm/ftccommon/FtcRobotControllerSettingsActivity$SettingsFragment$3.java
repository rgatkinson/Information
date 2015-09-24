package com.qualcomm.ftccommon;

import android.content.Intent;
import android.preference.Preference;
import android.preference.Preference$OnPreferenceClickListener;

class FtcRobotControllerSettingsActivity$SettingsFragment$3 implements Preference$OnPreferenceClickListener {
    public boolean onPreferenceClick(final Preference preference) {
        SettingsFragment.this.startActivity(new Intent("android.settings.SETTINGS"));
        return true;
    }
}