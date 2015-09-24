package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.wifi.WifiDirectAssistant;

static class UpdateUI$3 {
    static {
        a = new int[WifiDirectAssistant.Event.values().length];
        while (true) {
            try {
                UpdateUI$3.a[WifiDirectAssistant.Event.DISCONNECTED.ordinal()] = 1;
                try {
                    UpdateUI$3.a[WifiDirectAssistant.Event.CONNECTED_AS_GROUP_OWNER.ordinal()] = 2;
                    try {
                        UpdateUI$3.a[WifiDirectAssistant.Event.ERROR.ordinal()] = 3;
                        try {
                            UpdateUI$3.a[WifiDirectAssistant.Event.CONNECTION_INFO_AVAILABLE.ordinal()] = 4;
                        }
                        catch (NoSuchFieldError noSuchFieldError) {}
                    }
                    catch (NoSuchFieldError noSuchFieldError2) {}
                }
                catch (NoSuchFieldError noSuchFieldError3) {}
            }
            catch (NoSuchFieldError noSuchFieldError4) {
                continue;
            }
            break;
        }
    }
}