package com.qualcomm.ftccommon.configuration;

import android.app.AlertDialog;
import android.app.AlertDialog$Builder;
import android.widget.TextView;
import android.view.View;
import android.view.View$OnClickListener;

class FtcConfigurationActivity$4 implements View$OnClickListener {
    public void onClick(final View view) {
        final AlertDialog$Builder buildBuilder = FtcConfigurationActivity.a(FtcConfigurationActivity.this).buildBuilder("Devices", "These are the devices discovered by the Hardware Wizard. You can click on the name of each device to edit the information relating to that device. Make sure each device has a unique name. The names should match what is in the Op mode code. Scroll down to see more devices if there are any.");
        buildBuilder.setPositiveButton((CharSequence)"Ok", FtcConfigurationActivity.this.a);
        final AlertDialog create = buildBuilder.create();
        create.show();
        ((TextView)create.findViewById(16908299)).setTextSize(14.0f);
    }
}