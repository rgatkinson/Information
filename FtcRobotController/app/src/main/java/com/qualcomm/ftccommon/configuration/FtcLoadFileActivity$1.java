package com.qualcomm.ftccommon.configuration;

import android.app.AlertDialog;
import android.app.AlertDialog$Builder;
import android.widget.TextView;
import android.view.View;
import android.view.View$OnClickListener;

class FtcLoadFileActivity$1 implements View$OnClickListener {
    public void onClick(final View view) {
        final AlertDialog$Builder buildBuilder = FtcLoadFileActivity.a(FtcLoadFileActivity.this).buildBuilder("Available files", "These are the files the Hardware Wizard was able to find. You can edit each file by clicking the edit button. The 'Activate' button will set that file as the current configuration file, which will be used to start the robot.");
        buildBuilder.setPositiveButton((CharSequence)"Ok", FtcLoadFileActivity.this.a);
        final AlertDialog create = buildBuilder.create();
        create.show();
        ((TextView)create.findViewById(16908299)).setTextSize(14.0f);
    }
}