package com.ftdi.j2xx.interfaces;

public interface I2cSlave {
   int init();

   int reset();

   int getAddress(int[] var1);

   int setAddress(int var1);

   int read(byte[] var1, int var2, int[] var3);

   int write(byte[] var1, int var2, int[] var3);
}
