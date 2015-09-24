package com.qualcomm.ftccommon;

import android.content.Intent;
import android.content.Context;
import android.widget.Toast;
import android.preference.Preference;
import android.preference.Preference$OnPreferenceClickListener;

class FtcRobotControllerSettingsActivity$SettingsFragment$1 implements Preference$OnPreferenceClickListener {
    public boolean onPreferenceClick(final Preference preference) {
        final Intent launchIntentForPackage = SettingsFragment.this.getActivity().getPackageManager().getLaunchIntentForPackage("com.zte.wifichanneleditor");
        try {
            SettingsFragment.this.startActivity(launchIntentForPackage);
            return true;
        }
        catch (NullPointerException ex) {
            Toast.makeText((Context)SettingsFragment.this.getActivity(), (CharSequence)"Unable to launch ZTE WifiChannelEditor", 0).show();
            return true;
        }
    }
}