package com.qualcomm.robotcore.hardware.logitech;

import android.os.Build.VERSION;
import android.view.MotionEvent;

import com.qualcomm.robotcore.hardware.Gamepad;

public class LogitechGamepadF310 extends Gamepad {
    public LogitechGamepadF310() {
        this(null);
    }

    public LogitechGamepadF310(GamepadCallback var1) {
        super(var1);
        this.joystickDeadzone = 0.06F;
    }

    private void a(MotionEvent var1) {
        byte var2 = 1;
        this.left_stick_x = this.cleanMotionValues(var1.getAxisValue(0));
        this.left_stick_y = this.cleanMotionValues(var1.getAxisValue(var2));
        this.right_stick_x = this.cleanMotionValues(var1.getAxisValue(11));
        this.right_stick_y = this.cleanMotionValues(var1.getAxisValue(14));
        this.left_trigger = var1.getAxisValue(23);
        this.right_trigger = var1.getAxisValue(22);
        byte var3;
        if (var1.getAxisValue(16) > this.dpadThreshold) {
            var3 = var2;
        } else {
            var3 = 0;
        }

        this.dpad_down = var3 != 0;
        byte var4;
        if (var1.getAxisValue(16) < -this.dpadThreshold) {
            var4 = var2;
        } else {
            var4 = 0;
        }

        this.dpad_up = var4 != 0;
        byte var5;
        if (var1.getAxisValue(15) > this.dpadThreshold) {
            var5 = var2;
        } else {
            var5 = 0;
        }

        this.dpad_right = var5 != 0;
        if (var1.getAxisValue(15) >= -this.dpadThreshold) {
            var2 = 0;
        }

        this.dpad_left = var2 != 0;
        this.callCallback();
    }

    public String type() {
        return "F310";
    }

    public void update(MotionEvent var1) {
        byte var2 = 1;
        this.id = var1.getDeviceId();
        this.timestamp = var1.getEventTime();
        if (VERSION.RELEASE.startsWith("5")) {
            this.a(var1);
        } else {
            this.left_stick_x = this.cleanMotionValues(var1.getAxisValue(0));
            this.left_stick_y = this.cleanMotionValues(var1.getAxisValue(var2));
            this.right_stick_x = this.cleanMotionValues(var1.getAxisValue(12));
            this.right_stick_y = this.cleanMotionValues(var1.getAxisValue(13));
            this.left_trigger = (1.0F + var1.getAxisValue(11)) / 2.0F;
            this.right_trigger = (1.0F + var1.getAxisValue(14)) / 2.0F;
            byte var3;
            if (var1.getAxisValue(16) > this.dpadThreshold) {
                var3 = var2;
            } else {
                var3 = 0;
            }

            this.dpad_down = var3 != 0;
            byte var4;
            if (var1.getAxisValue(16) < -this.dpadThreshold) {
                var4 = var2;
            } else {
                var4 = 0;
            }

            this.dpad_up = var4 != 0;
            byte var5;
            if (var1.getAxisValue(15) > this.dpadThreshold) {
                var5 = var2;
            } else {
                var5 = 0;
            }

            this.dpad_right = var5 != 0;
            if (var1.getAxisValue(15) >= -this.dpadThreshold) {
                var2 = 0;
            }

            this.dpad_left = var2 != 0;
            this.callCallback();
        }
    }
}
