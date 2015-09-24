package com.ftdi.j2xx;

import android.util.Log;
import android.hardware.usb.UsbDevice;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

class D2xxManager$2 extends BroadcastReceiver {
    public void onReceive(final Context context, final Intent intent) {
        if ("com.ftdi.j2xx".equals(intent.getAction())) {
            synchronized (this) {
                final UsbDevice usbDevice = (UsbDevice)intent.getParcelableExtra("device");
                if (!intent.getBooleanExtra("permission", false)) {
                    Log.d("D2xx::", "permission denied for device " + usbDevice);
                }
            }
        }
    }
}