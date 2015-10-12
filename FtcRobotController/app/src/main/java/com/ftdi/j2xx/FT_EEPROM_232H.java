package com.ftdi.j2xx;

import com.ftdi.j2xx.FT_EEPROM;

public class FT_EEPROM_232H extends FT_EEPROM {
   public byte AL_DriveCurrent = 0;
   public boolean AL_SchmittInput = false;
   public boolean AL_SlowSlew = false;
   public byte BL_DriveCurrent = 0;
   public boolean BL_SchmittInput = false;
   public boolean BL_SlowSlew = false;
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
   public boolean FIFO = false;
   public boolean FIFOTarget = false;
   public boolean FT1248 = false;
   public boolean FT1248ClockPolarity = false;
   public boolean FT1248FlowControl = false;
   public boolean FT1248LSB = false;
   public boolean FastSerial = false;
   public boolean LoadD2XX = false;
   public boolean LoadVCP = false;
   public boolean PowerSaveEnable = false;
   public boolean UART = false;

   public static final class CBUS {
   }

   public static final class DRIVE_STRENGTH {
   }
}
