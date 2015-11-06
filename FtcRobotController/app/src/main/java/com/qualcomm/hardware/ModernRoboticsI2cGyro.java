//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;

public class ModernRoboticsI2cGyro extends GyroSensor implements HardwareDevice, I2cPortReadyCallback {
    public static final int ADDRESS_I2C = 32;
    protected static final int OFFSET_FIRMWARE_REV = 0;
    protected static final int OFFSET_MANUFACTURE_CODE = 1;
    protected static final int OFFSET_SENSOR_ID = 2;
    protected static final int OFFSET_COMMAND = 3;
    protected static final int OFFSET_HEADING_DATA = 4;
    protected static final int OFFSET_INTEGRATED_Z_VAL = 6;
    protected static final int OFFSET_RAW_X_VAL = 8;
    protected static final int OFFSET_RAW_Y_VAL = 10;
    protected static final int OFFSET_RAW_Z_VAL = 12;
    protected static final int OFFSET_Z_AXIS_OFFSET = 14;
    protected static final int OFFSET_Z_AXIS_SCALE_COEF = 16;
    protected static final int OFFSET_NEW_I2C_ADDRESS = 112;
    protected static final int OFFSET_TRIGGER_1 = 113;
    protected static final int OFFSET_TRIGGER_2 = 114;
    protected static final int TRIGGER_1_VAL = 85;
    protected static final int TRIGGER_2_VAL = 170;
    protected static final byte COMMAND_NORMAL = 0;
    protected static final byte COMMAND_NULL = 78;
    protected static final byte COMMAND_RESET_Z_AXIS = 82;
    protected static final byte COMMAND_WRITE_EEPROM = 87;
    protected static final int BUFFER_LENGTH = 18;
    protected ConcurrentLinkedQueue<ModernRoboticsI2cGyro.GyroI2cTransaction> transactionQueue;
    private int a = 32;
    private final DeviceInterfaceModule b;
    private final byte[] c;
    private final Lock d;
    private final byte[] e;
    private final Lock f;
    private final int g;
    private ModernRoboticsI2cGyro.HeadingMode h;
    private ModernRoboticsI2cGyro.MeasurementMode i;
    private ModernRoboticsI2cGyro.a j;

    public ModernRoboticsI2cGyro(DeviceInterfaceModule deviceInterfaceModule, int physicalPort) {
        this.b = deviceInterfaceModule;
        this.g = physicalPort;
        this.c = deviceInterfaceModule.getI2cReadCache(physicalPort);
        this.d = deviceInterfaceModule.getI2cReadCacheLock(physicalPort);
        this.e = deviceInterfaceModule.getI2cWriteCache(physicalPort);
        this.f = deviceInterfaceModule.getI2cWriteCacheLock(physicalPort);
        this.h = ModernRoboticsI2cGyro.HeadingMode.HEADING_CARDINAL;
        deviceInterfaceModule.enableI2cReadMode(physicalPort, 32, 0, 18);
        deviceInterfaceModule.setI2cPortActionFlag(physicalPort);
        deviceInterfaceModule.writeI2cCacheToController(physicalPort);
        deviceInterfaceModule.registerForI2cPortReadyCallback(this, physicalPort);
        this.transactionQueue = new ConcurrentLinkedQueue();
        this.j = new ModernRoboticsI2cGyro.a();
        this.i = ModernRoboticsI2cGyro.MeasurementMode.GYRO_NORMAL;
    }

    public boolean queueTransaction(ModernRoboticsI2cGyro.GyroI2cTransaction transaction, boolean force) {
        if(!force) {
            Iterator var3 = this.transactionQueue.iterator();

            while(var3.hasNext()) {
                ModernRoboticsI2cGyro.GyroI2cTransaction var4 = (ModernRoboticsI2cGyro.GyroI2cTransaction)var3.next();
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

    public boolean queueTransaction(ModernRoboticsI2cGyro.GyroI2cTransaction transaction) {
        return this.queueTransaction(transaction, false);
    }

    public void calibrate() {
        ModernRoboticsI2cGyro.GyroI2cTransaction var1 = new ModernRoboticsI2cGyro.GyroI2cTransaction((byte)78);
        this.queueTransaction(var1);
    }

    public boolean isCalibrating() {
        return this.i != ModernRoboticsI2cGyro.MeasurementMode.GYRO_NORMAL;
    }

    public ModernRoboticsI2cGyro.HeadingMode getHeadingMode() {
        return this.h;
    }

    public void setHeadingMode(ModernRoboticsI2cGyro.HeadingMode headingMode) {
        this.h = headingMode;
    }

    public ModernRoboticsI2cGyro.MeasurementMode getMeasurementMode() {
        return this.i;
    }

    public int getHeading() {
        return this.h == ModernRoboticsI2cGyro.HeadingMode.HEADING_CARDINAL?(this.j.e == 0?this.j.e:Math.abs(this.j.e - 360)):this.j.e;
    }

    public double getRotation() {
        this.notSupported();
        return 0.0D;
    }

    public int getIntegratedZValue() {
        return this.j.f;
    }

    public int rawX() {
        return this.j.g;
    }

    public int rawY() {
        return this.j.h;
    }

    public int rawZ() {
        return this.j.i;
    }

    public void resetZAxisIntegrator() {
        ModernRoboticsI2cGyro.GyroI2cTransaction var1 = new ModernRoboticsI2cGyro.GyroI2cTransaction((byte)82);
        this.queueTransaction(var1);
    }

    public String getDeviceName() {
        return "Modern Robotics Gyro";
    }

    public String getConnectionInfo() {
        return this.b.getConnectionInfo() + "; I2C port: " + this.g;
    }

    public String status() {
        return String.format("Modern Robotics Gyro, connected via device %s, port %d", new Object[]{this.b.getSerialNumber().toString(), Integer.valueOf(this.g)});
    }

    public int getVersion() {
        return 1;
    }

    public void close() {
    }

    private void a() {
        try {
            this.d.lock();
            ByteBuffer var1 = ByteBuffer.wrap(this.c);
            var1.order(ByteOrder.LITTLE_ENDIAN);
            this.j.a = this.c[4];
            this.j.b = this.c[5];
            this.j.c = this.c[6];
            this.j.d = this.c[7];
            this.j.e = var1.getShort(8);
            this.j.f = var1.getShort(10);
            this.j.g = var1.getShort(12);
            this.j.h = var1.getShort(14);
            this.j.i = var1.getShort(16);
            this.j.j = var1.getShort(18);
            this.j.k = var1.getShort(20);
        } finally {
            this.d.unlock();
        }

    }

    private void b() {
        ModernRoboticsI2cGyro.GyroI2cTransaction var1 = new ModernRoboticsI2cGyro.GyroI2cTransaction();
        this.queueTransaction(var1);
    }

    public void portIsReady(int port) {
        if(this.transactionQueue.isEmpty()) {
            this.b();
        } else {
            ModernRoboticsI2cGyro.GyroI2cTransaction var2 = (ModernRoboticsI2cGyro.GyroI2cTransaction)this.transactionQueue.peek();
            if(var2.a == ModernRoboticsI2cGyro.I2cTransactionState.PENDING_I2C_READ) {
                this.b.readI2cCacheFromModule(this.g);
                var2.a = ModernRoboticsI2cGyro.I2cTransactionState.PENDING_READ_DONE;
            } else {
                if(var2.a == ModernRoboticsI2cGyro.I2cTransactionState.PENDING_I2C_WRITE) {
                    var2 = (ModernRoboticsI2cGyro.GyroI2cTransaction)this.transactionQueue.poll();
                    if(this.transactionQueue.isEmpty()) {
                        return;
                    }

                    var2 = (ModernRoboticsI2cGyro.GyroI2cTransaction)this.transactionQueue.peek();
                } else if(var2.a == ModernRoboticsI2cGyro.I2cTransactionState.PENDING_READ_DONE) {
                    this.a();
                    var2 = (ModernRoboticsI2cGyro.GyroI2cTransaction)this.transactionQueue.poll();
                    if(this.transactionQueue.isEmpty()) {
                        return;
                    }

                    var2 = (ModernRoboticsI2cGyro.GyroI2cTransaction)this.transactionQueue.peek();
                }

                try {
                    if(var2.e) {
                        if(var2.c == 3) {
                            this.j.d = var2.b[0];
                            this.i = ModernRoboticsI2cGyro.MeasurementMode.GYRO_CALIBRATING;
                        }

                        this.b.enableI2cWriteMode(port, this.a, var2.c, var2.d);
                        this.b.copyBufferIntoWriteBuffer(port, var2.b);
                        var2.a = ModernRoboticsI2cGyro.I2cTransactionState.PENDING_I2C_WRITE;
                    } else {
                        this.b.enableI2cReadMode(port, this.a, var2.c, var2.d);
                        var2.a = ModernRoboticsI2cGyro.I2cTransactionState.PENDING_I2C_READ;
                    }

                    this.b.writeI2cCacheToController(port);
                } catch (IllegalArgumentException var4) {
                    RobotLog.e(var4.getMessage());
                }

                if(this.j.d == 0) {
                    this.i = ModernRoboticsI2cGyro.MeasurementMode.GYRO_NORMAL;
                }

            }
        }
    }

    protected void buginf(String s) {
    }

    private class a {
        byte a;
        byte b;
        byte c;
        byte d;
        short e;
        short f;
        short g;
        short h;
        short i;
        short j;
        short k;

        private a() {
        }
    }

    public static enum MeasurementMode {
        GYRO_CALIBRATING,
        GYRO_NORMAL;

        private MeasurementMode() {
        }
    }

    public static enum HeadingMode {
        HEADING_CARTESIAN,
        HEADING_CARDINAL;

        private HeadingMode() {
        }
    }

    public class GyroI2cTransaction {
        ModernRoboticsI2cGyro.I2cTransactionState a;
        byte[] b;
        byte c;
        byte d;
        boolean e;

        public GyroI2cTransaction() {
            this.c = 0;
            this.d = 18;
            this.e = false;
        }

        public GyroI2cTransaction(byte data) {
            this.c = 3;
            this.b = new byte[1];
            this.b[0] = data;
            this.d = (byte)this.b.length;
            this.e = true;
        }

        public boolean isEqual(ModernRoboticsI2cGyro.GyroI2cTransaction transaction) {
            if(this.c != transaction.c) {
                return false;
            } else {
                switch(this.c) {
                case 3:
                case 16:
                    if(Arrays.equals(this.b, transaction.b)) {
                        return true;
                    }

                    return false;
                default:
                    return false;
                }
            }
        }
    }

    protected static enum I2cTransactionState {
        QUEUED,
        PENDING_I2C_READ,
        PENDING_I2C_WRITE,
        PENDING_READ_DONE,
        DONE;

        private I2cTransactionState() {
        }
    }
}
