package com.qualcomm.robotcore.hardware;

import java.util.AbstractMap;
import android.view.MotionEvent;
import java.nio.BufferOverflowException;
import com.qualcomm.robotcore.util.RobotLog;
import android.view.KeyEvent;
import java.nio.ByteBuffer;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.Range;
import android.annotation.TargetApi;
import android.os.Build$VERSION;
import android.view.InputDevice;
import java.util.HashSet;
import java.util.Set;
import com.qualcomm.robotcore.robocol.RobocolParsable;

public class Gamepad implements RobocolParsable
{
    public static final int ID_UNASSOCIATED = -1;
    private static Set<Integer> d;
    private static Set<a> e;
    public boolean a;
    public boolean b;
    public boolean back;
    private final GamepadCallback c;
    protected float dpadThreshold;
    public boolean dpad_down;
    public boolean dpad_left;
    public boolean dpad_right;
    public boolean dpad_up;
    public boolean guide;
    public int id;
    protected float joystickDeadzone;
    public boolean left_bumper;
    public boolean left_stick_button;
    public float left_stick_x;
    public float left_stick_y;
    public float left_trigger;
    public boolean right_bumper;
    public boolean right_stick_button;
    public float right_stick_x;
    public float right_stick_y;
    public float right_trigger;
    public boolean start;
    public long timestamp;
    public byte user;
    public boolean x;
    public boolean y;
    
    static {
        Gamepad.d = new HashSet<Integer>();
        Gamepad.e = null;
    }
    
    public Gamepad() {
        this(null);
    }
    
    public Gamepad(final GamepadCallback c) {
        this.left_stick_x = 0.0f;
        this.left_stick_y = 0.0f;
        this.right_stick_x = 0.0f;
        this.right_stick_y = 0.0f;
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
        this.left_trigger = 0.0f;
        this.right_trigger = 0.0f;
        this.user = -1;
        this.id = -1;
        this.timestamp = 0L;
        this.dpadThreshold = 0.2f;
        this.joystickDeadzone = 0.2f;
        this.c = c;
    }
    
    public static void clearWhitelistFilter() {
        Gamepad.e = null;
    }
    
    public static void enableWhitelistFilter(final int n, final int n2) {
        if (Gamepad.e == null) {
            Gamepad.e = new HashSet<a>();
        }
        Gamepad.e.add(new a(n, n2));
    }
    
    @TargetApi(19)
    public static boolean isGamepadDevice(final int n) {
        while (true) {
            boolean b = true;
            while (true) {
                int n2 = 0;
                Label_0206: {
                    synchronized (Gamepad.class) {
                        if (Gamepad.d.contains(n)) {
                            return b;
                        }
                        Gamepad.d = new HashSet<Integer>();
                        final int[] deviceIds = InputDevice.getDeviceIds();
                        final int length = deviceIds.length;
                        n2 = 0;
                        if (n2 < length) {
                            final int n3 = deviceIds[n2];
                            final InputDevice device = InputDevice.getDevice(n3);
                            final int sources = device.getSources();
                            if ((sources & 0x401) != 0x401 && (sources & 0x1000010) != 0x1000010) {
                                break Label_0206;
                            }
                            if (Build$VERSION.SDK_INT < 19) {
                                Gamepad.d.add(n3);
                                break Label_0206;
                            }
                            if (Gamepad.e == null || Gamepad.e.contains(new a(device.getVendorId(), device.getProductId()))) {
                                Gamepad.d.add(n3);
                            }
                            break Label_0206;
                        }
                    }
                    if (!Gamepad.d.contains(n)) {
                        b = false;
                        return b;
                    }
                    return b;
                }
                ++n2;
                continue;
            }
        }
    }
    
    public boolean atRest() {
        return this.left_stick_x == 0.0f && this.left_stick_y == 0.0f && this.right_stick_x == 0.0f && this.right_stick_y == 0.0f && this.left_trigger == 0.0f && this.right_trigger == 0.0f;
    }
    
    protected void callCallback() {
        if (this.c != null) {
            this.c.gamepadChanged(this);
        }
    }
    
    protected float cleanMotionValues(float n) {
        if (n < this.joystickDeadzone && n > -this.joystickDeadzone) {
            n = 0.0f;
        }
        else {
            if (n > 1.0f) {
                return 1.0f;
            }
            if (n < -1.0f) {
                return -1.0f;
            }
            if (n < 0.0f) {
                Range.scale(n, this.joystickDeadzone, 1.0, 0.0, 1.0);
            }
            if (n > 0.0f) {
                Range.scale(n, -this.joystickDeadzone, -1.0, 0.0, -1.0);
                return n;
            }
        }
        return n;
    }
    
    @Override
    public void fromByteArray(final byte[] array) throws RobotCoreException {
        byte right_bumper = 1;
        if (array.length < 45) {
            throw new RobotCoreException("Expected buffer of at least 45 bytes, received " + array.length);
        }
        final ByteBuffer wrap = ByteBuffer.wrap(array, 3, 42);
        final byte value = wrap.get();
        if (value >= right_bumper) {
            this.id = wrap.getInt();
            this.timestamp = wrap.getLong();
            this.left_stick_x = wrap.getFloat();
            this.left_stick_y = wrap.getFloat();
            this.right_stick_x = wrap.getFloat();
            this.right_stick_y = wrap.getFloat();
            this.left_trigger = wrap.getFloat();
            this.right_trigger = wrap.getFloat();
            final int int1 = wrap.getInt();
            boolean left_stick_button;
            if ((int1 & 0x4000) != 0x0) {
                left_stick_button = (right_bumper != 0);
            }
            else {
                left_stick_button = false;
            }
            this.left_stick_button = left_stick_button;
            boolean right_stick_button;
            if ((int1 & 0x2000) != 0x0) {
                right_stick_button = (right_bumper != 0);
            }
            else {
                right_stick_button = false;
            }
            this.right_stick_button = right_stick_button;
            boolean dpad_up;
            if ((int1 & 0x1000) != 0x0) {
                dpad_up = (right_bumper != 0);
            }
            else {
                dpad_up = false;
            }
            this.dpad_up = dpad_up;
            boolean dpad_down;
            if ((int1 & 0x800) != 0x0) {
                dpad_down = (right_bumper != 0);
            }
            else {
                dpad_down = false;
            }
            this.dpad_down = dpad_down;
            boolean dpad_left;
            if ((int1 & 0x400) != 0x0) {
                dpad_left = (right_bumper != 0);
            }
            else {
                dpad_left = false;
            }
            this.dpad_left = dpad_left;
            boolean dpad_right;
            if ((int1 & 0x200) != 0x0) {
                dpad_right = (right_bumper != 0);
            }
            else {
                dpad_right = false;
            }
            this.dpad_right = dpad_right;
            boolean a;
            if ((int1 & 0x100) != 0x0) {
                a = (right_bumper != 0);
            }
            else {
                a = false;
            }
            this.a = a;
            boolean b;
            if ((int1 & 0x80) != 0x0) {
                b = (right_bumper != 0);
            }
            else {
                b = false;
            }
            this.b = b;
            boolean x;
            if ((int1 & 0x40) != 0x0) {
                x = (right_bumper != 0);
            }
            else {
                x = false;
            }
            this.x = x;
            boolean y;
            if ((int1 & 0x20) != 0x0) {
                y = (right_bumper != 0);
            }
            else {
                y = false;
            }
            this.y = y;
            boolean guide;
            if ((int1 & 0x10) != 0x0) {
                guide = (right_bumper != 0);
            }
            else {
                guide = false;
            }
            this.guide = guide;
            boolean start;
            if ((int1 & 0x8) != 0x0) {
                start = (right_bumper != 0);
            }
            else {
                start = false;
            }
            this.start = start;
            boolean back;
            if ((int1 & 0x4) != 0x0) {
                back = (right_bumper != 0);
            }
            else {
                back = false;
            }
            this.back = back;
            boolean left_bumper;
            if ((int1 & 0x2) != 0x0) {
                left_bumper = (right_bumper != 0);
            }
            else {
                left_bumper = false;
            }
            this.left_bumper = left_bumper;
            if ((int1 & 0x1) == 0x0) {
                right_bumper = 0;
            }
            this.right_bumper = (right_bumper != 0);
        }
        if (value >= 2) {
            this.user = wrap.get();
        }
        this.callCallback();
    }
    
    @Override
    public MsgType getRobocolMsgType() {
        return MsgType.GAMEPAD;
    }
    
    protected boolean pressed(final KeyEvent keyEvent) {
        return keyEvent.getAction() == 0;
    }
    
    public void setJoystickDeadzone(final float joystickDeadzone) {
        if (joystickDeadzone < 0.0f || joystickDeadzone > 1.0f) {
            throw new IllegalArgumentException("deadzone cannot be greater than max joystick value");
        }
        this.joystickDeadzone = joystickDeadzone;
    }
    
    @Override
    public byte[] toByteArray() throws RobotCoreException {
        int n = 1;
        final ByteBuffer allocate = ByteBuffer.allocate(45);
        try {
            allocate.put(this.getRobocolMsgType().asByte());
            allocate.putShort((short)42);
            allocate.put((byte)2);
            allocate.putInt(this.id);
            allocate.putLong(this.timestamp).array();
            allocate.putFloat(this.left_stick_x).array();
            allocate.putFloat(this.left_stick_y).array();
            allocate.putFloat(this.right_stick_x).array();
            allocate.putFloat(this.right_stick_y).array();
            allocate.putFloat(this.left_trigger).array();
            allocate.putFloat(this.right_trigger).array();
            int n2;
            if (this.left_stick_button) {
                n2 = n;
            }
            else {
                n2 = 0;
            }
            final int n3 = n2 + 0 << 1;
            int n4;
            if (this.right_stick_button) {
                n4 = n;
            }
            else {
                n4 = 0;
            }
            final int n5 = n4 + n3 << 1;
            int n6;
            if (this.dpad_up) {
                n6 = n;
            }
            else {
                n6 = 0;
            }
            final int n7 = n6 + n5 << 1;
            int n8;
            if (this.dpad_down) {
                n8 = n;
            }
            else {
                n8 = 0;
            }
            final int n9 = n8 + n7 << 1;
            int n10;
            if (this.dpad_left) {
                n10 = n;
            }
            else {
                n10 = 0;
            }
            final int n11 = n10 + n9 << 1;
            int n12;
            if (this.dpad_right) {
                n12 = n;
            }
            else {
                n12 = 0;
            }
            final int n13 = n12 + n11 << 1;
            int n14;
            if (this.a) {
                n14 = n;
            }
            else {
                n14 = 0;
            }
            final int n15 = n14 + n13 << 1;
            int n16;
            if (this.b) {
                n16 = n;
            }
            else {
                n16 = 0;
            }
            final int n17 = n16 + n15 << 1;
            int n18;
            if (this.x) {
                n18 = n;
            }
            else {
                n18 = 0;
            }
            final int n19 = n18 + n17 << 1;
            int n20;
            if (this.y) {
                n20 = n;
            }
            else {
                n20 = 0;
            }
            final int n21 = n20 + n19 << 1;
            int n22;
            if (this.guide) {
                n22 = n;
            }
            else {
                n22 = 0;
            }
            final int n23 = n22 + n21 << 1;
            int n24;
            if (this.start) {
                n24 = n;
            }
            else {
                n24 = 0;
            }
            final int n25 = n24 + n23 << 1;
            int n26;
            if (this.back) {
                n26 = n;
            }
            else {
                n26 = 0;
            }
            final int n27 = n26 + n25 << 1;
            int n28;
            if (this.left_bumper) {
                n28 = n;
            }
            else {
                n28 = 0;
            }
            final int n29 = n28 + n27 << 1;
            if (!this.right_bumper) {
                n = 0;
            }
            allocate.putInt(n + n29);
            allocate.put(this.user);
            return allocate.array();
        }
        catch (BufferOverflowException ex) {
            RobotLog.logStacktrace(ex);
            return allocate.array();
        }
    }
    
    @Override
    public String toString() {
        String s = new String();
        if (this.dpad_up) {
            s += "dpad_up ";
        }
        if (this.dpad_down) {
            s += "dpad_down ";
        }
        if (this.dpad_left) {
            s += "dpad_left ";
        }
        if (this.dpad_right) {
            s += "dpad_right ";
        }
        if (this.a) {
            s += "a ";
        }
        if (this.b) {
            s += "b ";
        }
        if (this.x) {
            s += "x ";
        }
        if (this.y) {
            s += "y ";
        }
        if (this.guide) {
            s += "guide ";
        }
        if (this.start) {
            s += "start ";
        }
        if (this.back) {
            s += "back ";
        }
        if (this.left_bumper) {
            s += "left_bumper ";
        }
        if (this.right_bumper) {
            s += "right_bumper ";
        }
        if (this.left_stick_button) {
            s += "left stick button ";
        }
        if (this.right_stick_button) {
            s += "right stick button ";
        }
        return String.format("ID: %2d user: %2d lx: % 1.2f ly: % 1.2f rx: % 1.2f ry: % 1.2f lt: %1.2f rt: %1.2f %s", this.id, this.user, this.left_stick_x, this.left_stick_y, this.right_stick_x, this.right_stick_y, this.left_trigger, this.right_trigger, s);
    }
    
    public String type() {
        return "Standard";
    }
    
    public void update(final KeyEvent keyEvent) {
        this.id = keyEvent.getDeviceId();
        this.timestamp = keyEvent.getEventTime();
        final int keyCode = keyEvent.getKeyCode();
        if (keyCode == 19) {
            this.dpad_up = this.pressed(keyEvent);
        }
        else if (keyCode == 20) {
            this.dpad_down = this.pressed(keyEvent);
        }
        else if (keyCode == 22) {
            this.dpad_right = this.pressed(keyEvent);
        }
        else if (keyCode == 21) {
            this.dpad_left = this.pressed(keyEvent);
        }
        else if (keyCode == 96) {
            this.a = this.pressed(keyEvent);
        }
        else if (keyCode == 97) {
            this.b = this.pressed(keyEvent);
        }
        else if (keyCode == 99) {
            this.x = this.pressed(keyEvent);
        }
        else if (keyCode == 100) {
            this.y = this.pressed(keyEvent);
        }
        else if (keyCode == 110) {
            this.guide = this.pressed(keyEvent);
        }
        else if (keyCode == 108) {
            this.start = this.pressed(keyEvent);
        }
        else if (keyCode == 4) {
            this.back = this.pressed(keyEvent);
        }
        else if (keyCode == 103) {
            this.right_bumper = this.pressed(keyEvent);
        }
        else if (keyCode == 102) {
            this.left_bumper = this.pressed(keyEvent);
        }
        else if (keyCode == 106) {
            this.left_stick_button = this.pressed(keyEvent);
        }
        else if (keyCode == 107) {
            this.right_stick_button = this.pressed(keyEvent);
        }
        this.callCallback();
    }
    
    public void update(final MotionEvent motionEvent) {
        int dpad_left = 1;
        this.id = motionEvent.getDeviceId();
        this.timestamp = motionEvent.getEventTime();
        this.left_stick_x = this.cleanMotionValues(motionEvent.getAxisValue(0));
        this.left_stick_y = this.cleanMotionValues(motionEvent.getAxisValue(dpad_left));
        this.right_stick_x = this.cleanMotionValues(motionEvent.getAxisValue(11));
        this.right_stick_y = this.cleanMotionValues(motionEvent.getAxisValue(14));
        this.left_trigger = motionEvent.getAxisValue(17);
        this.right_trigger = motionEvent.getAxisValue(18);
        this.dpad_down = (motionEvent.getAxisValue(16) > this.dpadThreshold && dpad_left);
        this.dpad_up = (motionEvent.getAxisValue(16) < -this.dpadThreshold && dpad_left);
        this.dpad_right = (motionEvent.getAxisValue(15) > this.dpadThreshold && dpad_left);
        if (motionEvent.getAxisValue(15) >= -this.dpadThreshold) {
            dpad_left = 0;
        }
        this.dpad_left = (dpad_left != 0);
        this.callCallback();
    }
    
    public interface GamepadCallback
    {
        void gamepadChanged(Gamepad p0);
    }
    
    private static class a extends SimpleEntry<Integer, Integer>
    {
        public a(final int n, final int n2) {
            super(n, n2);
        }
    }
}
