package com.qualcomm.ftccommon;

class FtcWifiChannelSelectorActivity$1$1 implements Runnable {
    @Override
    public void run() {
        Runnable.this.a.setResult(-1);
        FtcWifiChannelSelectorActivity.a(Runnable.this.a).dismiss();
        Runnable.this.a.finish();
    }
}