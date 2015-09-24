package com.qualcomm.robotcore.hardware;

public class DcMotor implements HardwareDevice
{
    protected DcMotorController controller;
    protected DcMotorController.DeviceMode devMode;
    protected Direction direction;
    protected DcMotorController.RunMode mode;
    protected int portNumber;
    
    public DcMotor(final DcMotorController dcMotorController, final int n) {
        this(dcMotorController, n, Direction.FORWARD);
    }
    
    public DcMotor(final DcMotorController controller, final int portNumber, final Direction direction) {
        this.direction = Direction.FORWARD;
        this.controller = null;
        this.portNumber = -1;
        this.mode = DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
        this.devMode = DcMotorController.DeviceMode.WRITE_ONLY;
        this.controller = controller;
        this.portNumber = portNumber;
        this.direction = direction;
    }
    
    @Override
    public void close() {
        this.setPowerFloat();
    }
    
    public DcMotorController.RunMode getChannelMode() {
        return this.controller.getMotorChannelMode(this.portNumber);
    }
    
    @Override
    public String getConnectionInfo() {
        return this.controller.getConnectionInfo() + "; port " + this.portNumber;
    }
    
    public DcMotorController getController() {
        return this.controller;
    }
    
    public int getCurrentPosition() {
        int motorCurrentPosition = this.controller.getMotorCurrentPosition(this.portNumber);
        if (this.direction == Direction.REVERSE) {
            motorCurrentPosition *= -1;
        }
        return motorCurrentPosition;
    }
    
    @Override
    public String getDeviceName() {
        return "DC Motor";
    }
    
    public Direction getDirection() {
        return this.direction;
    }
    
    public int getPortNumber() {
        return this.portNumber;
    }
    
    public double getPower() {
        double motorPower = this.controller.getMotorPower(this.portNumber);
        if (this.direction == Direction.REVERSE && motorPower != 0.0) {
            motorPower *= -1.0;
        }
        return motorPower;
    }
    
    public boolean getPowerFloat() {
        return this.controller.getMotorPowerFloat(this.portNumber);
    }
    
    public int getTargetPosition() {
        int motorTargetPosition = this.controller.getMotorTargetPosition(this.portNumber);
        if (this.direction == Direction.REVERSE) {
            motorTargetPosition *= -1;
        }
        return motorTargetPosition;
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    public boolean isBusy() {
        return this.controller.isBusy(this.portNumber);
    }
    
    public void setChannelMode(final DcMotorController.RunMode mode) {
        this.mode = mode;
        this.controller.setMotorChannelMode(this.portNumber, mode);
    }
    
    public void setDirection(final Direction direction) {
        this.direction = direction;
    }
    
    public void setPower(double abs) {
        if (this.direction == Direction.REVERSE) {
            abs *= -1.0;
        }
        if (this.mode == DcMotorController.RunMode.RUN_TO_POSITION) {
            abs = Math.abs(abs);
        }
        this.controller.setMotorPower(this.portNumber, abs);
    }
    
    public void setPowerFloat() {
        this.controller.setMotorPowerFloat(this.portNumber);
    }
    
    public void setTargetPosition(int n) {
        if (this.direction == Direction.REVERSE) {
            n *= -1;
        }
        this.controller.setMotorTargetPosition(this.portNumber, n);
    }
    
    public enum Direction
    {
        FORWARD, 
        REVERSE;
    }
}
