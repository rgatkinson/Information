package com.ftdi.j2xx.interfaces;

public interface SpiSlave {
   int getRxStatus(int[] var1);

   int init();

   int read(byte[] var1, int var2, int[] var3);

   int reset();

   int write(byte[] var1, int var2, int[] var3);
}
