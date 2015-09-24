package com.qualcomm.robotcore.hardware;

public abstract class AccelerationSensor implements HardwareDevice
{
    public abstract Acceleration getAcceleration();
    
    public abstract String status();
    
    @Override
    public String toString() {
        return this.getAcceleration().toString();
    }
    
    public static class Acceleration
    {
        public double x;
        public double y;
        public double z;
        
        public Acceleration() {
            this(0.0, 0.0, 0.0);
        }
        
        public Acceleration(final double x, final double y, final double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        @Override
        public String toString() {
            return String.format("Acceleration - x: %5.2f, y: %5.2f, z: %5.2f", this.x, this.y, this.z);
        }
    }
}
