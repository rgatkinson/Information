package com.qualcomm.robotcore.util;

import android.widget.TextView;

static final class Util$2 implements Runnable {
    final /* synthetic */ TextView a;
    final /* synthetic */ String b;
    
    @Override
    public void run() {
        this.a.setText((CharSequence)this.b);
    }
}