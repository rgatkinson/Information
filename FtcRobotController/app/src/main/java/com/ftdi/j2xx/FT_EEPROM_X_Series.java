package com.ftdi.j2xx;

import com.ftdi.j2xx.FT_EEPROM;

public class FT_EEPROM_X_Series extends FT_EEPROM {
   public byte AC_DriveCurrent = 0;
   public boolean AC_SchmittInput = false;
   public boolean AC_SlowSlew = false;
   public byte AD_DriveCurrent = 0;
   public boolean AD_SchmittInput = false;
   public boolean AD_SlowSlew = false;
   public short A_DeviceTypeValue = 0;
   public boolean A_LoadD2XX = false;
   public boolean A_LoadVCP = false;
   public boolean BCDDisableSleep = false;
   public boolean BCDEnable = false;
   public boolean BCDForceCBusPWREN = false;
   public byte CBus0 = 0;
   public byte CBus1 = 0;
   public byte CBus2 = 0;
   public byte CBus3 = 0;
   public byte CBus4 = 0;
   public byte CBus5 = 0;
   public byte CBus6 = 0;
   public boolean FT1248ClockPolarity = false;
   public boolean FT1248FlowControl = false;
   public boolean FT1248LSB = false;
   public int I2CDeviceID = 0;
   public boolean I2CDisableSchmitt = false;
   public int I2CSlaveAddress = 0;
   public boolean InvertCTS = false;
   public boolean InvertDCD = false;
   public boolean InvertDSR = false;
   public boolean InvertDTR = false;
   public boolean InvertRI = false;
   public boolean InvertRTS = false;
   public boolean InvertRXD = false;
   public boolean InvertTXD = false;
   public boolean PowerSaveEnable = false;
   public boolean RS485EchoSuppress = false;

   public static final class CBUS {
   }

   public static final class DRIVE_STRENGTH {
   }
}
