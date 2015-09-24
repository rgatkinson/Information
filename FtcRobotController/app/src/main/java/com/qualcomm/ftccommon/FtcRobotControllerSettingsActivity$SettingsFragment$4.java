package com.qualcomm.ftccommon;

import android.content.Intent;
import android.preference.Preference;
import android.preference.Preference$OnPreferenceClickListener;

class FtcRobotControllerSettingsActivity$SettingsFragment$4 implements Preference$OnPreferenceClickListener {
    public boolean onPreferenceClick(final Preference preference) {
        SettingsFragment.this.startActivityForResult(new Intent(preference.getIntent().getAction()), 3);
        return true;
    }
}