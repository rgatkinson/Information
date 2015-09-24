package com.qualcomm.robotcore.util;

import android.os.Build$VERSION;
import android.os.Message;
import android.os.Handler;
import android.view.View;

public class ImmersiveMode
{
    View a;
    Handler b;
    
    public ImmersiveMode(final View a) {
        this.b = new Handler() {
            public void handleMessage(final Message message) {
                ImmersiveMode.this.hideSystemUI();
            }
        };
        this.a = a;
    }
    
    public static boolean apiOver19() {
        return Build$VERSION.SDK_INT >= 19;
    }
    
    public void cancelSystemUIHide() {
        this.b.removeMessages(0);
    }
    
    public void hideSystemUI() {
        this.a.setSystemUiVisibility(4098);
    }
}
