package com.qualcomm.ftccommon.configuration;

import android.app.AlertDialog;
import android.app.AlertDialog$Builder;
import android.widget.TextView;
import android.view.View;
import android.view.View$OnClickListener;

class FtcConfigurationActivity$5 implements View$OnClickListener {
    public void onClick(final View view) {
        final AlertDialog$Builder buildBuilder = FtcConfigurationActivity.a(FtcConfigurationActivity.this).buildBuilder("Save Configuration", "Clicking the save button will create an xml file in: \n      /sdcard/FIRST/  \nThis file will be used to initialize the robot.");
        buildBuilder.setPositiveButton((CharSequence)"Ok", FtcConfigurationActivity.this.a);
        final AlertDialog create = buildBuilder.create();
        create.show();
        ((TextView)create.findViewById(16908299)).setTextSize(14.0f);
    }
}