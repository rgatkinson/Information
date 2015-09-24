package com.qualcomm.robotcore.util;

import android.app.Activity;
import android.os.Handler;
import android.view.WindowManager.LayoutParams;

public class Dimmer {
   public static final int DEFAULT_DIM_TIME = 30000;
   public static final int LONG_BRIGHT_TIME = 60000;
   public static final float MAXIMUM_BRIGHTNESS = 1.0F;
   public static final float MINIMUM_BRIGHTNESS = 0.05F;
   Handler a;
   Activity b;
   final LayoutParams c;
   long d;
   float e;

   public Dimmer(long var1, Activity var3) {
      this.a = new Handler();
      this.e = 1.0F;
      this.d = var1;
      this.b = var3;
      this.c = var3.getWindow().getAttributes();
      this.e = this.c.screenBrightness;
   }

   public Dimmer(Activity var1) {
      this(30000L, var1);
   }

   private float a() {
      float var1 = 0.05F * this.e;
      return var1 < 0.05F?0.05F:var1;
   }

   private void a(float var1) {
      this.c.screenBrightness = var1;
      this.b.runOnUiThread(new Runnable() {
         public void run() {
            Dimmer.this.b.getWindow().setAttributes(Dimmer.this.c);
         }
      });
   }

   public void handleDimTimer() {
      this.a(this.e);
      this.a.removeCallbacks((Runnable)null);
      this.a.postDelayed(new Runnable() {
         public void run() {
            Dimmer.this.a(Dimmer.this.a());
         }
      }, this.d);
   }

   public void longBright() {
      this.a(this.e);
      Runnable var1 = new Runnable() {
         public void run() {
            Dimmer.this.a(Dimmer.this.a());
         }
      };
      this.a.removeCallbacksAndMessages((Object)null);
      this.a.postDelayed(var1, 60000L);
   }
}
