package com.qualcomm.hardware;

import java.util.Arrays;
import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.util.HashMap;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.modernrobotics.ReadWriteRunnableUsbHandler;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ReadWriteRunnableStandard implements ReadWriteRunnable
{
    protected final boolean DEBUG_LOGGING;
    private volatile boolean a;
    protected Callback callback;
    protected final byte[] localDeviceReadCache;
    protected final byte[] localDeviceWriteCache;
    protected int monitorLength;
    protected volatile boolean running;
    protected ConcurrentLinkedQueue<Integer> segmentReadQueue;
    protected ConcurrentLinkedQueue<Integer> segmentWriteQueue;
    protected Map<Integer, ReadWriteRunnableSegment> segments;
    protected final SerialNumber serialNumber;
    protected volatile boolean shutdownComplete;
    protected int startAddress;
    protected final ReadWriteRunnableUsbHandler usbHandler;
    
    public ReadWriteRunnableStandard(final SerialNumber serialNumber, final RobotUsbDevice robotUsbDevice, final int monitorLength, final int startAddress, final boolean debug_LOGGING) {
        this.localDeviceReadCache = new byte[256];
        this.localDeviceWriteCache = new byte[256];
        this.segments = new HashMap<Integer, ReadWriteRunnableSegment>();
        this.segmentReadQueue = new ConcurrentLinkedQueue<Integer>();
        this.segmentWriteQueue = new ConcurrentLinkedQueue<Integer>();
        this.running = false;
        this.shutdownComplete = false;
        this.a = false;
        this.serialNumber = serialNumber;
        this.startAddress = startAddress;
        this.monitorLength = monitorLength;
        this.DEBUG_LOGGING = debug_LOGGING;
        this.callback = new EmptyCallback();
        this.usbHandler = new ReadWriteRunnableUsbHandler(robotUsbDevice);
    }
    
    @Override
    public void blockUntilReady() throws RobotCoreException, InterruptedException {
    }
    
    @Override
    public void close() {
        try {
            this.blockUntilReady();
            this.startBlockingWork();
        }
        catch (InterruptedException ex) {}
        catch (RobotCoreException ex2) {}
        finally {
            this.running = false;
            while (!this.shutdownComplete) {
                Thread.yield();
            }
            this.usbHandler.close();
        }
    }
    
    @Override
    public ReadWriteRunnableSegment createSegment(final int n, final int n2, final int n3) {
        final ReadWriteRunnableSegment readWriteRunnableSegment = new ReadWriteRunnableSegment(n2, n3);
        this.segments.put(n, readWriteRunnableSegment);
        return readWriteRunnableSegment;
    }
    
    public void destroySegment(final int n) {
        this.segments.remove(n);
    }
    
    protected void dumpBuffers(final String s, final byte[] array) {
        RobotLog.v("Dumping " + s + " buffers for " + this.serialNumber);
        final StringBuilder sb = new StringBuilder(1024);
        for (int i = 0; i < this.startAddress + this.monitorLength; ++i) {
            sb.append(String.format(" %02x", TypeConversion.unsignedByteToInt(array[i])));
            if ((i + 1) % 16 == 0) {
                sb.append("\n");
            }
        }
        RobotLog.v(sb.toString());
    }
    
    public ReadWriteRunnableSegment getSegment(final int n) {
        return this.segments.get(n);
    }
    
    protected void queueIfNotAlreadyQueued(final int n, final ConcurrentLinkedQueue<Integer> concurrentLinkedQueue) {
        if (!concurrentLinkedQueue.contains(n)) {
            concurrentLinkedQueue.add(n);
        }
    }
    
    @Override
    public void queueSegmentRead(final int n) {
        this.queueIfNotAlreadyQueued(n, this.segmentReadQueue);
    }
    
    @Override
    public void queueSegmentWrite(final int n) {
        this.queueIfNotAlreadyQueued(n, this.segmentWriteQueue);
    }
    
    @Override
    public byte[] read(final int n, final int n2) {
        synchronized (this.localDeviceReadCache) {
            return Arrays.copyOfRange(this.localDeviceReadCache, n, n + n2);
        }
    }
    
    @Override
    public byte[] readFromWriteCache(final int n, final int n2) {
        synchronized (this.localDeviceWriteCache) {
            return Arrays.copyOfRange(this.localDeviceWriteCache, n, n + n2);
        }
    }
    
    @Override
    public void run() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.monitorLength:I
        //     4: aload_0        
        //     5: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.startAddress:I
        //     8: iadd           
        //     9: newarray        B
        //    11: astore_1       
        //    12: new             Lcom/qualcomm/robotcore/util/ElapsedTime;
        //    15: dup            
        //    16: invokespecial   com/qualcomm/robotcore/util/ElapsedTime.<init>:()V
        //    19: astore_2       
        //    20: new             Ljava/lang/StringBuilder;
        //    23: dup            
        //    24: invokespecial   java/lang/StringBuilder.<init>:()V
        //    27: ldc             "Device "
        //    29: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    32: aload_0        
        //    33: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.serialNumber:Lcom/qualcomm/robotcore/util/SerialNumber;
        //    36: invokevirtual   com/qualcomm/robotcore/util/SerialNumber.toString:()Ljava/lang/String;
        //    39: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    42: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    45: astore_3       
        //    46: aload_0        
        //    47: iconst_1       
        //    48: putfield        com/qualcomm/hardware/ReadWriteRunnableStandard.running:Z
        //    51: iconst_1       
        //    52: anewarray       Ljava/lang/Object;
        //    55: astore          4
        //    57: aload           4
        //    59: iconst_0       
        //    60: aload_0        
        //    61: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.serialNumber:Lcom/qualcomm/robotcore/util/SerialNumber;
        //    64: aastore        
        //    65: ldc             "starting read/write loop for device %s"
        //    67: aload           4
        //    69: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //    72: invokestatic    com/qualcomm/robotcore/util/RobotLog.v:(Ljava/lang/String;)V
        //    75: aload_0        
        //    76: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.usbHandler:Lcom/qualcomm/modernrobotics/ReadWriteRunnableUsbHandler;
        //    79: getstatic       com/qualcomm/robotcore/hardware/usb/RobotUsbDevice$Channel.RX:Lcom/qualcomm/robotcore/hardware/usb/RobotUsbDevice$Channel;
        //    82: invokevirtual   com/qualcomm/modernrobotics/ReadWriteRunnableUsbHandler.purge:(Lcom/qualcomm/robotcore/hardware/usb/RobotUsbDevice$Channel;)V
        //    85: iconst_0       
        //    86: istore          12
        //    88: iconst_1       
        //    89: istore          13
        //    91: aload_0        
        //    92: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.running:Z
        //    95: ifeq            579
        //    98: aload_0        
        //    99: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.DEBUG_LOGGING:Z
        //   102: ifeq            114
        //   105: aload_2        
        //   106: aload_3        
        //   107: invokevirtual   com/qualcomm/robotcore/util/ElapsedTime.log:(Ljava/lang/String;)V
        //   110: aload_2        
        //   111: invokevirtual   com/qualcomm/robotcore/util/ElapsedTime.reset:()V
        //   114: aload_0        
        //   115: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.usbHandler:Lcom/qualcomm/modernrobotics/ReadWriteRunnableUsbHandler;
        //   118: iload           12
        //   120: aload_1        
        //   121: invokevirtual   com/qualcomm/modernrobotics/ReadWriteRunnableUsbHandler.read:(I[B)V
        //   124: aload_0        
        //   125: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.segmentReadQueue:Ljava/util/concurrent/ConcurrentLinkedQueue;
        //   128: invokevirtual   java/util/concurrent/ConcurrentLinkedQueue.isEmpty:()Z
        //   131: ifne            256
        //   134: aload_0        
        //   135: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.segments:Ljava/util/Map;
        //   138: aload_0        
        //   139: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.segmentReadQueue:Ljava/util/concurrent/ConcurrentLinkedQueue;
        //   142: invokevirtual   java/util/concurrent/ConcurrentLinkedQueue.remove:()Ljava/lang/Object;
        //   145: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   150: checkcast       Lcom/qualcomm/hardware/ReadWriteRunnableSegment;
        //   153: astore          26
        //   155: aload           26
        //   157: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableSegment.getReadBuffer:()[B
        //   160: arraylength    
        //   161: newarray        B
        //   163: astore          27
        //   165: aload_0        
        //   166: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.usbHandler:Lcom/qualcomm/modernrobotics/ReadWriteRunnableUsbHandler;
        //   169: aload           26
        //   171: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableSegment.getAddress:()I
        //   174: aload           27
        //   176: invokevirtual   com/qualcomm/modernrobotics/ReadWriteRunnableUsbHandler.read:(I[B)V
        //   179: aload           26
        //   181: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableSegment.getReadLock:()Ljava/util/concurrent/locks/Lock;
        //   184: invokeinterface java/util/concurrent/locks/Lock.lock:()V
        //   189: aload           27
        //   191: iconst_0       
        //   192: aload           26
        //   194: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableSegment.getReadBuffer:()[B
        //   197: iconst_0       
        //   198: aload           26
        //   200: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableSegment.getReadBuffer:()[B
        //   203: arraylength    
        //   204: invokestatic    java/lang/System.arraycopy:(Ljava/lang/Object;ILjava/lang/Object;II)V
        //   207: aload           26
        //   209: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableSegment.getReadLock:()Ljava/util/concurrent/locks/Lock;
        //   212: invokeinterface java/util/concurrent/locks/Lock.unlock:()V
        //   217: goto            124
        //   220: astore          14
        //   222: iconst_2       
        //   223: anewarray       Ljava/lang/Object;
        //   226: astore          15
        //   228: aload           15
        //   230: iconst_0       
        //   231: aload_0        
        //   232: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.serialNumber:Lcom/qualcomm/robotcore/util/SerialNumber;
        //   235: aastore        
        //   236: aload           15
        //   238: iconst_1       
        //   239: aload           14
        //   241: invokevirtual   com/qualcomm/robotcore/exception/RobotCoreException.getMessage:()Ljava/lang/String;
        //   244: aastore        
        //   245: ldc_w           "could not read from device %s: %s"
        //   248: aload           15
        //   250: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   253: invokestatic    com/qualcomm/robotcore/util/RobotLog.w:(Ljava/lang/String;)V
        //   256: aload_0        
        //   257: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.localDeviceReadCache:[B
        //   260: astore          16
        //   262: aload           16
        //   264: monitorenter   
        //   265: aload_1        
        //   266: iconst_0       
        //   267: aload_0        
        //   268: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.localDeviceReadCache:[B
        //   271: iload           12
        //   273: aload_1        
        //   274: arraylength    
        //   275: invokestatic    java/lang/System.arraycopy:(Ljava/lang/Object;ILjava/lang/Object;II)V
        //   278: aload           16
        //   280: monitorexit    
        //   281: aload_0        
        //   282: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.DEBUG_LOGGING:Z
        //   285: ifeq            299
        //   288: aload_0        
        //   289: ldc_w           "read"
        //   292: aload_0        
        //   293: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.localDeviceReadCache:[B
        //   296: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableStandard.dumpBuffers:(Ljava/lang/String;[B)V
        //   299: aload_0        
        //   300: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.callback:Lcom/qualcomm/hardware/ReadWriteRunnable$Callback;
        //   303: invokeinterface com/qualcomm/hardware/ReadWriteRunnable$Callback.readComplete:()V
        //   308: aload_0        
        //   309: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableStandard.waitForSyncdEvents:()V
        //   312: iload           13
        //   314: ifeq            337
        //   317: aload_0        
        //   318: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.startAddress:I
        //   321: istore          18
        //   323: aload_0        
        //   324: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.monitorLength:I
        //   327: newarray        B
        //   329: astore_1       
        //   330: iload           18
        //   332: istore          12
        //   334: iconst_0       
        //   335: istore          13
        //   337: aload_0        
        //   338: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.localDeviceWriteCache:[B
        //   341: astore          19
        //   343: aload           19
        //   345: monitorenter   
        //   346: aload_0        
        //   347: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.localDeviceWriteCache:[B
        //   350: iload           12
        //   352: aload_1        
        //   353: iconst_0       
        //   354: aload_1        
        //   355: arraylength    
        //   356: invokestatic    java/lang/System.arraycopy:(Ljava/lang/Object;ILjava/lang/Object;II)V
        //   359: aload           19
        //   361: monitorexit    
        //   362: aload_0        
        //   363: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableStandard.writeNeeded:()Z
        //   366: ifeq            384
        //   369: aload_0        
        //   370: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.usbHandler:Lcom/qualcomm/modernrobotics/ReadWriteRunnableUsbHandler;
        //   373: iload           12
        //   375: aload_1        
        //   376: invokevirtual   com/qualcomm/modernrobotics/ReadWriteRunnableUsbHandler.write:(I[B)V
        //   379: aload_0        
        //   380: iconst_0       
        //   381: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableStandard.setWriteNeeded:(Z)V
        //   384: aload_0        
        //   385: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.segmentWriteQueue:Ljava/util/concurrent/ConcurrentLinkedQueue;
        //   388: invokevirtual   java/util/concurrent/ConcurrentLinkedQueue.isEmpty:()Z
        //   391: ifne            504
        //   394: aload_0        
        //   395: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.segments:Ljava/util/Map;
        //   398: aload_0        
        //   399: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.segmentWriteQueue:Ljava/util/concurrent/ConcurrentLinkedQueue;
        //   402: invokevirtual   java/util/concurrent/ConcurrentLinkedQueue.remove:()Ljava/lang/Object;
        //   405: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   410: checkcast       Lcom/qualcomm/hardware/ReadWriteRunnableSegment;
        //   413: astore          23
        //   415: aload           23
        //   417: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableSegment.getWriteLock:()Ljava/util/concurrent/locks/Lock;
        //   420: invokeinterface java/util/concurrent/locks/Lock.lock:()V
        //   425: aload           23
        //   427: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableSegment.getWriteBuffer:()[B
        //   430: aload           23
        //   432: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableSegment.getWriteBuffer:()[B
        //   435: arraylength    
        //   436: invokestatic    java/util/Arrays.copyOf:([BI)[B
        //   439: astore          25
        //   441: aload           23
        //   443: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableSegment.getWriteLock:()Ljava/util/concurrent/locks/Lock;
        //   446: invokeinterface java/util/concurrent/locks/Lock.unlock:()V
        //   451: aload_0        
        //   452: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.usbHandler:Lcom/qualcomm/modernrobotics/ReadWriteRunnableUsbHandler;
        //   455: aload           23
        //   457: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableSegment.getAddress:()I
        //   460: aload           25
        //   462: invokevirtual   com/qualcomm/modernrobotics/ReadWriteRunnableUsbHandler.write:(I[B)V
        //   465: goto            384
        //   468: astore          21
        //   470: iconst_2       
        //   471: anewarray       Ljava/lang/Object;
        //   474: astore          22
        //   476: aload           22
        //   478: iconst_0       
        //   479: aload_0        
        //   480: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.serialNumber:Lcom/qualcomm/robotcore/util/SerialNumber;
        //   483: aastore        
        //   484: aload           22
        //   486: iconst_1       
        //   487: aload           21
        //   489: invokevirtual   com/qualcomm/robotcore/exception/RobotCoreException.getMessage:()Ljava/lang/String;
        //   492: aastore        
        //   493: ldc_w           "could not write to device %s: %s"
        //   496: aload           22
        //   498: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   501: invokestatic    com/qualcomm/robotcore/util/RobotLog.w:(Ljava/lang/String;)V
        //   504: aload_0        
        //   505: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.DEBUG_LOGGING:Z
        //   508: ifeq            522
        //   511: aload_0        
        //   512: ldc_w           "write"
        //   515: aload_0        
        //   516: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.localDeviceWriteCache:[B
        //   519: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableStandard.dumpBuffers:(Ljava/lang/String;[B)V
        //   522: aload_0        
        //   523: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.callback:Lcom/qualcomm/hardware/ReadWriteRunnable$Callback;
        //   526: invokeinterface com/qualcomm/hardware/ReadWriteRunnable$Callback.writeComplete:()V
        //   531: aload_0        
        //   532: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.usbHandler:Lcom/qualcomm/modernrobotics/ReadWriteRunnableUsbHandler;
        //   535: invokevirtual   com/qualcomm/modernrobotics/ReadWriteRunnableUsbHandler.throwIfUsbErrorCountIsTooHigh:()V
        //   538: goto            91
        //   541: astore          10
        //   543: iconst_1       
        //   544: anewarray       Ljava/lang/Object;
        //   547: astore          11
        //   549: aload           11
        //   551: iconst_0       
        //   552: aload_0        
        //   553: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.serialNumber:Lcom/qualcomm/robotcore/util/SerialNumber;
        //   556: aastore        
        //   557: ldc_w           "could not write to device %s: FTDI Null Pointer Exception"
        //   560: aload           11
        //   562: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   565: invokestatic    com/qualcomm/robotcore/util/RobotLog.w:(Ljava/lang/String;)V
        //   568: aload           10
        //   570: invokestatic    com/qualcomm/robotcore/util/RobotLog.logStacktrace:(Ljava/lang/Exception;)V
        //   573: ldc_w           "There was a problem communicating with a Modern Robotics USB device"
        //   576: invokestatic    com/qualcomm/robotcore/util/RobotLog.setGlobalErrorMsg:(Ljava/lang/String;)V
        //   579: iconst_1       
        //   580: anewarray       Ljava/lang/Object;
        //   583: astore          7
        //   585: aload           7
        //   587: iconst_0       
        //   588: aload_0        
        //   589: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.serialNumber:Lcom/qualcomm/robotcore/util/SerialNumber;
        //   592: aastore        
        //   593: ldc_w           "stopped read/write loop for device %s"
        //   596: aload           7
        //   598: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   601: invokestatic    com/qualcomm/robotcore/util/RobotLog.v:(Ljava/lang/String;)V
        //   604: aload_0        
        //   605: iconst_0       
        //   606: putfield        com/qualcomm/hardware/ReadWriteRunnableStandard.running:Z
        //   609: aload_0        
        //   610: iconst_1       
        //   611: putfield        com/qualcomm/hardware/ReadWriteRunnableStandard.shutdownComplete:Z
        //   614: return         
        //   615: astore          28
        //   617: aload           26
        //   619: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableSegment.getReadLock:()Ljava/util/concurrent/locks/Lock;
        //   622: invokeinterface java/util/concurrent/locks/Lock.unlock:()V
        //   627: aload           28
        //   629: athrow         
        //   630: astore          8
        //   632: iconst_1       
        //   633: anewarray       Ljava/lang/Object;
        //   636: astore          9
        //   638: aload           9
        //   640: iconst_0       
        //   641: aload_0        
        //   642: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.serialNumber:Lcom/qualcomm/robotcore/util/SerialNumber;
        //   645: aastore        
        //   646: ldc_w           "could not write to device %s: Interrupted Exception"
        //   649: aload           9
        //   651: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   654: invokestatic    com/qualcomm/robotcore/util/RobotLog.w:(Ljava/lang/String;)V
        //   657: goto            579
        //   660: astore          17
        //   662: aload           16
        //   664: monitorexit    
        //   665: aload           17
        //   667: athrow         
        //   668: astore          5
        //   670: aload           5
        //   672: invokevirtual   com/qualcomm/robotcore/exception/RobotCoreException.getMessage:()Ljava/lang/String;
        //   675: invokestatic    com/qualcomm/robotcore/util/RobotLog.w:(Ljava/lang/String;)V
        //   678: iconst_1       
        //   679: anewarray       Ljava/lang/Object;
        //   682: astore          6
        //   684: aload           6
        //   686: iconst_0       
        //   687: aload_0        
        //   688: getfield        com/qualcomm/hardware/ReadWriteRunnableStandard.serialNumber:Lcom/qualcomm/robotcore/util/SerialNumber;
        //   691: aastore        
        //   692: ldc_w           "There was a problem communicating with a Modern Robotics USB device %s"
        //   695: aload           6
        //   697: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   700: invokestatic    com/qualcomm/robotcore/util/RobotLog.setGlobalErrorMsg:(Ljava/lang/String;)V
        //   703: goto            579
        //   706: astore          20
        //   708: aload           19
        //   710: monitorexit    
        //   711: aload           20
        //   713: athrow         
        //   714: astore          24
        //   716: aload           23
        //   718: invokevirtual   com/qualcomm/hardware/ReadWriteRunnableSegment.getWriteLock:()Ljava/util/concurrent/locks/Lock;
        //   721: invokeinterface java/util/concurrent/locks/Lock.unlock:()V
        //   726: aload           24
        //   728: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                 
        //  -----  -----  -----  -----  -----------------------------------------------------
        //  75     85     541    579    Ljava/lang/NullPointerException;
        //  75     85     630    660    Ljava/lang/InterruptedException;
        //  75     85     668    706    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  91     114    541    579    Ljava/lang/NullPointerException;
        //  91     114    630    660    Ljava/lang/InterruptedException;
        //  91     114    668    706    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  114    124    220    256    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  114    124    541    579    Ljava/lang/NullPointerException;
        //  114    124    630    660    Ljava/lang/InterruptedException;
        //  124    179    220    256    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  124    179    541    579    Ljava/lang/NullPointerException;
        //  124    179    630    660    Ljava/lang/InterruptedException;
        //  179    207    615    630    Any
        //  207    217    220    256    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  207    217    541    579    Ljava/lang/NullPointerException;
        //  207    217    630    660    Ljava/lang/InterruptedException;
        //  222    256    541    579    Ljava/lang/NullPointerException;
        //  222    256    630    660    Ljava/lang/InterruptedException;
        //  222    256    668    706    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  256    265    541    579    Ljava/lang/NullPointerException;
        //  256    265    630    660    Ljava/lang/InterruptedException;
        //  256    265    668    706    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  265    281    660    668    Any
        //  281    299    541    579    Ljava/lang/NullPointerException;
        //  281    299    630    660    Ljava/lang/InterruptedException;
        //  281    299    668    706    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  299    312    541    579    Ljava/lang/NullPointerException;
        //  299    312    630    660    Ljava/lang/InterruptedException;
        //  299    312    668    706    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  317    330    541    579    Ljava/lang/NullPointerException;
        //  317    330    630    660    Ljava/lang/InterruptedException;
        //  317    330    668    706    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  337    346    541    579    Ljava/lang/NullPointerException;
        //  337    346    630    660    Ljava/lang/InterruptedException;
        //  337    346    668    706    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  346    362    706    714    Any
        //  362    384    468    504    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  362    384    541    579    Ljava/lang/NullPointerException;
        //  362    384    630    660    Ljava/lang/InterruptedException;
        //  384    415    468    504    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  384    415    541    579    Ljava/lang/NullPointerException;
        //  384    415    630    660    Ljava/lang/InterruptedException;
        //  415    441    714    729    Any
        //  441    465    468    504    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  441    465    541    579    Ljava/lang/NullPointerException;
        //  441    465    630    660    Ljava/lang/InterruptedException;
        //  470    504    541    579    Ljava/lang/NullPointerException;
        //  470    504    630    660    Ljava/lang/InterruptedException;
        //  470    504    668    706    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  504    522    541    579    Ljava/lang/NullPointerException;
        //  504    522    630    660    Ljava/lang/InterruptedException;
        //  504    522    668    706    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  522    538    541    579    Ljava/lang/NullPointerException;
        //  522    538    630    660    Ljava/lang/InterruptedException;
        //  522    538    668    706    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  617    630    220    256    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  617    630    541    579    Ljava/lang/NullPointerException;
        //  617    630    630    660    Ljava/lang/InterruptedException;
        //  662    665    660    668    Any
        //  665    668    541    579    Ljava/lang/NullPointerException;
        //  665    668    630    660    Ljava/lang/InterruptedException;
        //  665    668    668    706    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  708    711    706    714    Any
        //  711    714    541    579    Ljava/lang/NullPointerException;
        //  711    714    630    660    Ljava/lang/InterruptedException;
        //  711    714    668    706    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  716    729    468    504    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  716    729    541    579    Ljava/lang/NullPointerException;
        //  716    729    630    660    Ljava/lang/InterruptedException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 339, Size: 339
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3305)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:114)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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
    
    @Override
    public void setCallback(final Callback callback) {
        this.callback = callback;
    }
    
    public void setWriteNeeded(final boolean a) {
        this.a = a;
    }
    
    @Override
    public void startBlockingWork() {
    }
    
    protected void waitForSyncdEvents() throws RobotCoreException, InterruptedException {
    }
    
    @Override
    public void write(final int n, final byte[] array) {
        synchronized (this.localDeviceWriteCache) {
            System.arraycopy(array, 0, this.localDeviceWriteCache, n, array.length);
            this.a = true;
        }
    }
    
    public boolean writeNeeded() {
        return this.a;
    }
}
