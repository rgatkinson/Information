package com.ftdi.j2xx;

public class FT_EEPROM
{
    public short DeviceType;
    public String Manufacturer;
    public short MaxPower;
    public String Product;
    public short ProductId;
    public boolean PullDownEnable;
    public boolean RemoteWakeup;
    public boolean SelfPowered;
    public boolean SerNumEnable;
    public String SerialNumber;
    public short VendorId;
    
    public FT_EEPROM() {
        this.DeviceType = 0;
        this.Manufacturer = "FTDI";
        this.Product = "USB <-> Serial Converter";
        this.SerialNumber = "FT123456";
        this.VendorId = 1027;
        this.ProductId = 24577;
        this.SerNumEnable = true;
        this.MaxPower = 90;
        this.SelfPowered = false;
        this.RemoteWakeup = false;
        this.PullDownEnable = false;
    }
}
