package com.qualcomm.robotcore.wifi;

import com.qualcomm.robotcore.util.RobotLog;
import android.net.wifi.p2p.WifiP2pManager$ActionListener;

class WifiDirectAssistant$1 implements WifiP2pManager$ActionListener {
    public void onFailure(final int n) {
        final String failureReasonToString = WifiDirectAssistant.failureReasonToString(n);
        WifiDirectAssistant.a(WifiDirectAssistant.this, n);
        RobotLog.w("Wifi Direct failure while trying to discover peers - reason: " + failureReasonToString);
        WifiDirectAssistant.a(WifiDirectAssistant.this, Event.ERROR);
    }
    
    public void onSuccess() {
        WifiDirectAssistant.a(WifiDirectAssistant.this, Event.DISCOVERING_PEERS);
        RobotLog.d("Wifi Direct discovering peers");
    }
}