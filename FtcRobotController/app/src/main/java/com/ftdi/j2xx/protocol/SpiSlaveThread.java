package com.ftdi.j2xx.protocol;

import java.util.concurrent.locks.ReentrantLock;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.Queue;

public abstract class SpiSlaveThread extends Thread
{
    public static final int THREAD_DESTORYED = 2;
    public static final int THREAD_INIT = 0;
    public static final int THREAD_RUNNING = 1;
    private Queue<SpiSlaveEvent> a;
    private Lock b;
    private Object c;
    private Object d;
    private boolean e;
    private boolean f;
    private int g;
    
    public SpiSlaveThread() {
        this.a = new LinkedList<SpiSlaveEvent>();
        this.c = new Object();
        this.d = new Object();
        this.b = new ReentrantLock();
        this.g = 0;
        this.setName("SpiSlaveThread");
    }
    
    protected abstract boolean isTerminateEvent(final SpiSlaveEvent p0);
    
    protected abstract boolean pollData();
    
    protected abstract void requestEvent(final SpiSlaveEvent p0);
    
    @Override
    public void run() {
        boolean terminateEvent = false;
        this.g = 1;
        while (!Thread.interrupted() && !terminateEvent) {
            this.pollData();
            this.b.lock();
            if (this.a.size() <= 0) {
                this.b.unlock();
            }
            else {
                final SpiSlaveEvent spiSlaveEvent = this.a.peek();
                this.a.remove();
                this.b.unlock();
                this.requestEvent(spiSlaveEvent);
                Label_0136: {
                    if (!spiSlaveEvent.getSync()) {
                        break Label_0136;
                    }
                    synchronized (this.d) {
                        while (this.f) {
                            try {
                                Thread.sleep(100L);
                            }
                            catch (InterruptedException ex) {}
                        }
                        this.f = true;
                        this.d.notify();
                        // monitorexit(this.d)
                        terminateEvent = this.isTerminateEvent(spiSlaveEvent);
                    }
                }
            }
        }
        this.g = 2;
    }
    
    public boolean sendMessage(final SpiSlaveEvent p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        com/ftdi/j2xx/protocol/SpiSlaveThread.g:I
        //     4: iconst_1       
        //     5: if_icmpne       50
        //     8: aload_0        
        //     9: getfield        com/ftdi/j2xx/protocol/SpiSlaveThread.b:Ljava/util/concurrent/locks/Lock;
        //    12: invokeinterface java/util/concurrent/locks/Lock.lock:()V
        //    17: aload_0        
        //    18: getfield        com/ftdi/j2xx/protocol/SpiSlaveThread.a:Ljava/util/Queue;
        //    21: invokeinterface java/util/Queue.size:()I
        //    26: bipush          10
        //    28: if_icmple       63
        //    31: aload_0        
        //    32: getfield        com/ftdi/j2xx/protocol/SpiSlaveThread.b:Ljava/util/concurrent/locks/Lock;
        //    35: invokeinterface java/util/concurrent/locks/Lock.unlock:()V
        //    40: ldc             "FTDI"
        //    42: ldc             "SpiSlaveThread sendMessage Buffer full!!"
        //    44: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    47: pop            
        //    48: iconst_0       
        //    49: ireturn        
        //    50: ldc2_w          100
        //    53: invokestatic    java/lang/Thread.sleep:(J)V
        //    56: goto            0
        //    59: astore_2       
        //    60: goto            0
        //    63: aload_0        
        //    64: getfield        com/ftdi/j2xx/protocol/SpiSlaveThread.a:Ljava/util/Queue;
        //    67: aload_1        
        //    68: invokeinterface java/util/Queue.add:(Ljava/lang/Object;)Z
        //    73: pop            
        //    74: aload_0        
        //    75: getfield        com/ftdi/j2xx/protocol/SpiSlaveThread.a:Ljava/util/Queue;
        //    78: invokeinterface java/util/Queue.size:()I
        //    83: iconst_1       
        //    84: if_icmpne       111
        //    87: aload_0        
        //    88: getfield        com/ftdi/j2xx/protocol/SpiSlaveThread.c:Ljava/lang/Object;
        //    91: astore          7
        //    93: aload           7
        //    95: monitorenter   
        //    96: aload_0        
        //    97: iconst_1       
        //    98: putfield        com/ftdi/j2xx/protocol/SpiSlaveThread.e:Z
        //   101: aload_0        
        //   102: getfield        com/ftdi/j2xx/protocol/SpiSlaveThread.c:Ljava/lang/Object;
        //   105: invokevirtual   java/lang/Object.notify:()V
        //   108: aload           7
        //   110: monitorexit    
        //   111: aload_0        
        //   112: getfield        com/ftdi/j2xx/protocol/SpiSlaveThread.b:Ljava/util/concurrent/locks/Lock;
        //   115: invokeinterface java/util/concurrent/locks/Lock.unlock:()V
        //   120: aload_1        
        //   121: invokevirtual   com/ftdi/j2xx/protocol/SpiSlaveEvent.getSync:()Z
        //   124: ifeq            151
        //   127: aload_0        
        //   128: getfield        com/ftdi/j2xx/protocol/SpiSlaveThread.d:Ljava/lang/Object;
        //   131: astore          4
        //   133: aload           4
        //   135: monitorenter   
        //   136: aload_0        
        //   137: iconst_0       
        //   138: putfield        com/ftdi/j2xx/protocol/SpiSlaveThread.f:Z
        //   141: aload_0        
        //   142: getfield        com/ftdi/j2xx/protocol/SpiSlaveThread.f:Z
        //   145: ifeq            161
        //   148: aload           4
        //   150: monitorexit    
        //   151: iconst_1       
        //   152: ireturn        
        //   153: astore          8
        //   155: aload           7
        //   157: monitorexit    
        //   158: aload           8
        //   160: athrow         
        //   161: aload_0        
        //   162: getfield        com/ftdi/j2xx/protocol/SpiSlaveThread.d:Ljava/lang/Object;
        //   165: invokevirtual   java/lang/Object.wait:()V
        //   168: goto            141
        //   171: astore          6
        //   173: aload_0        
        //   174: iconst_1       
        //   175: putfield        com/ftdi/j2xx/protocol/SpiSlaveThread.f:Z
        //   178: goto            141
        //   181: astore          5
        //   183: aload           4
        //   185: monitorexit    
        //   186: aload           5
        //   188: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  50     56     59     63     Ljava/lang/InterruptedException;
        //  96     111    153    161    Any
        //  136    141    181    189    Any
        //  141    151    181    189    Any
        //  155    158    153    161    Any
        //  161    168    171    181    Ljava/lang/InterruptedException;
        //  161    168    181    189    Any
        //  173    178    181    189    Any
        //  183    186    181    189    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0141:
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
}
