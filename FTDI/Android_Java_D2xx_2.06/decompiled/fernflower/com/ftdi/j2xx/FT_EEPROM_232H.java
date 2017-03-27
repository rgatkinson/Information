package com.ftdi.j2xx;

import com.ftdi.j2xx.FT_EEPROM;

public class FT_EEPROM_232H extends FT_EEPROM {
   public boolean AL_SlowSlew = false;
   public boolean AL_SchmittInput = false;
   public byte AL_DriveCurrent = 0;
   public boolean BL_SlowSlew = false;
   public boolean BL_SchmittInput = false;
   public byte BL_DriveCurrent = 0;
   public byte CBus0 = 0;
   public byte CBus1 = 0;
   public byte CBus2 = 0;
   public byte CBus3 = 0;
   public byte CBus4 = 0;
   public byte CBus5 = 0;
   public byte CBus6 = 0;
   public byte CBus7 = 0;
   public byte CBus8 = 0;
   public byte CBus9 = 0;
   public boolean UART = false;
   public boolean FIFO = false;
   public boolean FIFOTarget = false;
   public boolean FastSerial = false;
   public boolean FT1248 = false;
   public boolean FT1248ClockPolarity = false;
   public boolean FT1248LSB = false;
   public boolean FT1248FlowControl = false;
   public boolean PowerSaveEnable = false;
   public boolean LoadVCP = false;
   public boolean LoadD2XX = false;

   public static final class CBUS {
      static final int TRISTATE = 0;
      static final int TXLED = 1;
      static final int RXLED = 2;
      static final int TXRXLED = 3;
      static final int PWREN = 4;
      static final int SLEEP = 5;
      static final int DRIVE_0 = 6;
      static final int DRIVE_1 = 7;
      static final int GPIO_MODE = 8;
      static final int TXDEN = 9;
      static final int CLK30MHz = 10;
      static final int CLK15MHz = 11;
      static final int CLK7_5MHz = 12;
   }

   public static final class DRIVE_STRENGTH {
      static final byte DRIVE_4mA = 0;
      static final byte DRIVE_8mA = 1;
      static final byte DRIVE_12mA = 2;
      static final byte DRIVE_16mA = 3;
   }
}
