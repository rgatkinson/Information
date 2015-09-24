package com.qualcomm.ftccommon;

import java.io.IOException;
import com.qualcomm.robotcore.util.RobotLog;

class ViewLogsActivity$2 implements Runnable {
    @Override
    public void run() {
        try {
            ViewLogsActivity.this.a.setText((CharSequence)ViewLogsActivity.a(ViewLogsActivity.this, ViewLogsActivity.this.readNLines(ViewLogsActivity.this.b)));
        }
        catch (IOException ex) {
            RobotLog.e(ex.toString());
            ViewLogsActivity.this.a.setText((CharSequence)("File not found: " + ViewLogsActivity.this.c));
        }
    }
}