package com.qualcomm.robotcore.util;

import android.os.Message;
import android.os.Handler;

class ImmersiveMode$1 extends Handler {
    public void handleMessage(final Message message) {
        ImmersiveMode.this.hideSystemUI();
    }
}