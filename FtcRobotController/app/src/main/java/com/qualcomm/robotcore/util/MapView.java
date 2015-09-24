package com.qualcomm.robotcore.util;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.graphics.Matrix;
import java.util.Iterator;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap$Config;
import android.util.AttributeSet;
import android.content.Context;
import java.util.HashMap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class MapView extends View
{
    MapView a;
    private int b;
    private int c;
    private int d;
    private int e;
    private Paint f;
    private Canvas g;
    private Bitmap h;
    private boolean i;
    private boolean j;
    private int k;
    private float l;
    private float m;
    private BitmapDrawable n;
    private int o;
    private int p;
    private int q;
    private boolean r;
    private HashMap<Integer, a> s;
    private Bitmap t;
    
    public MapView(final Context context) {
        super(context);
        this.i = false;
        this.j = false;
        this.k = 1;
        this.r = false;
        this.a();
    }
    
    public MapView(final Context context, final AttributeSet set) {
        super(context, set);
        this.i = false;
        this.j = false;
        this.k = 1;
        this.r = false;
        this.a();
    }
    
    public MapView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.i = false;
        this.j = false;
        this.k = 1;
        this.r = false;
        this.a();
    }
    
    private int a(final int n) {
        if (n % 2 == 0) {
            return n;
        }
        return n + 1;
    }
    
    private void a() {
        (this.f = new Paint()).setColor(-16777216);
        this.f.setStrokeWidth(1.0f);
        this.f.setAntiAlias(true);
        this.a = this;
        this.s = new HashMap<Integer, a>();
    }
    
    private float b(final int n) {
        return n * this.l + this.getWidth() / 2;
    }
    
    private void b() {
        this.h = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap$Config.ARGB_8888);
        this.g = new Canvas(this.h);
        final Paint paint = new Paint();
        paint.setColor(-1);
        paint.setAntiAlias(true);
        this.g.drawRect(0.0f, 0.0f, (float)this.g.getWidth(), (float)this.g.getHeight(), paint);
        for (int i = 0; i < this.c; i += this.e) {
            final float n = i * this.m;
            this.g.drawLine(0.0f, n, (float)this.g.getWidth(), n, this.f);
        }
        for (int j = 0; j < this.b; j += this.d) {
            final float n2 = j * this.l;
            this.g.drawLine(n2, 0.0f, n2, (float)this.g.getHeight(), this.f);
        }
    }
    
    private float c(final int n) {
        return this.getHeight() / 2 - n * this.m;
    }
    
    private void c() {
        for (final a a : this.s.values()) {
            final float b = this.b(a.b);
            final float c = this.c(a.c);
            if (a.e) {
                final Paint paint = new Paint();
                paint.setColor(a.d);
                this.g.drawCircle(b, c, 5.0f, paint);
            }
            else {
                final Bitmap decodeResource = BitmapFactory.decodeResource(this.getResources(), a.d);
                this.g.drawBitmap(decodeResource, b - decodeResource.getWidth() / 2, c - decodeResource.getHeight() / 2, new Paint());
            }
        }
    }
    
    private int d(final int n) {
        return 360 - n;
    }
    
    private void d() {
        final float b = this.b(this.o);
        final float c = this.c(this.p);
        final int d = this.d(this.q);
        final Matrix matrix = new Matrix();
        matrix.postRotate((float)d);
        matrix.postScale(0.2f, 0.2f);
        final Bitmap t = this.t;
        final Bitmap bitmap = Bitmap.createBitmap(t, 0, 0, t.getWidth(), t.getHeight(), matrix, true);
        this.g.drawBitmap(bitmap, b - bitmap.getWidth() / 2, c - bitmap.getHeight() / 2, new Paint());
    }
    
    public int addDrawable(final int n, final int n2, final int n3) {
        final int n4 = this.k++;
        this.s.put(n4, new a(n4, -n, n2, n3, false));
        return n4;
    }
    
    public int addMarker(final int n, final int n2, final int n3) {
        final int n4 = this.k++;
        this.s.put(n4, new a(n4, -n, n2, n3, true));
        return n4;
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        this.l = this.getWidth() / this.b;
        this.m = this.getHeight() / this.c;
        this.j = true;
        this.redraw();
        Log.e("MapView", "Size changed");
    }
    
    public void redraw() {
        if (this.i && this.j) {
            this.b();
            this.c();
            if (this.r) {
                this.d();
            }
        }
        this.n = new BitmapDrawable(this.getResources(), this.h);
        this.a.setBackgroundDrawable((Drawable)this.n);
    }
    
    public boolean removeMarker(final int n) {
        return this.s.remove(n) != null;
    }
    
    public void setRobotLocation(final int n, final int p3, final int q) {
        this.o = -n;
        this.p = p3;
        this.q = q;
        this.r = true;
    }
    
    public void setup(final int n, final int n2, final int n3, final int n4, final Bitmap t) {
        this.b = n * 2;
        this.c = n2 * 2;
        this.d = this.b / this.a(n3);
        this.e = this.c / this.a(n4);
        this.t = t;
        this.i = true;
    }
    
    private class a
    {
        public int a;
        public int b;
        public int c;
        public int d;
        public boolean e;
        
        public a(final int a, final int b, final int c, final int d, final boolean e) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
        }
    }
}
