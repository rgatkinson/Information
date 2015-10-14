package com.qualcomm.ftccommon;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.qualcomm.ftccommon.R;

public class FtcRobotControllerSettingsActivity extends Activity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.getFragmentManager().beginTransaction().replace(16908290, new SettingsFragment()).commit();
   }

   public static class SettingsFragment extends PreferenceFragment {
      OnPreferenceClickListener a = new OnPreferenceClickListener() {
         public boolean onPreferenceClick(Preference var1) {
            Intent var2 = new Intent(var1.getIntent().getAction());
            SettingsFragment.this.startActivityForResult(var2, 3);
            return true;
         }
      };

      public void onActivityResult(int var1, int var2, Intent var3) {
         if(var1 == 3 && var2 == -1) {
            this.getActivity().setResult(-1, var3);
         }

      }

      public void onCreate(Bundle var1) {
         super.onCreate(var1);
         this.addPreferencesFromResource(R.xml.preferences);
         this.findPreference(this.getString(R.string.pref_launch_configure)).setOnPreferenceClickListener(this.a);
         this.findPreference(this.getString(R.string.pref_launch_autoconfigure)).setOnPreferenceClickListener(this.a);
         if(Build.MANUFACTURER.equalsIgnoreCase("zte") && Build.MODEL.equalsIgnoreCase("N9130")) {
            this.findPreference(this.getString(R.string.pref_launch_settings)).setOnPreferenceClickListener(new OnPreferenceClickListener() {
               public boolean onPreferenceClick(Preference var1) {
                  Intent var2 = SettingsFragment.this.getActivity().getPackageManager().getLaunchIntentForPackage("com.zte.wifichanneleditor");

                  try {
                     SettingsFragment.this.startActivity(var2);
                  } catch (NullPointerException var4) {
                     Toast.makeText(SettingsFragment.this.getActivity(), "Unable to launch ZTE WifiChannelEditor", 0).show();
                  }

                  return true;
               }
            });
         } else {
            this.findPreference(this.getString(R.string.pref_launch_settings)).setOnPreferenceClickListener(new OnPreferenceClickListener() {
               public boolean onPreferenceClick(Preference var1) {
                  Intent var2 = new Intent(var1.getIntent().getAction());
                  SettingsFragment.this.startActivity(var2);
                  return true;
               }
            });
         }

         if(Build.MODEL.equals("FL7007")) {
            this.findPreference(this.getString(R.string.pref_launch_settings)).setOnPreferenceClickListener(new OnPreferenceClickListener() {
               public boolean onPreferenceClick(Preference var1) {
                  Intent var2 = new Intent("android.settings.SETTINGS");
                  SettingsFragment.this.startActivity(var2);
                  return true;
               }
            });
         }

      }
   }
}
