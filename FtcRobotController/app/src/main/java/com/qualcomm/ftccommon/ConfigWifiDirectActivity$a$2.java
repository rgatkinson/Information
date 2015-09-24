package com.qualcomm.ftccommon;

class ConfigWifiDirectActivity$a$2 implements Runnable {
    @Override
    public void run() {
        ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.a.this.a).dismiss();
        ConfigWifiDirectActivity.a.this.a.finish();
    }
}