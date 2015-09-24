package com.ftdi.j2xx;

import android.hardware.usb.UsbDevice;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

class D2xxManager$1 extends BroadcastReceiver {
    public void onReceive(final Context context, final Intent intent) {
        final String action = intent.getAction();
        Label_0097: {
            if (!"android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                break Label_0097;
            }
            final UsbDevice usbDevice = (UsbDevice)intent.getParcelableExtra("device");
            FT_Device ft_Device = D2xxManager.a(D2xxManager.this, usbDevice);
            while (ft_Device != null) {
                ft_Device.close();
                synchronized (D2xxManager.a(D2xxManager.this)) {
                    D2xxManager.a(D2xxManager.this).remove(ft_Device);
                    // monitorexit(D2xxManager.a(this.a))
                    ft_Device = D2xxManager.a(D2xxManager.this, usbDevice);
                    continue;
                }
                break Label_0097;
            }
            return;
        }
        if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
            D2xxManager.this.addUsbDevice((UsbDevice)intent.getParcelableExtra("device"));
        }
    }
}