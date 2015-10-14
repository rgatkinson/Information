package com.qualcomm.robotcore.hardware;

public class DcMotor implements HardwareDevice {
    protected DcMotorController controller;
    protected DcMotorController.DeviceMode devMode;
    protected Direction direction;
    protected DcMotorController.RunMode mode;
    protected int portNumber;

    public DcMotor(DcMotorController var1, int var2) {
        this(var1, var2, Direction.FORWARD);
    }

    public DcMotor(DcMotorController var1, int var2, Direction var3) {
        this.direction = Direction.FORWARD;
        this.controller = null;
        this.portNumber = -1;
        this.mode = DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
        this.devMode = DcMotorController.DeviceMode.WRITE_ONLY;
        this.controller = var1;
        this.portNumber = var2;
        this.direction = var3;
    }

    public void close() {
        this.setPowerFloat();
    }

    public DcMotorController.RunMode getChannelMode() {
        return this.controller.getMotorChannelMode(this.portNumber);
    }

    public void setChannelMode(DcMotorController.RunMode var1) {
        this.mode = var1;
        this.controller.setMotorChannelMode(this.portNumber, var1);
    }

    public String getConnectionInfo() {
        return this.controller.getConnectionInfo() + "; port " + this.portNumber;
    }

    public DcMotorController getController() {
        return this.controller;
    }

    public int getCurrentPosition() {
        int var1 = this.controller.getMotorCurrentPosition(this.portNumber);
        if (this.direction == Direction.REVERSE) {
            var1 *= -1;
        }

        return var1;
    }

    public String getDeviceName() {
        return "DC Motor";
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction var1) {
        this.direction = var1;
    }

    public int getPortNumber() {
        return this.portNumber;
    }

    public double getPower() {
        double var1 = this.controller.getMotorPower(this.portNumber);
        if (this.direction == Direction.REVERSE && var1 != 0.0D) {
            var1 *= -1.0D;
        }

        return var1;
    }

    public void setPower(double var1) {
        if (this.direction == Direction.REVERSE) {
            var1 *= -1.0D;
        }

        if (this.mode == DcMotorController.RunMode.RUN_TO_POSITION) {
            var1 = Math.abs(var1);
        }

        this.controller.setMotorPower(this.portNumber, var1);
    }

    public boolean getPowerFloat() {
        return this.controller.getMotorPowerFloat(this.portNumber);
    }

    public int getTargetPosition() {
        int var1 = this.controller.getMotorTargetPosition(this.portNumber);
        if (this.direction == Direction.REVERSE) {
            var1 *= -1;
        }

        return var1;
    }

    public void setTargetPosition(int var1) {
        if (this.direction == Direction.REVERSE) {
            var1 *= -1;
        }

        this.controller.setMotorTargetPosition(this.portNumber, var1);
    }

    public int getVersion() {
        return 1;
    }

    public boolean isBusy() {
        return this.controller.isBusy(this.portNumber);
    }

    public void setPowerFloat() {
        this.controller.setMotorPowerFloat(this.portNumber);
    }

    public enum Direction {
        FORWARD,
        REVERSE;

        static {
            Direction[] var0 = new Direction[]{FORWARD, REVERSE};
        }
    }
}
