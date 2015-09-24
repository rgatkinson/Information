package com.qualcomm.ftccommon;

import android.content.Intent;
import android.preference.Preference;
import android.preference.Preference$OnPreferenceClickListener;

class FtcRobotControllerSettingsActivity$SettingsFragment$2 implements Preference$OnPreferenceClickListener {
    public boolean onPreferenceClick(final Preference preference) {
        SettingsFragment.this.startActivity(new Intent(preference.getIntent().getAction()));
        return true;
    }
}