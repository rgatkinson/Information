package com.qualcomm.ftccommon;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.util.Version;

public class AboutActivity extends Activity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.about);
      ((ListView)this.findViewById(R.id.aboutList)).setAdapter(new ArrayAdapter(this, 17367044, 16908308) {
         public String[] a(int var1) {
            String[] var2 = new String[2];
            if(var1 == 0) {
               try {
                  var2[0] = "App Version";
                  var2[1] = AboutActivity.this.getPackageManager().getPackageInfo(AboutActivity.this.getPackageName(), 0).versionName;
               } catch (NameNotFoundException var4) {
                  return var2;
               }
            } else if(var1 == 1) {
               var2[0] = "Library Version";
               var2[1] = Version.getLibraryVersion();
               return var2;
            }

            return var2;
         }

         public int getCount() {
            return 2;
         }

         // $FF: synthetic method
         public Object getItem(int var1) {
            return this.a(var1);
         }

         public View getView(int var1, View var2, ViewGroup var3) {
            View var4 = super.getView(var1, var2, var3);
            TextView var5 = (TextView)var4.findViewById(16908308);
            TextView var6 = (TextView)var4.findViewById(16908309);
            String[] var7 = this.a(var1);
            if(var7.length == 2) {
               var5.setText(var7[0]);
               var6.setText(var7[1]);
            }

            return var4;
         }
      });
   }
}
