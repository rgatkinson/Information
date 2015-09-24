package com.qualcomm.robotcore.hardware;

public abstract class IrSeekerSensor implements HardwareDevice
{
    public static final int MAX_NEW_I2C_ADDRESS = 126;
    public static final int MIN_NEW_I2C_ADDRESS = 16;
    
    public static void throwIfModernRoboticsI2cAddressIsInvalid(final int n) {
        if (n < 16 || n > 126) {
            throw new IllegalArgumentException(String.format("New I2C address %d is invalid; valid range is: %d..%d", n, 16, 126));
        }
        if (n % 2 != 0) {
            throw new IllegalArgumentException(String.format("New I2C address %d is invalid; the address must be even.", n));
        }
    }
    
    public abstract double getAngle();
    
    public abstract int getI2cAddress();
    
    public abstract IrSeekerIndividualSensor[] getIndividualSensors();
    
    public abstract Mode getMode();
    
    public abstract double getSignalDetectedThreshold();
    
    public abstract double getStrength();
    
    public abstract void setI2cAddress(final int p0);
    
    public abstract void setMode(final Mode p0);
    
    public abstract void setSignalDetectedThreshold(final double p0);
    
    public abstract boolean signalDetected();
    
    @Override
    public String toString() {
        if (this.signalDetected()) {
            return String.format("IR Seeker: %3.0f%% signal at %6.1f degrees", 100.0 * this.getStrength(), this.getAngle());
        }
        return "IR Seeker:  --% signal at  ---.- degrees";
    }
    
    public static class IrSeekerIndividualSensor
    {
        private double a;
        private double b;
        
        public IrSeekerIndividualSensor() {
            this(0.0, 0.0);
        }
        
        public IrSeekerIndividualSensor(final double a, final double b) {
            this.a = 0.0;
            this.b = 0.0;
            this.a = a;
            this.b = b;
        }
        
        public double getSensorAngle() {
            return this.a;
        }
        
        public double getSensorStrength() {
            return this.b;
        }
        
        @Override
        public String toString() {
            return String.format("IR Sensor: %3.1f degrees at %3.1f%% power", this.a, 100.0 * this.b);
        }
    }
    
    public enum Mode
    {
        MODE_1200HZ, 
        MODE_600HZ;
    }
}
