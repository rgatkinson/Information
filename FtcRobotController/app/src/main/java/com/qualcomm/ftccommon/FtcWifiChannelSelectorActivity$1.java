package com.qualcomm.ftccommon;

class FtcWifiChannelSelectorActivity$1 implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000L);
                FtcWifiChannelSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        FtcWifiChannelSelectorActivity.this.setResult(-1);
                        FtcWifiChannelSelectorActivity.a(FtcWifiChannelSelectorActivity.this).dismiss();
                        FtcWifiChannelSelectorActivity.this.finish();
                    }
                });
            }
            catch (InterruptedException ex) {
                continue;
            }
            break;
        }
    }
}