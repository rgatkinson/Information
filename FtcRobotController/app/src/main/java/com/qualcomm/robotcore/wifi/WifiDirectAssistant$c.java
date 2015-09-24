package com.qualcomm.robotcore.wifi;

import java.util.Iterator;
import android.net.wifi.p2p.WifiP2pDevice;
import com.qualcomm.robotcore.util.RobotLog;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager$PeerListListener;

private class c implements WifiP2pManager$PeerListListener
{
    public void onPeersAvailable(final WifiP2pDeviceList list) {
        WifiDirectAssistant.a(WifiDirectAssistant.this).clear();
        WifiDirectAssistant.a(WifiDirectAssistant.this).addAll(list.getDeviceList());
        RobotLog.v("Wifi Direct peers found: " + WifiDirectAssistant.a(WifiDirectAssistant.this).size());
        for (final WifiP2pDevice wifiP2pDevice : WifiDirectAssistant.a(WifiDirectAssistant.this)) {
            RobotLog.v("    peer: " + wifiP2pDevice.deviceAddress + " " + wifiP2pDevice.deviceName);
        }
        WifiDirectAssistant.a(WifiDirectAssistant.this, Event.PEERS_AVAILABLE);
    }
}
