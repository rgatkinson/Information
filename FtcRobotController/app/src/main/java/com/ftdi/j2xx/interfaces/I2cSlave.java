package com.ftdi.j2xx.interfaces;

public interface I2cSlave {
   int getAddress(int[] var1);

   int init();

   int read(byte[] var1, int var2, int[] var3);

   int reset();

   int setAddress(int var1);

   int write(byte[] var1, int var2, int[] var3);
}
