package com.qualcomm.robotcore.hardware.logitech;

import android.os.Build$VERSION;
import android.view.MotionEvent;
import com.qualcomm.robotcore.hardware.Gamepad;

public class LogitechGamepadF310 extends Gamepad
{
    public LogitechGamepadF310() {
        this(null);
    }
    
    public LogitechGamepadF310(final GamepadCallback gamepadCallback) {
        super(gamepadCallback);
        this.joystickDeadzone = 0.06f;
    }
    
    private void a(final MotionEvent motionEvent) {
        int dpad_left = 1;
        this.left_stick_x = this.cleanMotionValues(motionEvent.getAxisValue(0));
        this.left_stick_y = this.cleanMotionValues(motionEvent.getAxisValue(dpad_left));
        this.right_stick_x = this.cleanMotionValues(motionEvent.getAxisValue(11));
        this.right_stick_y = this.cleanMotionValues(motionEvent.getAxisValue(14));
        this.left_trigger = motionEvent.getAxisValue(23);
        this.right_trigger = motionEvent.getAxisValue(22);
        this.dpad_down = (motionEvent.getAxisValue(16) > this.dpadThreshold && dpad_left);
        this.dpad_up = (motionEvent.getAxisValue(16) < -this.dpadThreshold && dpad_left);
        this.dpad_right = (motionEvent.getAxisValue(15) > this.dpadThreshold && dpad_left);
        if (motionEvent.getAxisValue(15) >= -this.dpadThreshold) {
            dpad_left = 0;
        }
        this.dpad_left = (dpad_left != 0);
        this.callCallback();
    }
    
    @Override
    public String type() {
        return "F310";
    }
    
    @Override
    public void update(final MotionEvent motionEvent) {
        int dpad_left = 1;
        this.id = motionEvent.getDeviceId();
        this.timestamp = motionEvent.getEventTime();
        if (Build$VERSION.RELEASE.startsWith("5")) {
            this.a(motionEvent);
            return;
        }
        this.left_stick_x = this.cleanMotionValues(motionEvent.getAxisValue(0));
        this.left_stick_y = this.cleanMotionValues(motionEvent.getAxisValue(dpad_left));
        this.right_stick_x = this.cleanMotionValues(motionEvent.getAxisValue(12));
        this.right_stick_y = this.cleanMotionValues(motionEvent.getAxisValue(13));
        this.left_trigger = (1.0f + motionEvent.getAxisValue(11)) / 2.0f;
        this.right_trigger = (1.0f + motionEvent.getAxisValue(14)) / 2.0f;
        this.dpad_down = (motionEvent.getAxisValue(16) > this.dpadThreshold && dpad_left);
        this.dpad_up = (motionEvent.getAxisValue(16) < -this.dpadThreshold && dpad_left);
        this.dpad_right = (motionEvent.getAxisValue(15) > this.dpadThreshold && dpad_left);
        if (motionEvent.getAxisValue(15) >= -this.dpadThreshold) {
            dpad_left = 0;
        }
        this.dpad_left = (dpad_left != 0);
        this.callCallback();
    }
}
