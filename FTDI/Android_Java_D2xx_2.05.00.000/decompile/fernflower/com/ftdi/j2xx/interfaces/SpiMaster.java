package com.ftdi.j2xx.interfaces;

public interface SpiMaster {
   int init(int var1, int var2, int var3, int var4, byte var5);

   int reset();

   int setLines(int var1);

   int singleWrite(byte[] var1, int var2, int[] var3, boolean var4);

   int singleRead(byte[] var1, int var2, int[] var3, boolean var4);

   int singleReadWrite(byte[] var1, byte[] var2, int var3, int[] var4, boolean var5);

   int multiReadWrite(byte[] var1, byte[] var2, int var3, int var4, int var5, int[] var6);
}
