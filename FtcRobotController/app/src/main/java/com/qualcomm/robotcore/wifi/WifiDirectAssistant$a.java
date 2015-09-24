package com.qualcomm.robotcore.wifi;

import com.qualcomm.robotcore.util.RobotLog;
import android.net.wifi.p2p.WifiP2pManager$GroupInfoListener;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager$ConnectionInfoListener;

private class a implements WifiP2pManager$ConnectionInfoListener
{
    public void onConnectionInfoAvailable(final WifiP2pInfo wifiP2pInfo) {
        WifiDirectAssistant.d(WifiDirectAssistant.this).requestGroupInfo(WifiDirectAssistant.b(WifiDirectAssistant.this), (WifiP2pManager$GroupInfoListener)WifiDirectAssistant.c(WifiDirectAssistant.this));
        WifiDirectAssistant.a(WifiDirectAssistant.this, wifiP2pInfo.groupOwnerAddress);
        RobotLog.d("Group owners address: " + WifiDirectAssistant.e(WifiDirectAssistant.this).toString());
        if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
            RobotLog.d("Wifi Direct group formed, this device is the group owner (GO)");
            WifiDirectAssistant.a(WifiDirectAssistant.this, ConnectStatus.GROUP_OWNER);
            WifiDirectAssistant.a(WifiDirectAssistant.this, Event.CONNECTED_AS_GROUP_OWNER);
            return;
        }
        if (wifiP2pInfo.groupFormed) {
            RobotLog.d("Wifi Direct group formed, this device is a client");
            WifiDirectAssistant.a(WifiDirectAssistant.this, ConnectStatus.CONNECTED);
            WifiDirectAssistant.a(WifiDirectAssistant.this, Event.CONNECTED_AS_PEER);
            return;
        }
        RobotLog.d("Wifi Direct group NOT formed, ERROR: " + wifiP2pInfo.toString());
        WifiDirectAssistant.a(WifiDirectAssistant.this, 0);
        WifiDirectAssistant.a(WifiDirectAssistant.this, ConnectStatus.ERROR);
        WifiDirectAssistant.a(WifiDirectAssistant.this, Event.ERROR);
    }
}
