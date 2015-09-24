package com.qualcomm.ftccommon.configuration;

import android.app.AlertDialog;
import android.app.AlertDialog$Builder;
import android.widget.TextView;
import android.view.View;
import android.view.View$OnClickListener;

class FtcLoadFileActivity$2 implements View$OnClickListener {
    public void onClick(final View view) {
        final AlertDialog$Builder buildBuilder = FtcLoadFileActivity.a(FtcLoadFileActivity.this).buildBuilder("AutoConfigure", "This is the fastest way to get a new machine up and running. The AutoConfigure tool will automatically enter some default names for devices. AutoConfigure expects certain devices.  If there are other devices attached, the AutoConfigure tool will not name them.");
        buildBuilder.setPositiveButton((CharSequence)"Ok", FtcLoadFileActivity.this.a);
        final AlertDialog create = buildBuilder.create();
        create.show();
        ((TextView)create.findViewById(16908299)).setTextSize(14.0f);
    }
}