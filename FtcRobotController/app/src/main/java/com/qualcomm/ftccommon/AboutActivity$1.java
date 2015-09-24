package com.qualcomm.ftccommon;

import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View;
import android.content.pm.PackageManager$NameNotFoundException;
import com.qualcomm.robotcore.util.Version;
import android.content.Context;
import android.widget.ArrayAdapter;

class AboutActivity$1 extends ArrayAdapter<String[]> {
    public String[] a(final int n) {
        final String[] array = new String[2];
        Label_0040: {
            if (n != 0) {
                break Label_0040;
            }
            try {
                array[0] = "App Version";
                array[1] = AboutActivity.this.getPackageManager().getPackageInfo(AboutActivity.this.getPackageName(), 0).versionName;
                Label_0038: {
                    return array;
                }
                // iftrue(Label_0038:, n != 1)
                array[0] = "Library Version";
                array[1] = Version.getLibraryVersion();
                return array;
            }
            catch (PackageManager$NameNotFoundException ex) {
                return array;
            }
        }
    }
    
    public int getCount() {
        return 2;
    }
    
    public View getView(final int n, final View view, final ViewGroup viewGroup) {
        final View view2 = super.getView(n, view, viewGroup);
        final TextView textView = (TextView)view2.findViewById(16908308);
        final TextView textView2 = (TextView)view2.findViewById(16908309);
        final String[] a = this.a(n);
        if (a.length == 2) {
            textView.setText((CharSequence)a[0]);
            textView2.setText((CharSequence)a[1]);
        }
        return view2;
    }
}