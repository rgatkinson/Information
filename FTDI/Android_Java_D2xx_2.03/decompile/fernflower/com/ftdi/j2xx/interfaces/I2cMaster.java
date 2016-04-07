package com.ftdi.j2xx.interfaces;

public interface I2cMaster {
   int init(int var1);

   int reset();

   int read(int var1, byte[] var2, int var3, int[] var4);

   int readEx(int var1, int var2, byte[] var3, int var4, int[] var5);

   int write(int var1, byte[] var2, int var3, int[] var4);

   int writeEx(int var1, int var2, byte[] var3, int var4, int[] var5);

   int getStatus(int var1, byte[] var2);
}
