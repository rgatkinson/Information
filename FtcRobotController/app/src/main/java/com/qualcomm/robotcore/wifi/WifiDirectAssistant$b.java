package com.qualcomm.robotcore.wifi;

import android.net.wifi.p2p.WifiP2pDevice;
import com.qualcomm.robotcore.util.RobotLog;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager$GroupInfoListener;

private class b implements WifiP2pManager$GroupInfoListener
{
    public void onGroupInfoAvailable(final WifiP2pGroup wifiP2pGroup) {
        if (wifiP2pGroup == null) {
            return;
        }
        if (wifiP2pGroup.isGroupOwner()) {
            WifiDirectAssistant.a(WifiDirectAssistant.this, WifiDirectAssistant.f(WifiDirectAssistant.this));
            WifiDirectAssistant.b(WifiDirectAssistant.this, WifiDirectAssistant.g(WifiDirectAssistant.this));
        }
        else {
            final WifiP2pDevice owner = wifiP2pGroup.getOwner();
            WifiDirectAssistant.a(WifiDirectAssistant.this, owner.deviceAddress);
            WifiDirectAssistant.b(WifiDirectAssistant.this, owner.deviceName);
        }
        WifiDirectAssistant.c(WifiDirectAssistant.this, wifiP2pGroup.getPassphrase());
        final WifiDirectAssistant a = WifiDirectAssistant.this;
        String h;
        if (WifiDirectAssistant.h(WifiDirectAssistant.this) != null) {
            h = WifiDirectAssistant.h(WifiDirectAssistant.this);
        }
        else {
            h = "";
        }
        WifiDirectAssistant.c(a, h);
        RobotLog.v("Wifi Direct connection information available");
        WifiDirectAssistant.a(WifiDirectAssistant.this, Event.CONNECTION_INFO_AVAILABLE);
    }
}
