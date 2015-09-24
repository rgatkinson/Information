package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.util.RobotLog;

class UpdateUI$Callback$4 implements Runnable {
    final /* synthetic */ String a;
    
    @Override
    public void run() {
        Callback.this.a.textRobotStatus.setText((CharSequence)this.a);
        Callback.this.a.textErrorMessage.setText((CharSequence)RobotLog.getGlobalErrorMsg());
        if (RobotLog.hasGlobalErrorMsg()) {
            Callback.this.a.d.longBright();
        }
    }
}