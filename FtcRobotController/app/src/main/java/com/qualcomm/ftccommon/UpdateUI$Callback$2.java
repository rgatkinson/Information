package com.qualcomm.ftccommon;

class UpdateUI$Callback$2 extends Thread {
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1500L);
                Callback.this.a.c.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        UpdateUI.a(Callback.this.a);
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