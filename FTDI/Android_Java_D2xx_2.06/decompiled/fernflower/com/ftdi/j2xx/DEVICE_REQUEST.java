package com.ftdi.j2xx;

class DEVICE_REQUEST {
   static final byte GET_STATUS = 0;
   static final byte CLEAR_FEATURE = 1;
   static final byte SET_FEATURE = 3;
   static final byte SET_ADDRESS = 5;
   static final byte GET_DESCRIPTOR = 6;
   static final byte SET_DESCRIPTOR = 7;
   static final byte GET_CONFIGURATION = 8;
   static final byte SET_CONFIGURATION = 9;
}
