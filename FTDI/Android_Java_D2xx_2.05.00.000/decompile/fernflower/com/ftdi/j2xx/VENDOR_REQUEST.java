package com.ftdi.j2xx;

class VENDOR_REQUEST {
   static final int RESET = 0;
   static final int MODEM_CTRL = 1;
   static final int SET_FLOW_CONTROL = 2;
   static final int SET_BAUD_RATE = 3;
   static final int SET_DATA = 4;
   static final int GET_MODEM_STATUS = 5;
   static final int SET_EVENT_CHAR = 6;
   static final int SET_ERROR_CHAR = 7;
   static final int SET_LATENCY_TIMER = 9;
   static final int GET_LATENCY_TIMER = 10;
   static final int SET_BIT_MODE = 11;
   static final int GET_BIT_MODE = 12;
   static final int READ_EE = 144;
   static final int WRITE_EE = 145;
   static final int ERASE_EE = 146;
}
