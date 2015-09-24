package com.qualcomm.ftccommon;

import android.content.Context;
import android.widget.Toast;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.preference.Preference;
import android.preference.Preference$OnPreferenceClickListener;
import android.preference.PreferenceFragment;

public static class SettingsFragment extends PreferenceFragment
{
    Preference$OnPreferenceClickListener a;
    
    public SettingsFragment() {
        this.a = (Preference$OnPreferenceClickListener)new Preference$OnPreferenceClickListener() {
            public boolean onPreferenceClick(final Preference preference) {
                SettingsFragment.this.startActivityForResult(new Intent(preference.getIntent().getAction()), 3);
                return true;
            }
        };
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        if (n == 3 && n2 == -1) {
            this.getActivity().setResult(-1, intent);
        }
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.addPreferencesFromResource(R.xml.preferences);
        this.findPreference((CharSequence)this.getString(R.string.pref_launch_configure)).setOnPreferenceClickListener(this.a);
        this.findPreference((CharSequence)this.getString(R.string.pref_launch_autoconfigure)).setOnPreferenceClickListener(this.a);
        if (Build.MANUFACTURER.equalsIgnoreCase("zte") && Build.MODEL.equalsIgnoreCase("N9130")) {
            this.findPreference((CharSequence)this.getString(R.string.pref_launch_settings)).setOnPreferenceClickListener((Preference$OnPreferenceClickListener)new Preference$OnPreferenceClickListener() {
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
            });
        }
        else {
            this.findPreference((CharSequence)this.getString(R.string.pref_launch_settings)).setOnPreferenceClickListener((Preference$OnPreferenceClickListener)new Preference$OnPreferenceClickListener() {
                public boolean onPreferenceClick(final Preference preference) {
                    SettingsFragment.this.startActivity(new Intent(preference.getIntent().getAction()));
                    return true;
                }
            });
        }
        if (Build.MODEL.equals("FL7007")) {
            this.findPreference((CharSequence)this.getString(R.string.pref_launch_settings)).setOnPreferenceClickListener((Preference$OnPreferenceClickListener)new Preference$OnPreferenceClickListener() {
                public boolean onPreferenceClick(final Preference preference) {
                    SettingsFragment.this.startActivity(new Intent("android.settings.SETTINGS"));
                    return true;
                }
            });
        }
    }
}
