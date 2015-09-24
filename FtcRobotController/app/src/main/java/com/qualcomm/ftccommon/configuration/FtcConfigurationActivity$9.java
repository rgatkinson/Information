package com.qualcomm.ftccommon.configuration;

import java.io.IOException;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftccommon.R;
import android.content.DialogInterface;
import android.widget.EditText;
import android.content.DialogInterface$OnClickListener;

class FtcConfigurationActivity$9 implements DialogInterface$OnClickListener {
    final /* synthetic */ EditText a;
    
    public void onClick(final DialogInterface dialogInterface, final int n) {
        final String trim = (this.a.getText().toString() + ".xml").trim();
        if (trim.equals(".xml")) {
            FtcConfigurationActivity.a(FtcConfigurationActivity.this).complainToast("File not saved: Please entire a filename.", FtcConfigurationActivity.k(FtcConfigurationActivity.this));
            return;
        }
        try {
            FtcConfigurationActivity.a(FtcConfigurationActivity.this).writeToFile(trim);
            FtcConfigurationActivity.c(FtcConfigurationActivity.this);
            FtcConfigurationActivity.a(FtcConfigurationActivity.this).saveToPreferences(this.a.getText().toString(), R.string.pref_hardware_config_filename);
            FtcConfigurationActivity.b(FtcConfigurationActivity.this, this.a.getText().toString());
            FtcConfigurationActivity.a(FtcConfigurationActivity.this).updateHeader("robot_config", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
            FtcConfigurationActivity.a(FtcConfigurationActivity.this).confirmSave();
            FtcConfigurationActivity.this.finish();
        }
        catch (RobotCoreException ex) {
            FtcConfigurationActivity.a(FtcConfigurationActivity.this).complainToast(ex.getMessage(), FtcConfigurationActivity.k(FtcConfigurationActivity.this));
            DbgLog.error(ex.getMessage());
        }
        catch (IOException ex2) {
            FtcConfigurationActivity.a(FtcConfigurationActivity.this, ex2.getMessage());
            DbgLog.error(ex2.getMessage());
        }
    }
}