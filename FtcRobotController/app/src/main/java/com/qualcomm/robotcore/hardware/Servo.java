package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.Range;

public class Servo implements HardwareDevice
{
    public static final double MAX_POSITION = 1.0;
    public static final double MIN_POSITION;
    protected ServoController controller;
    protected Direction direction;
    protected double maxPosition;
    protected double minPosition;
    protected int portNumber;
    
    public Servo(final ServoController servoController, final int n) {
        this(servoController, n, Direction.FORWARD);
    }
    
    public Servo(final ServoController controller, final int portNumber, final Direction direction) {
        this.controller = null;
        this.portNumber = -1;
        this.direction = Direction.FORWARD;
        this.minPosition = 0.0;
        this.maxPosition = 1.0;
        this.direction = direction;
        this.controller = controller;
        this.portNumber = portNumber;
    }
    
    private double a(final double n) {
        return 0.0 + (1.0 - n);
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public String getConnectionInfo() {
        return this.controller.getConnectionInfo() + "; port " + this.portNumber;
    }
    
    public ServoController getController() {
        return this.controller;
    }
    
    @Override
    public String getDeviceName() {
        return "Servo";
    }
    
    public Direction getDirection() {
        return this.direction;
    }
    
    public int getPortNumber() {
        return this.portNumber;
    }
    
    public double getPosition() {
        double n = this.controller.getServoPosition(this.portNumber);
        if (this.direction == Direction.REVERSE) {
            n = this.a(n);
        }
        return Range.clip(Range.scale(n, this.minPosition, this.maxPosition, 0.0, 1.0), 0.0, 1.0);
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    public void scaleRange(final double minPosition, final double maxPosition) throws IllegalArgumentException {
        Range.throwIfRangeIsInvalid(minPosition, 0.0, 1.0);
        Range.throwIfRangeIsInvalid(maxPosition, 0.0, 1.0);
        if (minPosition >= maxPosition) {
            throw new IllegalArgumentException("min must be less than max");
        }
        this.minPosition = minPosition;
        this.maxPosition = maxPosition;
    }
    
    public void setDirection(final Direction direction) {
        this.direction = direction;
    }
    
    public void setPosition(double a) {
        if (this.direction == Direction.REVERSE) {
            a = this.a(a);
        }
        this.controller.setServoPosition(this.portNumber, Range.scale(a, 0.0, 1.0, this.minPosition, this.maxPosition));
    }
    
    public enum Direction
    {
        FORWARD, 
        REVERSE;
    }
}
