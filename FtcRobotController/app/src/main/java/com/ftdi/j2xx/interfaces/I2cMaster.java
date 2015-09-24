package com.ftdi.j2xx.interfaces;

public interface I2cMaster {
   int init(int var1);

   int read(int var1, byte[] var2, int var3, int[] var4);

   int reset();

   int write(int var1, byte[] var2, int var3, int[] var4);
}
