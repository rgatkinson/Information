package com.ftdi.j2xx.interfaces;

public interface SpiSlave {
   int init();

   int getRxStatus(int[] var1);

   int read(byte[] var1, int var2, int[] var3);

   int write(byte[] var1, int var2, int[] var3);

   int reset();
}
