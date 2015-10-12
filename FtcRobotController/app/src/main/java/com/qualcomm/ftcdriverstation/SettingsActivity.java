package com.qualcomm.ftcdriverstation;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsActivity extends Activity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2130903048);
      this.getFragmentManager().beginTransaction().replace(16908290, new SettingsActivity.SettingsFragment()).commit();
   }

   public static class SettingsFragment extends PreferenceFragment {
      public void onCreate(Bundle var1) {
         super.onCreate(var1);
         this.addPreferencesFromResource(2131034112);
      }
   }
}
