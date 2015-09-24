package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.hardware.Gamepad;

class UpdateUI$Callback$3 implements Runnable {
    final /* synthetic */ Gamepad[] a;
    final /* synthetic */ String b;
    
    @Override
    public void run() {
        for (int n = 0; n < Callback.this.a.textGamepad.length && n < this.a.length; ++n) {
            if (this.a[n].id == -1) {
                Callback.this.a.textGamepad[n].setText((CharSequence)" ");
            }
            else {
                Callback.this.a.textGamepad[n].setText((CharSequence)this.a[n].toString());
            }
        }
        Callback.this.a.textOpMode.setText((CharSequence)("Op Mode: " + this.b));
        Callback.this.a.textErrorMessage.setText((CharSequence)RobotLog.getGlobalErrorMsg());
    }
}