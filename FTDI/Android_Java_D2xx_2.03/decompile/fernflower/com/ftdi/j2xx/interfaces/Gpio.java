package com.ftdi.j2xx.interfaces;

public interface Gpio {
   int init(int[] var1);

   int read(int var1, boolean[] var2);

   int write(int var1, boolean var2);
}
