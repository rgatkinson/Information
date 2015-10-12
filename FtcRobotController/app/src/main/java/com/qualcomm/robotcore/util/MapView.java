package com.qualcomm.robotcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.util.HashMap;
import java.util.Iterator;

public class MapView extends View {
   MapView a;
   private int b;
   private int c;
   private int d;
   private int e;
   private Paint f;
   private Canvas g;
   private Bitmap h;
   private boolean i = false;
   private boolean j = false;
   private int k = 1;
   private float l;
   private float m;
   private BitmapDrawable n;
   private int o;
   private int p;
   private int q;
   private boolean r = false;
   private HashMap<Integer, MapView.a> s;
   private Bitmap t;

   public MapView(Context var1) {
      super(var1);
      this.a();
   }

   public MapView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.a();
   }

   public MapView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.a();
   }

   private int a(int var1) {
      return var1 % 2 == 0?var1:var1 + 1;
   }

   private void a() {
      this.f = new Paint();
      this.f.setColor(-16777216);
      this.f.setStrokeWidth(1.0F);
      this.f.setAntiAlias(true);
      this.a = this;
      this.s = new HashMap();
   }

   private float b(int var1) {
      return (float)var1 * this.l + (float)(this.getWidth() / 2);
   }

   private void b() {
      this.h = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Config.ARGB_8888);
      this.g = new Canvas(this.h);
      Paint var1 = new Paint();
      var1.setColor(-1);
      var1.setAntiAlias(true);
      this.g.drawRect(0.0F, 0.0F, (float)this.g.getWidth(), (float)this.g.getHeight(), var1);

      for(int var2 = 0; var2 < this.c; var2 += this.e) {
         float var5 = (float)var2 * this.m;
         this.g.drawLine(0.0F, var5, (float)this.g.getWidth(), var5, this.f);
      }

      for(int var3 = 0; var3 < this.b; var3 += this.d) {
         float var4 = (float)var3 * this.l;
         this.g.drawLine(var4, 0.0F, var4, (float)this.g.getHeight(), this.f);
      }

   }

   private float c(int var1) {
      float var2 = (float)var1 * this.m;
      return (float)(this.getHeight() / 2) - var2;
   }

   private void c() {
      Iterator var1 = this.s.values().iterator();

      while(var1.hasNext()) {
         MapView.a var2 = (MapView.a)var1.next();
         float var3 = this.b(var2.b);
         float var4 = this.c(var2.c);
         if(var2.e) {
            Paint var5 = new Paint();
            var5.setColor(var2.d);
            this.g.drawCircle(var3, var4, 5.0F, var5);
         } else {
            Bitmap var6 = BitmapFactory.decodeResource(this.getResources(), var2.d);
            float var7 = var3 - (float)(var6.getWidth() / 2);
            float var8 = var4 - (float)(var6.getHeight() / 2);
            this.g.drawBitmap(var6, var7, var8, new Paint());
         }
      }

   }

   private int d(int var1) {
      return 360 - var1;
   }

   private void d() {
      float var1 = this.b(this.o);
      float var2 = this.c(this.p);
      int var3 = this.d(this.q);
      Matrix var4 = new Matrix();
      var4.postRotate((float)var3);
      var4.postScale(0.2F, 0.2F);
      Bitmap var7 = this.t;
      Bitmap var8 = Bitmap.createBitmap(var7, 0, 0, var7.getWidth(), var7.getHeight(), var4, true);
      float var9 = var1 - (float)(var8.getWidth() / 2);
      float var10 = var2 - (float)(var8.getHeight() / 2);
      this.g.drawBitmap(var8, var9, var10, new Paint());
   }

   public int addDrawable(int var1, int var2, int var3) {
      int var4 = this.k;
      this.k = var4 + 1;
      MapView.a var5 = new MapView.a(var4, -var1, var2, var3, false);
      this.s.put(Integer.valueOf(var4), var5);
      return var4;
   }

   public int addMarker(int var1, int var2, int var3) {
      int var4 = this.k;
      this.k = var4 + 1;
      MapView.a var5 = new MapView.a(var4, -var1, var2, var3, true);
      this.s.put(Integer.valueOf(var4), var5);
      return var4;
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      this.l = (float)this.getWidth() / (float)this.b;
      this.m = (float)this.getHeight() / (float)this.c;
      this.j = true;
      this.redraw();
      Log.e("MapView", "Size changed");
   }

   public void redraw() {
      if(this.i && this.j) {
         this.b();
         this.c();
         if(this.r) {
            this.d();
         }
      }

      this.n = new BitmapDrawable(this.getResources(), this.h);
      this.a.setBackgroundDrawable(this.n);
   }

   public boolean removeMarker(int var1) {
      return this.s.remove(Integer.valueOf(var1)) != null;
   }

   public void setRobotLocation(int var1, int var2, int var3) {
      this.o = -var1;
      this.p = var2;
      this.q = var3;
      this.r = true;
   }

   public void setup(int var1, int var2, int var3, int var4, Bitmap var5) {
      this.b = var1 * 2;
      this.c = var2 * 2;
      this.d = this.b / this.a(var3);
      this.e = this.c / this.a(var4);
      this.t = var5;
      this.i = true;
   }

   private class a {
      public int a;
      public int b;
      public int c;
      public int d;
      public boolean e;

      public a(int var2, int var3, int var4, int var5, boolean var6) {
         this.a = var2;
         this.b = var3;
         this.c = var4;
         this.d = var5;
         this.e = var6;
      }
   }
}
