package com.qualcomm.robotcore.util;

import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.view.View;

public class ImmersiveMode {
   View a;
   Handler b = new Handler() {
      public void handleMessage(Message var1) {
         ImmersiveMode.this.hideSystemUI();
      }
   };

   public ImmersiveMode(View var1) {
      this.a = var1;
   }

   public static boolean apiOver19() {
      return VERSION.SDK_INT >= 19;
   }

   public void cancelSystemUIHide() {
      this.b.removeMessages(0);
   }

   public void hideSystemUI() {
      this.a.setSystemUiVisibility(4098);
   }
}
