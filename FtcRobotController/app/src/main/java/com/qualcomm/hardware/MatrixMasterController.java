//
// Source code recreated from Type1 .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MatrixMasterController implements I2cPortReadyCallback {
    private static final byte[] a = new byte[]{(byte)0, (byte)70, (byte)72, (byte)74, (byte)76};
    private static final byte[] b = new byte[]{(byte)0, (byte)78, (byte)88, (byte)98, (byte)108};
    private static final byte[] c = new byte[]{(byte)0, (byte)82, (byte)92, (byte)102, (byte)112};
    private static final byte[] d = new byte[]{(byte)0, (byte)86, (byte)96, (byte)106, (byte)116};
    private static final byte[] e = new byte[]{(byte)0, (byte)87, (byte)97, (byte)107, (byte)117};
    private final ElapsedTime g = new ElapsedTime(0L);
    protected ConcurrentLinkedQueue<MatrixI2cTransaction> transactionQueue;
    protected ModernRoboticsUsbLegacyModule legacyModule;
    protected MatrixDcMotorController motorController;
    protected MatrixServoController servoController;
    protected int physicalPort;
    private volatile boolean f = false;

    public MatrixMasterController(ModernRoboticsUsbLegacyModule legacyModule, int physicalPort) {
        this.legacyModule = legacyModule;
        this.physicalPort = physicalPort;
        this.transactionQueue = new ConcurrentLinkedQueue();
        legacyModule.registerForI2cPortReadyCallback(this, physicalPort);
    }

    public void registerMotorController(MatrixDcMotorController mc) {
        this.motorController = mc;
    }

    public void registerServoController(MatrixServoController sc) {
        this.servoController = sc;
    }

    public int getPort() {
        return this.physicalPort;
    }

    public String getConnectionInfo() {
        return this.legacyModule.getConnectionInfo() + "; port " + this.physicalPort;
    }

    public boolean queueTransaction(MatrixI2cTransaction transaction, boolean force) {
        if(!force) {
            Iterator var3 = this.transactionQueue.iterator();

            while(var3.hasNext()) {
                MatrixI2cTransaction var4 = (MatrixI2cTransaction)var3.next();
                if(var4.isEqual(transaction)) {
                    this.buginf("NO Queue transaction " + transaction.toString());
                    return false;
                }
            }
        }

        this.buginf("YES Queue transaction " + transaction.toString());
        this.transactionQueue.add(transaction);
        return true;
    }

    public boolean queueTransaction(MatrixI2cTransaction transaction) {
        return this.queueTransaction(transaction, false);
    }

    public void waitOnRead() {
        synchronized(this) {
            this.f = true;

            try {
                while(this.f) {
                    this.wait(0L);
                }
            } catch (InterruptedException var4) {
                var4.printStackTrace();
            }

        }
    }

    protected void handleReadDone(MatrixI2cTransaction transaction) {
        byte[] var2 = this.legacyModule.getI2cReadCache(this.physicalPort);
        switch (transaction.property.ordinal()) {
        case 1:
            this.motorController.handleReadBattery(var2);
            break;
        case 2:
            this.motorController.handleReadPosition(transaction, var2);
            break;
        case 3:
            this.motorController.handleReadPosition(transaction, var2);
            break;
        case 4:
            this.motorController.handleReadMode(transaction, var2);
            break;
        case 5:
            this.servoController.handleReadServo(transaction, var2);
            break;
        default:
            RobotLog.e("Transaction not Type1 read " + transaction.property);
        }

        synchronized(this) {
            if(this.f) {
                this.f = false;
                this.notify();
            }

        }
    }

    protected void sendHeartbeat() {
        MatrixI2cTransaction var1 = new MatrixI2cTransaction((byte) 0, MatrixI2cTransaction.Type1.a, 3);
        this.queueTransaction(var1);
    }

    public void portIsReady(int port) {
        if(this.transactionQueue.isEmpty()) {
            if(this.g.time() > 2.0D) {
                this.sendHeartbeat();
                this.g.reset();
            }

        } else {
            MatrixI2cTransaction var5 = this.transactionQueue.peek();
            if (var5.state == MatrixI2cTransaction.Type2.b) {
                this.legacyModule.readI2cCacheFromModule(this.physicalPort);
                var5.state = MatrixI2cTransaction.Type2.d;
            } else {
                if (var5.state == MatrixI2cTransaction.Type2.d) {
                    var5 = this.transactionQueue.poll();
                    if(this.transactionQueue.isEmpty()) {
                        return;
                    }

                    var5 = this.transactionQueue.peek();
                } else if (var5.state == MatrixI2cTransaction.Type2.d) {
                    this.handleReadDone(var5);
                    var5 = this.transactionQueue.poll();
                    if(this.transactionQueue.isEmpty()) {
                        return;
                    }

                    var5 = this.transactionQueue.peek();
                }

                byte[] var2;
                byte var3;
                byte var4;
                switch (var5.property.ordinal()) {
                case 1:
                    var3 = 67;
                    var2 = new byte[]{(byte)0};
                    var4 = 1;
                    break;
                case 2:
                    var3 = b[var5.motor];
                    var4 = 4;
                    var2 = new byte[]{(byte)0};
                    break;
                case 3:
                    var3 = c[var5.motor];
                    var2 = TypeConversion.intToByteArray(var5.value);
                    var4 = 4;
                    break;
                case 4:
                    var3 = e[var5.motor];
                    var2 = new byte[]{(byte)var5.value};
                    var4 = 1;
                    break;
                case 5:
                    var3 = a[var5.servo];
                    var2 = new byte[]{var5.speed, (byte)var5.target};
                    var4 = 2;
                    break;
                case 6:
                    var3 = 66;
                    var2 = new byte[]{(byte)var5.value};
                    var4 = 1;
                    break;
                case 7:
                    var3 = 68;
                    var2 = new byte[]{(byte)var5.value};
                    var4 = 1;
                    break;
                case 8:
                    var3 = d[var5.motor];
                    var2 = new byte[]{(byte)var5.value};
                    var4 = 1;
                    break;
                case 9:
                    var3 = b[var5.motor];
                    ByteBuffer var6 = ByteBuffer.allocate(10);
                    var6.put(TypeConversion.intToByteArray(0));
                    var6.put(TypeConversion.intToByteArray(var5.target));
                    var6.put(var5.speed);
                    var6.put(var5.mode);
                    var2 = var6.array();
                    var4 = 10;
                    break;
                case 10:
                    var3 = 69;
                    var2 = new byte[]{(byte)var5.value};
                    var4 = 1;
                    break;
                default:
                    var3 = 0;
                    var2 = new byte[]{(byte)var5.value};
                    var4 = 1;
                }

                try {
                    if(var5.write) {
                        this.legacyModule.setWriteMode(this.physicalPort, 16, var3);
                        this.legacyModule.setData(this.physicalPort, var2, var4);
                        var5.state = MatrixI2cTransaction.Type2.c;
                    } else {
                        this.legacyModule.setReadMode(this.physicalPort, 16, var3, var4);
                        var5.state = MatrixI2cTransaction.Type2.b;
                    }

                    this.legacyModule.setI2cPortActionFlag(this.physicalPort);
                    this.legacyModule.writeI2cCacheToModule(this.physicalPort);
                } catch (IllegalArgumentException var7) {
                    RobotLog.e(var7.getMessage());
                }

                this.buginf(var5.toString());
            }
        }
    }

    protected void buginf(String s) {
    }
}
