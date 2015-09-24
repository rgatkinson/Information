package com.qualcomm.robotcore.wifi;

import com.qualcomm.robotcore.util.RobotLog;
import android.net.wifi.p2p.WifiP2pManager$ActionListener;

class WifiDirectAssistant$3 implements WifiP2pManager$ActionListener {
    public void onFailure(final int n) {
        final String failureReasonToString = WifiDirectAssistant.failureReasonToString(n);
        WifiDirectAssistant.a(WifiDirectAssistant.this, n);
        RobotLog.d("WifiDirect connect cannot start - reason: " + failureReasonToString);
        WifiDirectAssistant.a(WifiDirectAssistant.this, Event.ERROR);
    }
    
    public void onSuccess() {
        RobotLog.d("WifiDirect connect started");
        WifiDirectAssistant.a(WifiDirectAssistant.this, Event.CONNECTING);
    }
}