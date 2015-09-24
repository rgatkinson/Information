package com.qualcomm.robotcore.wifi;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager$ActionListener;
import android.net.wifi.p2p.WifiP2pManager$ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager$PeerListListener;
import com.qualcomm.robotcore.util.RobotLog;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

private class d extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        final String action = intent.getAction();
        if ("android.net.wifi.p2p.STATE_CHANGED".equals(action)) {
            WifiDirectAssistant.a(WifiDirectAssistant.this, intent.getIntExtra("wifi_p2p_state", -1) == 2);
            RobotLog.d("Wifi Direct state - enabled: " + WifiDirectAssistant.i(WifiDirectAssistant.this));
        }
        else {
            if ("android.net.wifi.p2p.PEERS_CHANGED".equals(action)) {
                RobotLog.d("Wifi Direct peers changed");
                WifiDirectAssistant.d(WifiDirectAssistant.this).requestPeers(WifiDirectAssistant.b(WifiDirectAssistant.this), (WifiP2pManager$PeerListListener)WifiDirectAssistant.j(WifiDirectAssistant.this));
                return;
            }
            if ("android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(action)) {
                final NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");
                final WifiP2pInfo wifiP2pInfo = (WifiP2pInfo)intent.getParcelableExtra("wifiP2pInfo");
                RobotLog.d("Wifi Direct connection changed - connected: " + networkInfo.isConnected());
                if (networkInfo.isConnected()) {
                    WifiDirectAssistant.d(WifiDirectAssistant.this).requestConnectionInfo(WifiDirectAssistant.b(WifiDirectAssistant.this), (WifiP2pManager$ConnectionInfoListener)WifiDirectAssistant.k(WifiDirectAssistant.this));
                    WifiDirectAssistant.d(WifiDirectAssistant.this).stopPeerDiscovery(WifiDirectAssistant.b(WifiDirectAssistant.this), (WifiP2pManager$ActionListener)null);
                    return;
                }
                WifiDirectAssistant.a(WifiDirectAssistant.this, ConnectStatus.NOT_CONNECTED);
                if (!WifiDirectAssistant.l(WifiDirectAssistant.this)) {
                    WifiDirectAssistant.this.discoverPeers();
                }
                if (WifiDirectAssistant.this.isConnected()) {
                    WifiDirectAssistant.a(WifiDirectAssistant.this, Event.DISCONNECTED);
                }
                WifiDirectAssistant.b(WifiDirectAssistant.this, wifiP2pInfo.groupFormed);
            }
            else if ("android.net.wifi.p2p.THIS_DEVICE_CHANGED".equals(action)) {
                RobotLog.d("Wifi Direct this device changed");
                WifiDirectAssistant.a(WifiDirectAssistant.this, (WifiP2pDevice)intent.getParcelableExtra("wifiP2pDevice"));
            }
        }
    }
}
