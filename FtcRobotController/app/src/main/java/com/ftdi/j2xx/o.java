package com.ftdi.j2xx;

import java.util.concurrent.TimeUnit;
import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;
import java.io.IOException;
import android.util.Log;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.nio.channels.Pipe;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;

class o
{
    private Semaphore[] a;
    private Semaphore[] b;
    private n[] c;
    private ByteBuffer d;
    private ByteBuffer[] e;
    private Pipe f;
    private Pipe.SinkChannel g;
    private Pipe.SourceChannel h;
    private int i;
    private int j;
    private Object k;
    private FT_Device l;
    private D2xxManager.DriverParameters m;
    private Lock n;
    private Condition o;
    private boolean p;
    private Lock q;
    private Condition r;
    private Object s;
    private int t;
    
    public o(final FT_Device l) {
        while (true) {
            int n = 0;
            this.l = l;
            this.m = this.l.d();
            this.i = this.m.getBufferNumber();
            final int maxBufferSize = this.m.getMaxBufferSize();
            this.t = this.l.e();
            this.a = new Semaphore[this.i];
            this.b = new Semaphore[this.i];
            this.c = new n[this.i];
            this.e = new ByteBuffer[256];
            this.n = new ReentrantLock();
            this.o = this.n.newCondition();
            this.p = false;
            this.q = new ReentrantLock();
            this.r = this.q.newCondition();
            this.k = new Object();
            this.s = new Object();
            this.h();
            this.d = ByteBuffer.allocateDirect(maxBufferSize);
        Label_0288_Outer:
            while (true) {
                try {
                    this.f = Pipe.open();
                    this.g = this.f.sink();
                    this.h = this.f.source();
                    if (n >= this.i) {
                        return;
                    }
                }
                catch (IOException ex) {
                    Log.d("ProcessInCtrl", "Create mMainPipe failed!");
                    ex.printStackTrace();
                    n = 0;
                    continue;
                }
                this.c[n] = new n(maxBufferSize);
                this.b[n] = new Semaphore(1);
                this.a[n] = new Semaphore(1);
                while (true) {
                    try {
                        this.c(n);
                        ++n;
                        continue Label_0288_Outer;
                    }
                    catch (Exception ex2) {
                        Log.d("ProcessInCtrl", "Acquire read buffer " + n + " failed!");
                        ex2.printStackTrace();
                        continue;
                    }
                    break;
                }
                break;
            }
        }
    }
    
    private void b(final n n) throws InterruptedException {
        int n2 = 1;
        final ByteBuffer a = n.a();
        final int b = n.b();
        if (b <= 0) {
            return;
        }
        final int n3 = b / this.t;
    Label_0164:
        while (true) {
            Label_0180: {
                if (b % this.t <= 0) {
                    break Label_0180;
                }
                int n4 = n2;
            Label_0059_Outer:
                while (true) {
                    final int n5 = n3 + n4;
                    int n6 = 0;
                    short n7 = 0;
                    short n8 = 0;
                    int n9 = 0;
                Label_0322_Outer:
                    while (true) {
                        Label_0186: {
                            if (n6 < n5) {
                                break Label_0186;
                            }
                            if (n9 == 0) {
                                break Label_0180;
                            }
                            try {
                                final long write = this.g.write(this.e, 0, n5);
                                if (write != n9) {
                                    Log.d("extractReadData::", "written != totalData, written= " + write + " totalData=" + n9);
                                }
                                this.f((int)write);
                                this.q.lock();
                                this.r.signalAll();
                                this.q.unlock();
                                a.clear();
                                this.a(n2 != 0, n8, n7);
                                return;
                                n4 = 0;
                                continue Label_0059_Outer;
                                // iftrue(Label_0367:, n6 != n5 - 1)
                                a.limit(b);
                                final int n10 = n6 * this.t;
                                a.position(n10);
                                final byte value = a.get();
                                n8 = (short)(this.l.g.modemStatus ^ (short)(value & 0xF0));
                                this.l.g.modemStatus = (short)(value & 0xF0);
                                this.l.g.lineStatus = (short)(a.get() & 0xFF);
                                final int n11 = n10 + 2;
                                // iftrue(Label_0353:, !a.hasRemaining())
                                n7 = (short)(0x1E & this.l.g.lineStatus);
                                int n12 = n11;
                                int n13 = b;
                                while (true) {
                                    break Label_0322;
                                    Label_0353: {
                                        n12 = n11;
                                    }
                                    n13 = b;
                                    n7 = 0;
                                    final int n14 = n9 + (n13 - n12);
                                    this.e[n6] = a.slice();
                                    ++n6;
                                    n9 = n14;
                                    continue Label_0322_Outer;
                                    Label_0367:
                                    n13 = (n6 + 1) * this.t;
                                    a.limit(n13);
                                    n12 = 2 + n6 * this.t;
                                    a.position(n12);
                                    continue;
                                }
                            }
                            catch (Exception ex) {
                                Log.d("extractReadData::", "Write data to sink failed!!");
                                ex.printStackTrace();
                                continue Label_0164;
                            }
                        }
                        break;
                    }
                    break;
                }
            }
            n2 = 0;
            continue Label_0164;
        }
    }
    
    private int f(final int n) {
        synchronized (this.k) {
            return this.j += n;
        }
    }
    
    private int g(final int n) {
        synchronized (this.k) {
            return this.j -= n;
        }
    }
    
    private void h() {
        synchronized (this.k) {
            this.j = 0;
        }
    }
    
    public int a(final boolean b, final short n, final short n2) throws InterruptedException {
        final q q = new q();
        q.a = this.l.i.a;
        if (b && (0x1L & q.a) != 0x0L && (0x1L ^ this.l.a) == 0x1L) {
            final FT_Device l = this.l;
            l.a |= 0x1L;
            final Intent intent = new Intent("FT_EVENT_RXCHAR");
            intent.putExtra("message", "FT_EVENT_RXCHAR");
            LocalBroadcastManager.getInstance(this.l.j).sendBroadcast(intent);
        }
        if (n != 0 && (0x2L & q.a) != 0x0L && (0x2L ^ this.l.a) == 0x2L) {
            final FT_Device i = this.l;
            i.a |= 0x2L;
            final Intent intent2 = new Intent("FT_EVENT_MODEM_STATUS");
            intent2.putExtra("message", "FT_EVENT_MODEM_STATUS");
            LocalBroadcastManager.getInstance(this.l.j).sendBroadcast(intent2);
        }
        if (n2 != 0 && (0x4L & q.a) != 0x0L && (0x4L ^ this.l.a) == 0x4L) {
            final FT_Device j = this.l;
            j.a |= 0x4L;
            final Intent intent3 = new Intent("FT_EVENT_LINE_STATUS");
            intent3.putExtra("message", "FT_EVENT_LINE_STATUS");
            LocalBroadcastManager.getInstance(this.l.j).sendBroadcast(intent3);
        }
        return 0;
    }
    
    public int a(final byte[] array, final int n, long n2) {
        this.m.getMaxBufferSize();
        final long currentTimeMillis = System.currentTimeMillis();
        final ByteBuffer wrap = ByteBuffer.wrap(array, 0, n);
        if (n2 == 0L) {
            n2 = this.m.getReadTimeout();
        }
    Label_0220_Outer:
        while (this.l.isOpen()) {
            if (this.c() >= n) {
                synchronized (this.h) {
                    while (true) {
                        try {
                            this.h.read(wrap);
                            this.g(n);
                            // monitorexit(this.h)
                            synchronized (this.s) {
                                if (this.p) {
                                    Log.i("FTDI debug::", "buffer is full , and also re start buffer");
                                    this.n.lock();
                                    this.o.signalAll();
                                    this.p = false;
                                    this.n.unlock();
                                }
                                return n;
                            }
                        }
                        catch (Exception ex) {
                            Log.d("readBulkInData::", "Cannot read data from Source!!");
                            ex.printStackTrace();
                            continue;
                        }
                        break;
                    }
                }
            }
            while (true) {
                try {
                    this.q.lock();
                    this.r.await(System.currentTimeMillis() - currentTimeMillis, TimeUnit.MILLISECONDS);
                    this.q.unlock();
                    if (System.currentTimeMillis() - currentTimeMillis >= n2) {
                        return 0;
                    }
                    continue Label_0220_Outer;
                }
                catch (InterruptedException ex2) {
                    Log.d("readBulkInData::", "Cannot wait to read data!!");
                    ex2.printStackTrace();
                    this.q.unlock();
                    continue;
                }
                break;
            }
        }
        return 0;
    }
    
    n a(final int n) {
        final n[] c = this.c;
        // monitorenter(c)
        n n2 = null;
        if (n < 0) {
            return n2;
        }
        try {
            final int i = this.i;
            n2 = null;
            if (n < i) {
                n2 = this.c[n];
            }
            return n2;
        }
        finally {
        }
        // monitorexit(c)
    }
    
    public void a(final n p0) throws D2xxManager.D2xxException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1        
        //     1: invokevirtual   com/ftdi/j2xx/n.b:()I
        //     4: istore          6
        //     6: iload           6
        //     8: iconst_2       
        //     9: if_icmpge       21
        //    12: aload_1        
        //    13: invokevirtual   com/ftdi/j2xx/n.a:()Ljava/nio/ByteBuffer;
        //    16: invokevirtual   java/nio/ByteBuffer.clear:()Ljava/nio/Buffer;
        //    19: pop            
        //    20: return         
        //    21: aload_0        
        //    22: getfield        com/ftdi/j2xx/o.s:Ljava/lang/Object;
        //    25: astore          8
        //    27: aload           8
        //    29: monitorenter   
        //    30: aload_0        
        //    31: invokevirtual   com/ftdi/j2xx/o.d:()I
        //    34: istore          10
        //    36: iload           6
        //    38: iconst_2       
        //    39: isub           
        //    40: istore          11
        //    42: iload           10
        //    44: iload           11
        //    46: if_icmpge       81
        //    49: ldc_w           "ProcessBulkIn::"
        //    52: ldc_w           " Buffer is full, waiting for read...."
        //    55: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    58: pop            
        //    59: aload_0        
        //    60: iconst_0       
        //    61: iconst_0       
        //    62: iconst_0       
        //    63: invokevirtual   com/ftdi/j2xx/o.a:(ZSS)I
        //    66: pop            
        //    67: aload_0        
        //    68: getfield        com/ftdi/j2xx/o.n:Ljava/util/concurrent/locks/Lock;
        //    71: invokeinterface java/util/concurrent/locks/Lock.lock:()V
        //    76: aload_0        
        //    77: iconst_1       
        //    78: putfield        com/ftdi/j2xx/o.p:Z
        //    81: aload           8
        //    83: monitorexit    
        //    84: iload           10
        //    86: iload           11
        //    88: if_icmpge       109
        //    91: aload_0        
        //    92: getfield        com/ftdi/j2xx/o.o:Ljava/util/concurrent/locks/Condition;
        //    95: invokeinterface java/util/concurrent/locks/Condition.await:()V
        //   100: aload_0        
        //   101: getfield        com/ftdi/j2xx/o.n:Ljava/util/concurrent/locks/Lock;
        //   104: invokeinterface java/util/concurrent/locks/Lock.unlock:()V
        //   109: aload_0        
        //   110: aload_1        
        //   111: invokespecial   com/ftdi/j2xx/o.b:(Lcom/ftdi/j2xx/n;)V
        //   114: return         
        //   115: astore          4
        //   117: aload_0        
        //   118: getfield        com/ftdi/j2xx/o.n:Ljava/util/concurrent/locks/Lock;
        //   121: invokeinterface java/util/concurrent/locks/Lock.unlock:()V
        //   126: ldc             "ProcessInCtrl"
        //   128: ldc_w           "Exception in Full await!"
        //   131: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   134: pop            
        //   135: aload           4
        //   137: invokevirtual   java/lang/InterruptedException.printStackTrace:()V
        //   140: return         
        //   141: astore          9
        //   143: aload           8
        //   145: monitorexit    
        //   146: aload           9
        //   148: athrow         
        //   149: astore_2       
        //   150: ldc             "ProcessInCtrl"
        //   152: ldc_w           "Exception in ProcessBulkIN"
        //   155: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   158: pop            
        //   159: aload_2        
        //   160: invokevirtual   java/lang/Exception.printStackTrace:()V
        //   163: new             Lcom/ftdi/j2xx/D2xxManager$D2xxException;
        //   166: dup            
        //   167: ldc_w           "Fatal error in BulkIn."
        //   170: invokespecial   com/ftdi/j2xx/D2xxManager$D2xxException.<init>:(Ljava/lang/String;)V
        //   173: athrow         
        //    Exceptions:
        //  throws com.ftdi.j2xx.D2xxManager.D2xxException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  0      6      115    141    Ljava/lang/InterruptedException;
        //  0      6      149    174    Ljava/lang/Exception;
        //  12     20     115    141    Ljava/lang/InterruptedException;
        //  12     20     149    174    Ljava/lang/Exception;
        //  21     30     115    141    Ljava/lang/InterruptedException;
        //  21     30     149    174    Ljava/lang/Exception;
        //  30     36     141    149    Any
        //  49     81     141    149    Any
        //  81     84     141    149    Any
        //  91     109    115    141    Ljava/lang/InterruptedException;
        //  91     109    149    174    Ljava/lang/Exception;
        //  109    114    115    141    Ljava/lang/InterruptedException;
        //  109    114    149    174    Ljava/lang/Exception;
        //  143    146    141    149    Any
        //  146    149    115    141    Ljava/lang/InterruptedException;
        //  146    149    149    174    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0081:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.doSaveJarDecompiled(ProcyonDecompiler.java:194)
        //     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.decompileToZip(ProcyonDecompiler.java:146)
        //     at the.bytecode.club.bytecodeviewer.gui.MainViewerGUI$18$1$2.run(MainViewerGUI.java:1093)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    boolean a() {
        return this.p;
    }
    
    D2xxManager.DriverParameters b() {
        return this.m;
    }
    
    n b(final int n) throws InterruptedException {
        this.a[n].acquire();
        n a = this.a(n);
        if (a.c(n) == null) {
            a = null;
        }
        return a;
    }
    
    public int c() {
        synchronized (this.k) {
            return this.j;
        }
    }
    
    n c(final int n) throws InterruptedException {
        this.b[n].acquire();
        return this.a(n);
    }
    
    public int d() {
        return -1 + (this.m.getMaxBufferSize() - this.c());
    }
    
    public void d(final int n) throws InterruptedException {
        synchronized (this.c) {
            this.c[n].d(n);
            // monitorexit(this.c)
            this.a[n].release();
        }
    }
    
    public int e() {
        final int bufferNumber = this.m.getBufferNumber();
        // monitorenter(this.d)
    Label_0057_Outer:
        while (true) {
            while (true) {
                int n;
                while (true) {
                    try {
                        int i;
                        do {
                            this.h.configureBlocking(false);
                            i = this.h.read(this.d);
                            this.d.clear();
                        } while (i != 0);
                        this.h();
                        n = 0;
                        if (n >= bufferNumber) {
                            return 0;
                        }
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        continue Label_0057_Outer;
                    }
                    finally {
                    }
                    // monitorexit(this.d)
                    break;
                }
                final n a = this.a(n);
                if (a.d() && a.b() > 2) {
                    a.c();
                }
                ++n;
                continue;
            }
        }
    }
    
    public void e(final int n) throws InterruptedException {
        this.b[n].release();
    }
    
    public void f() throws InterruptedException {
        for (int bufferNumber = this.m.getBufferNumber(), i = 0; i < bufferNumber; ++i) {
            if (this.a(i).d()) {
                this.d(i);
            }
        }
    }
    
    void g() {
        int n = 0;
    Label_0014_Outer:
        while (true) {
            final int i = this.i;
            int n2 = 0;
            Label_0172: {
                if (n < i) {
                    break Label_0172;
                }
            Label_0161_Outer:
                while (true) {
                    Label_0241: {
                        if (n2 < 256) {
                            break Label_0241;
                        }
                        this.a = null;
                        this.b = null;
                        this.c = null;
                        this.e = null;
                        this.d = null;
                        if (this.p) {
                            this.n.lock();
                            this.o.signalAll();
                            this.n.unlock();
                        }
                        this.q.lock();
                        this.r.signalAll();
                        this.q.unlock();
                        this.n = null;
                        this.o = null;
                        this.k = null;
                        this.q = null;
                        this.r = null;
                        while (true) {
                            try {
                                this.g.close();
                                this.g = null;
                                this.h.close();
                                this.h = null;
                                this.f = null;
                                this.l = null;
                                this.m = null;
                                return;
                                while (true) {
                                    try {
                                        this.e(n);
                                        this.c[n] = null;
                                        this.b[n] = null;
                                        this.a[n] = null;
                                        ++n;
                                        continue Label_0014_Outer;
                                    }
                                    catch (Exception ex) {
                                        Log.d("ProcessInCtrl", "Acquire read buffer " + n + " failed!");
                                        ex.printStackTrace();
                                        continue;
                                    }
                                    break;
                                }
                                this.e[n2] = null;
                                ++n2;
                                continue Label_0161_Outer;
                            }
                            catch (IOException ex2) {
                                Log.d("ProcessInCtrl", "Close mMainPipe failed!");
                                ex2.printStackTrace();
                                continue;
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }
}
