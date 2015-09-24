package com.qualcomm.robotcore.wifi;

import com.qualcomm.robotcore.util.RobotLog;
import android.net.wifi.p2p.WifiP2pManager$ActionListener;

class WifiDirectAssistant$2 implements WifiP2pManager$ActionListener {
    public void onFailure(final int n) {
        if (n == 2) {
            RobotLog.d("Wifi Direct cannot create group, does group already exist?");
            return;
        }
        final String failureReasonToString = WifiDirectAssistant.failureReasonToString(n);
        WifiDirectAssistant.a(WifiDirectAssistant.this, n);
        RobotLog.w("Wifi Direct failure while trying to create group - reason: " + failureReasonToString);
        WifiDirectAssistant.a(WifiDirectAssistant.this, ConnectStatus.ERROR);
        WifiDirectAssistant.a(WifiDirectAssistant.this, Event.ERROR);
    }
    
    public void onSuccess() {
        WifiDirectAssistant.a(WifiDirectAssistant.this, ConnectStatus.GROUP_OWNER);
        WifiDirectAssistant.a(WifiDirectAssistant.this, Event.GROUP_CREATED);
        RobotLog.d("Wifi Direct created group");
    }
}