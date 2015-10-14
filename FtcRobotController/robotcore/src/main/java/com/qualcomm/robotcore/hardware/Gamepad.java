//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.robotcore.hardware;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.Set;

public class Gamepad implements RobocolParsable {
    public static final int ID_UNASSOCIATED = -1;
    private static Set<Integer> d = new HashSet();
    private static Set<Gamepad.a> e = null;
    private final GamepadCallback c;
    public float left_stick_x;
    public float left_stick_y;
    public float right_stick_x;
    public float right_stick_y;
    public boolean dpad_up;
    public boolean dpad_down;
    public boolean dpad_left;
    public boolean dpad_right;
    public boolean a;
    public boolean b;
    public boolean x;
    public boolean y;
    public boolean guide;
    public boolean start;
    public boolean back;
    public boolean left_bumper;
    public boolean right_bumper;
    public boolean left_stick_button;
    public boolean right_stick_button;
    public float left_trigger;
    public float right_trigger;
    public byte user;
    public int id;
    public long timestamp;
    protected float dpadThreshold;
    protected float joystickDeadzone;

    public Gamepad() {
        this(null);
    }

    public Gamepad(GamepadCallback callback) {
        this.left_stick_x = 0.0F;
        this.left_stick_y = 0.0F;
        this.right_stick_x = 0.0F;
        this.right_stick_y = 0.0F;
        this.dpad_up = false;
        this.dpad_down = false;
        this.dpad_left = false;
        this.dpad_right = false;
        this.a = false;
        this.b = false;
        this.x = false;
        this.y = false;
        this.guide = false;
        this.start = false;
        this.back = false;
        this.left_bumper = false;
        this.right_bumper = false;
        this.left_stick_button = false;
        this.right_stick_button = false;
        this.left_trigger = 0.0F;
        this.right_trigger = 0.0F;
        this.user = -1;
        this.id = -1;
        this.timestamp = 0L;
        this.dpadThreshold = 0.2F;
        this.joystickDeadzone = 0.2F;
        this.c = callback;
    }

    public static void enableWhitelistFilter(int vendorId, int productId) {
        if (e == null) {
            e = new HashSet();
        }

        e.add(new a(vendorId, productId));
    }

    public static void clearWhitelistFilter() {
        e = null;
    }

    @TargetApi(19)
    public static synchronized boolean isGamepadDevice(int deviceId) {
        if (d.contains(Integer.valueOf(deviceId))) {
            return true;
        } else {
            d = new HashSet();
            int[] var1 = InputDevice.getDeviceIds();
            int[] var2 = var1;
            int var3 = var1.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                int var5 = var2[var4];
                InputDevice var6 = InputDevice.getDevice(var5);
                int var7 = var6.getSources();
                if ((var7 & 1025) == 1025 || (var7 & 16777232) == 16777232) {
                    if (VERSION.SDK_INT >= 19) {
                        if (e == null || e.contains(new a(var6.getVendorId(), var6.getProductId()))) {
                            d.add(Integer.valueOf(var5));
                        }
                    } else {
                        d.add(Integer.valueOf(var5));
                    }
                }
            }

            return d.contains(Integer.valueOf(deviceId));
        }
    }

    public void setJoystickDeadzone(float deadzone) {
        if (deadzone >= 0.0F && deadzone <= 1.0F) {
            this.joystickDeadzone = deadzone;
        } else {
            throw new IllegalArgumentException("deadzone cannot be greater than max joystick value");
        }
    }

    public void update(MotionEvent event) {
        this.id = event.getDeviceId();
        this.timestamp = event.getEventTime();
        this.left_stick_x = this.cleanMotionValues(event.getAxisValue(0));
        this.left_stick_y = this.cleanMotionValues(event.getAxisValue(1));
        this.right_stick_x = this.cleanMotionValues(event.getAxisValue(11));
        this.right_stick_y = this.cleanMotionValues(event.getAxisValue(14));
        this.left_trigger = event.getAxisValue(17);
        this.right_trigger = event.getAxisValue(18);
        this.dpad_down = event.getAxisValue(16) > this.dpadThreshold;
        this.dpad_up = event.getAxisValue(16) < -this.dpadThreshold;
        this.dpad_right = event.getAxisValue(15) > this.dpadThreshold;
        this.dpad_left = event.getAxisValue(15) < -this.dpadThreshold;
        this.callCallback();
    }

    public void update(KeyEvent event) {
        this.id = event.getDeviceId();
        this.timestamp = event.getEventTime();
        int var2 = event.getKeyCode();
        if (var2 == 19) {
            this.dpad_up = this.pressed(event);
        } else if (var2 == 20) {
            this.dpad_down = this.pressed(event);
        } else if (var2 == 22) {
            this.dpad_right = this.pressed(event);
        } else if (var2 == 21) {
            this.dpad_left = this.pressed(event);
        } else if (var2 == 96) {
            this.a = this.pressed(event);
        } else if (var2 == 97) {
            this.b = this.pressed(event);
        } else if (var2 == 99) {
            this.x = this.pressed(event);
        } else if (var2 == 100) {
            this.y = this.pressed(event);
        } else if (var2 == 110) {
            this.guide = this.pressed(event);
        } else if (var2 == 108) {
            this.start = this.pressed(event);
        } else if (var2 == 4) {
            this.back = this.pressed(event);
        } else if (var2 == 103) {
            this.right_bumper = this.pressed(event);
        } else if (var2 == 102) {
            this.left_bumper = this.pressed(event);
        } else if (var2 == 106) {
            this.left_stick_button = this.pressed(event);
        } else if (var2 == 107) {
            this.right_stick_button = this.pressed(event);
        }

        this.callCallback();
    }

    public MsgType getRobocolMsgType() {
        return MsgType.GAMEPAD;
    }

    public byte[] toByteArray() throws RobotCoreException {
        ByteBuffer var1 = ByteBuffer.allocate(45);

        try {
            byte var2 = 0;
            var1.put(this.getRobocolMsgType().asByte());
            var1.putShort((short) 42);
            var1.put((byte) 2);
            var1.putInt(this.id);
            var1.putLong(this.timestamp).array();
            var1.putFloat(this.left_stick_x).array();
            var1.putFloat(this.left_stick_y).array();
            var1.putFloat(this.right_stick_x).array();
            var1.putFloat(this.right_stick_y).array();
            var1.putFloat(this.left_trigger).array();
            var1.putFloat(this.right_trigger).array();
            int var4 = (var2 << 1) + (this.left_stick_button ? 1 : 0);
            var4 = (var4 << 1) + (this.right_stick_button ? 1 : 0);
            var4 = (var4 << 1) + (this.dpad_up ? 1 : 0);
            var4 = (var4 << 1) + (this.dpad_down ? 1 : 0);
            var4 = (var4 << 1) + (this.dpad_left ? 1 : 0);
            var4 = (var4 << 1) + (this.dpad_right ? 1 : 0);
            var4 = (var4 << 1) + (this.a ? 1 : 0);
            var4 = (var4 << 1) + (this.b ? 1 : 0);
            var4 = (var4 << 1) + (this.x ? 1 : 0);
            var4 = (var4 << 1) + (this.y ? 1 : 0);
            var4 = (var4 << 1) + (this.guide ? 1 : 0);
            var4 = (var4 << 1) + (this.start ? 1 : 0);
            var4 = (var4 << 1) + (this.back ? 1 : 0);
            var4 = (var4 << 1) + (this.left_bumper ? 1 : 0);
            var4 = (var4 << 1) + (this.right_bumper ? 1 : 0);
            var1.putInt(var4);
            var1.put(this.user);
        } catch (BufferOverflowException var3) {
            RobotLog.logStacktrace(var3);
        }

        return var1.array();
    }

    public void fromByteArray(byte[] byteArray) throws RobotCoreException {
        if (byteArray.length < 45) {
            throw new RobotCoreException("Expected buffer of at least 45 bytes, received " + byteArray.length);
        } else {
            ByteBuffer var2 = ByteBuffer.wrap(byteArray, 3, 42);
            boolean var3 = false;
            byte var4 = var2.get();
            if (var4 >= 1) {
                this.id = var2.getInt();
                this.timestamp = var2.getLong();
                this.left_stick_x = var2.getFloat();
                this.left_stick_y = var2.getFloat();
                this.right_stick_x = var2.getFloat();
                this.right_stick_y = var2.getFloat();
                this.left_trigger = var2.getFloat();
                this.right_trigger = var2.getFloat();
                int var5 = var2.getInt();
                this.left_stick_button = (var5 & 16384) != 0;
                this.right_stick_button = (var5 & 8192) != 0;
                this.dpad_up = (var5 & 4096) != 0;
                this.dpad_down = (var5 & 2048) != 0;
                this.dpad_left = (var5 & 1024) != 0;
                this.dpad_right = (var5 & 512) != 0;
                this.a = (var5 & 256) != 0;
                this.b = (var5 & 128) != 0;
                this.x = (var5 & 64) != 0;
                this.y = (var5 & 32) != 0;
                this.guide = (var5 & 16) != 0;
                this.start = (var5 & 8) != 0;
                this.back = (var5 & 4) != 0;
                this.left_bumper = (var5 & 2) != 0;
                this.right_bumper = (var5 & 1) != 0;
            }

            if (var4 >= 2) {
                this.user = var2.get();
            }

            this.callCallback();
        }
    }

    public boolean atRest() {
        return this.left_stick_x == 0.0F && this.left_stick_y == 0.0F && this.right_stick_x == 0.0F && this.right_stick_y == 0.0F && this.left_trigger == 0.0F && this.right_trigger == 0.0F;
    }

    public String type() {
        return "Standard";
    }

    public String toString() {
        String var1 = new String();
        if (this.dpad_up) {
            var1 = var1 + "dpad_up ";
        }

        if (this.dpad_down) {
            var1 = var1 + "dpad_down ";
        }

        if (this.dpad_left) {
            var1 = var1 + "dpad_left ";
        }

        if (this.dpad_right) {
            var1 = var1 + "dpad_right ";
        }

        if (this.a) {
            var1 = var1 + "a ";
        }

        if (this.b) {
            var1 = var1 + "b ";
        }

        if (this.x) {
            var1 = var1 + "x ";
        }

        if (this.y) {
            var1 = var1 + "y ";
        }

        if (this.guide) {
            var1 = var1 + "guide ";
        }

        if (this.start) {
            var1 = var1 + "start ";
        }

        if (this.back) {
            var1 = var1 + "back ";
        }

        if (this.left_bumper) {
            var1 = var1 + "left_bumper ";
        }

        if (this.right_bumper) {
            var1 = var1 + "right_bumper ";
        }

        if (this.left_stick_button) {
            var1 = var1 + "left stick button ";
        }

        if (this.right_stick_button) {
            var1 = var1 + "right stick button ";
        }

        return String.format("ID: %2d user: %2d lx: % 1.2f ly: % 1.2f rx: % 1.2f ry: % 1.2f lt: %1.2f rt: %1.2f %s", Integer.valueOf(this.id), Byte.valueOf(this.user), Float.valueOf(this.left_stick_x), Float.valueOf(this.left_stick_y), Float.valueOf(this.right_stick_x), Float.valueOf(this.right_stick_y), Float.valueOf(this.left_trigger), Float.valueOf(this.right_trigger), var1);
    }

    protected float cleanMotionValues(float number) {
        if (number < this.joystickDeadzone && number > -this.joystickDeadzone) {
            return 0.0F;
        } else if (number > 1.0F) {
            return 1.0F;
        } else if (number < -1.0F) {
            return -1.0F;
        } else {
            if (number < 0.0F) {
                Range.scale((double) number, (double) this.joystickDeadzone, 1.0D, 0.0D, 1.0D);
            }

            if (number > 0.0F) {
                Range.scale((double) number, (double) (-this.joystickDeadzone), -1.0D, 0.0D, -1.0D);
            }

            return number;
        }
    }

    protected boolean pressed(KeyEvent event) {
        return event.getAction() == 0;
    }

    protected void callCallback() {
        if (this.c != null) {
            this.c.gamepadChanged(this);
        }

    }

    public interface GamepadCallback {
        void gamepadChanged(Gamepad var1);
    }

    private static class a extends SimpleEntry<Integer, Integer> {
        public a(int var1, int var2) {
            super(Integer.valueOf(var1), Integer.valueOf(var2));
        }
    }
}
