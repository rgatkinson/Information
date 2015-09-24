package com.qualcomm.ftccommon;

import android.content.Context;
import android.widget.Toast;

class UpdateUI$Callback$1 implements Runnable {
    @Override
    public void run() {
        Toast.makeText((Context)Callback.this.a.c, (CharSequence)"Restarting Robot", 0).show();
    }
}