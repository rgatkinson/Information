package com.qualcomm.robotcore.wifi;

import android.net.NetworkInfo;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

private static class a extends BroadcastReceiver
{
    private WifiState a;
    private final WifiAssistantCallback b;
    
    public a(final WifiAssistantCallback b) {
        this.a = null;
        this.b = b;
    }
    
    private void a(final WifiState a) {
        if (this.a != a) {
            this.a = a;
            if (this.b != null) {
                this.b.wifiEventCallback(this.a);
            }
        }
    }
    
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals("android.net.wifi.STATE_CHANGE")) {
            if (!((NetworkInfo)intent.getParcelableExtra("networkInfo")).isConnected()) {
                this.a(WifiState.NOT_CONNECTED);
                return;
            }
            this.a(WifiState.CONNECTED);
        }
    }
}
