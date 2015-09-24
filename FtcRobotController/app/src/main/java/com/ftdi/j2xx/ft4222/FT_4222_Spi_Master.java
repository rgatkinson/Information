package com.ftdi.j2xx.ft4222;

import android.util.Log;
import junit.framework.Assert;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.interfaces.SpiMaster;

public class FT_4222_Spi_Master implements SpiMaster
{
    private FT_4222_Device a;
    private FT_Device b;
    
    public FT_4222_Spi_Master(final FT_4222_Device a) {
        this.a = a;
        this.b = a.mFtDev;
    }
    
    private int a(final FT_Device ft_Device, final byte[] array, final byte[] array2) {
        int n;
        if (ft_Device == null || !ft_Device.isOpen()) {
            n = -1;
        }
        else {
            ft_Device.write(array, array.length);
            n = 0;
            int n2 = 0;
        Label_0109_Outer:
            while (n < array2.length && n2 < 30000) {
                final int queueStatus = ft_Device.getQueueStatus();
            Label_0118_Outer:
                while (true) {
                Label_0180_Outer:
                    while (true) {
                        Label_0158: {
                            Label_0136: {
                                if (queueStatus <= 0) {
                                    break Label_0136;
                                }
                                final byte[] array3 = new byte[queueStatus];
                                final int read = ft_Device.read(array3, queueStatus);
                                if (array3.length != read) {
                                    break Label_0118_Outer;
                                }
                                final boolean b = true;
                                Assert.assertEquals(b, true);
                                final int n3 = 0;
                                if (n3 < array3.length) {
                                    break Label_0158;
                                }
                                n += read;
                                n2 = 0;
                            }
                            final long n4 = 10;
                            try {
                                Thread.sleep(n4);
                                n2 += 10;
                                continue Label_0109_Outer;
                                final boolean b = false;
                                continue Label_0118_Outer;
                                while (true) {
                                    int n3 = 0;
                                    ++n3;
                                    continue Label_0180_Outer;
                                    final byte[] array3;
                                    array2[n + n3] = array3[n3];
                                    continue;
                                }
                            }
                            // iftrue(Label_0180:, n + n3 >= array2.length)
                            catch (InterruptedException ex) {
                                n2 = 30000;
                            }
                        }
                        break;
                    }
                    break;
                }
            }
            if (array2.length != n || n2 > 30000) {
                Log.e("FTDI_Device::", "MultiReadWritePackage timeout!!!!");
                return -1;
            }
        }
        return n;
    }
    
    private int a(final FT_Device ft_Device, final byte[] array, final byte[] array2, final int n) {
        final byte[] array3 = new byte[16384];
        final byte[] array4 = new byte[array3.length];
        final int n2 = n / array3.length;
        final int n3 = n % array3.length;
        int i = 0;
        int n4 = 0;
        int n5 = 0;
        while (i < n2) {
            int n6 = n4;
            for (int j = 0; j < array3.length; ++j) {
                array3[j] = array[n6];
                ++n6;
            }
            if (this.b(ft_Device, array3, array4) <= 0) {
                return -1;
            }
            for (int k = 0; k < array4.length; ++k) {
                array2[n5] = array4[k];
                ++n5;
            }
            ++i;
            n4 = n6;
        }
        if (n3 > 0) {
            final byte[] array5 = new byte[n3];
            final byte[] array6 = new byte[array5.length];
            int n7 = n4;
            for (int l = 0; l < array5.length; ++l) {
                array5[l] = array[n7];
                ++n7;
            }
            final int b = this.b(ft_Device, array5, array6);
            int n8 = 0;
            if (b > 0) {
                while (n8 < array6.length) {
                    array2[n5] = array6[n8];
                    ++n5;
                    ++n8;
                }
                return n5;
            }
            n5 = -1;
        }
        return n5;
    }
    
    private int b(final FT_Device ft_Device, final byte[] array, final byte[] array2) {
        int n = 0;
        Label_0014: {
            if (ft_Device == null || !ft_Device.isOpen()) {
                n = -1;
            }
            else {
                Assert.assertEquals(array.length == array2.length, true);
                if (array.length != ft_Device.write(array, array.length)) {
                    Log.e("FTDI_Device::", "setReadWritePackage Incomplete Write Error!!!");
                    return -1;
                }
            Label_0143_Outer:
                while (true) {
                    n = 0;
                    int n2 = 0;
                Label_0143:
                    while (true) {
                        break Label_0143;
                    Label_0189_Outer:
                        while (true) {
                            final long n3 = 10;
                            int read = 0;
                            while (true) {
                                int n4 = 0;
                                byte[] array3 = null;
                                Label_0112: {
                                    boolean b = false;
                                    Label_0103: {
                                        while (true) {
                                            Label_0065: {
                                                try {
                                                    Thread.sleep(n3);
                                                    n2 += 10;
                                                    if (n < array2.length && n2 < 30000) {
                                                        break Label_0065;
                                                    }
                                                    if (array2.length != n || n2 > 30000) {
                                                        Log.e("FTDI_Device::", "SingleReadWritePackage timeout!!!!");
                                                        return -1;
                                                    }
                                                    break Label_0014;
                                                    b = false;
                                                    break Label_0103;
                                                    while (true) {
                                                        array2[n + n4] = array3[n4];
                                                        Label_0211: {
                                                            ++n4;
                                                        }
                                                        break Label_0112;
                                                        continue Label_0189_Outer;
                                                    }
                                                }
                                                // iftrue(Label_0211:, n + n4 >= array2.length)
                                                catch (InterruptedException ex) {
                                                    n2 = 30000;
                                                    continue Label_0143;
                                                }
                                                break;
                                            }
                                            final int queueStatus = ft_Device.getQueueStatus();
                                            if (queueStatus <= 0) {
                                                continue Label_0143_Outer;
                                            }
                                            array3 = new byte[queueStatus];
                                            read = ft_Device.read(array3, queueStatus);
                                            if (array3.length != read) {
                                                continue Label_0189_Outer;
                                            }
                                            break;
                                        }
                                        b = true;
                                    }
                                    Assert.assertEquals(b, true);
                                    n4 = 0;
                                }
                                if (n4 < array3.length) {
                                    continue;
                                }
                                break;
                            }
                            n += read;
                            n2 = 0;
                        }
                        break;
                    }
                }
            }
        }
        return n;
    }
    
    @Override
    public int init(final int a, final int b, final int c, final int d, final byte e) {
        byte b2 = 1;
        final b mChipStatus = this.a.mChipStatus;
        final a mSpiMasterCfg = this.a.mSpiMasterCfg;
        mSpiMasterCfg.a = a;
        mSpiMasterCfg.b = b;
        mSpiMasterCfg.c = c;
        mSpiMasterCfg.d = d;
        mSpiMasterCfg.e = e;
        if (mSpiMasterCfg.a != b2 && mSpiMasterCfg.a != 2 && mSpiMasterCfg.a != 4) {
            return 6;
        }
        this.a.cleanRxData();
        Label_0131: {
            switch (mChipStatus.a) {
                default: {
                    b2 = 0;
                    break Label_0131;
                }
                case 2: {
                    b2 = 15;
                    break Label_0131;
                }
                case 1: {
                    b2 = 7;
                }
                case 0:
                case 3: {
                    if ((b2 & mSpiMasterCfg.e) == 0x0) {
                        return 6;
                    }
                    mSpiMasterCfg.e &= b2;
                    if (this.b.VendorCmdSet(33, 0x42 | mSpiMasterCfg.a << 8) < 0) {
                        return 4;
                    }
                    if (this.b.VendorCmdSet(33, 0x44 | mSpiMasterCfg.b << 8) < 0) {
                        return 4;
                    }
                    if (this.b.VendorCmdSet(33, 0x45 | mSpiMasterCfg.c << 8) < 0) {
                        return 4;
                    }
                    if (this.b.VendorCmdSet(33, 0x46 | mSpiMasterCfg.d << 8) < 0) {
                        return 4;
                    }
                    if (this.b.VendorCmdSet(33, 67) < 0) {
                        return 4;
                    }
                    if (this.b.VendorCmdSet(33, 0x48 | mSpiMasterCfg.e << 8) < 0) {
                        return 4;
                    }
                    if (this.b.VendorCmdSet(33, 773) < 0) {
                        return 4;
                    }
                    mChipStatus.g = 3;
                    return 0;
                }
            }
        }
    }
    
    @Override
    public int multiReadWrite(final byte[] array, final byte[] array2, final int n, final int n2, final int n3, final int[] array3) {
        final b mChipStatus = this.a.mChipStatus;
        final a mSpiMasterCfg = this.a.mSpiMasterCfg;
        if ((n3 > 0 && array == null) || (n + n2 > 0 && array2 == null) || (n3 > 0 && array3 == null)) {
            return 1009;
        }
        if (mChipStatus.g != 3 || mSpiMasterCfg.a == 1) {
            return 1006;
        }
        if (n > 15) {
            Log.e("FTDI_Device::", "The maxium single write bytes are 15 bytes");
            return 6;
        }
        final byte[] array4 = new byte[n2 + (n + 5)];
        array4[0] = (byte)(0x80 | (n & 0xF));
        array4[1] = (byte)((n2 & 0xFF00) >> 8);
        array4[2] = (byte)(n2 & 0xFF);
        array4[3] = (byte)((n3 & 0xFF00) >> 8);
        array4[4] = (byte)(n3 & 0xFF);
        for (int i = 0; i < n + n2; ++i) {
            array4[i + 5] = array2[i];
        }
        array3[0] = this.a(this.b, array4, array);
        return 0;
    }
    
    @Override
    public int reset() {
        if (this.b.VendorCmdSet(33, 74) < 0) {
            return 4;
        }
        return 0;
    }
    
    public int setDrivingStrength(final int n, final int n2, final int n3) {
        byte b = 3;
        int n4 = 4;
        final b mChipStatus = this.a.mChipStatus;
        if (mChipStatus.g != b && mChipStatus.g != n4) {
            n4 = 1003;
        }
        else {
            final int n5 = n3 | (n << 4 | n2 << 2);
            if (mChipStatus.g != b) {
                b = (byte)n4;
            }
            if (this.b.VendorCmdSet(33, 0xA0 | n5 << 8) >= 0 && this.b.VendorCmdSet(33, 0x5 | b << 8) >= 0) {
                return 0;
            }
        }
        return n4;
    }
    
    @Override
    public int setLines(final int a) {
        int n = 4;
        if (this.a.mChipStatus.g != 3) {
            n = 1003;
        }
        else {
            if (a == 0) {
                return 17;
            }
            if (this.b.VendorCmdSet(33, 0x42 | a << 8) >= 0 && this.b.VendorCmdSet(33, 330) >= 0) {
                this.a.mSpiMasterCfg.a = a;
                return 0;
            }
        }
        return n;
    }
    
    @Override
    public int singleRead(final byte[] array, final int n, final int[] array2, final boolean b) {
        return this.singleReadWrite(array, new byte[array.length], n, array2, b);
    }
    
    @Override
    public int singleReadWrite(final byte[] array, final byte[] array2, final int n, final int[] array3, final boolean b) {
        final b mChipStatus = this.a.mChipStatus;
        final a mSpiMasterCfg = this.a.mSpiMasterCfg;
        int n2;
        if (array2 == null || array == null || array3 == null) {
            n2 = 1009;
        }
        else {
            array3[0] = 0;
            if (mChipStatus.g != 3 || mSpiMasterCfg.a != 1) {
                return 1005;
            }
            if (n == 0) {
                return 6;
            }
            if (n > array2.length || n > array.length) {
                Assert.assertTrue("sizeToTransfer > writeBuffer.length || sizeToTransfer > readBuffer.length", false);
            }
            if (array2.length != array.length || array2.length == 0) {
                Assert.assertTrue("writeBuffer.length != readBuffer.length || writeBuffer.length == 0", false);
            }
            array3[0] = this.a(this.b, array2, array, n);
            n2 = 0;
            if (b) {
                this.b.write(null, 0);
                return 0;
            }
        }
        return n2;
    }
    
    @Override
    public int singleWrite(final byte[] array, final int n, final int[] array2, final boolean b) {
        return this.singleReadWrite(new byte[array.length], array, n, array2, b);
    }
}
