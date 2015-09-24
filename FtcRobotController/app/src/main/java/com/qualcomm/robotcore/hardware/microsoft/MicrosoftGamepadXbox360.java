package com.qualcomm.robotcore.hardware.microsoft;

import com.qualcomm.robotcore.hardware.Gamepad;

public class MicrosoftGamepadXbox360 extends Gamepad
{
    public MicrosoftGamepadXbox360() {
        this(null);
    }
    
    public MicrosoftGamepadXbox360(final GamepadCallback gamepadCallback) {
        super(gamepadCallback);
        this.joystickDeadzone = 0.15f;
    }
    
    @Override
    public String type() {
        return "Xbox 360";
    }
}
