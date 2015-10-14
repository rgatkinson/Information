package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Comparator;

public class Command implements RobocolParsable, Comparable<Command>, Comparator<Command> {
    public static final int MAX_COMMAND_LENGTH = 256;
    private static final Charset h = Charset.forName("UTF-8");
    String a;
    String b;
    byte[] c;
    byte[] d;
    long e;
    boolean f;
    byte g;

    public Command(String var1) {
        this(var1, "");
    }

    public Command(String var1, String var2) {
        this.f = false;
        this.g = 0;
        this.a = var1;
        this.b = var2;
        this.c = TypeConversion.stringToUtf8(this.a);
        this.d = TypeConversion.stringToUtf8(this.b);
        this.e = generateTimestamp();
        if (this.c.length > 256) {
            Object[] var4 = new Object[]{Integer.valueOf(256)};
            throw new IllegalArgumentException(String.format("command name length is too long (MAX: %d)", var4));
        } else if (this.d.length > 256) {
            Object[] var3 = new Object[]{Integer.valueOf(256)};
            throw new IllegalArgumentException(String.format("command extra data length is too long (MAX: %d)", var3));
        }
    }

    public Command(byte[] var1) throws RobotCoreException {
        this.f = false;
        this.g = 0;
        this.fromByteArray(var1);
    }

    public static long generateTimestamp() {
        return System.nanoTime();
    }

    public void acknowledge() {
        this.f = true;
    }

    public int compare(Command var1, Command var2) {
        return var1.compareTo(var2);
    }

    public int compareTo(Command var1) {
        int var2 = this.a.compareTo(var1.a);
        return var2 != 0 ? var2 : (this.e < var1.e ? -1 : (this.e > var1.e ? 1 : 0));
    }

    public boolean equals(Object var1) {
        if (var1 instanceof Command) {
            Command var2 = (Command) var1;
            if (this.a.equals(var2.a) && this.e == var2.e) {
                return true;
            }
        }

        return false;
    }

    public void fromByteArray(byte[] var1) throws RobotCoreException {
        int var2 = 1;
        ByteBuffer var3 = ByteBuffer.wrap(var1, 3, -3 + var1.length);
        this.e = var3.getLong();
        if (var3.get() != var2) {
            var2 = 0;
        }

        this.f = var2 != 0;
        this.c = new byte[TypeConversion.unsignedByteToInt(var3.get())];
        var3.get(this.c);
        this.a = TypeConversion.utf8ToString(this.c);
        this.d = new byte[TypeConversion.unsignedByteToInt(var3.get())];
        var3.get(this.d);
        this.b = TypeConversion.utf8ToString(this.d);
    }

    public byte getAttempts() {
        return this.g;
    }

    public String getExtra() {
        return this.b;
    }

    public String getName() {
        return this.a;
    }

    public MsgType getRobocolMsgType() {
        return MsgType.COMMAND;
    }

    public long getTimestamp() {
        return this.e;
    }

    public int hashCode() {
        return (int) ((long) this.a.hashCode() & this.e);
    }

    public boolean isAcknowledged() {
        return this.f;
    }

    public byte[] toByteArray() throws RobotCoreException {
        if (this.g != 127) {
            ++this.g;
        }

        short var1 = (short) (11 + this.c.length + this.d.length);
        ByteBuffer var2 = ByteBuffer.allocate(3 + var1);

        try {
            var2.put(this.getRobocolMsgType().asByte());
            var2.putShort(var1);
            var2.putLong(this.e);
            var2.put((byte) (this.f ? 1 : 0));
            var2.put((byte) this.c.length);
            var2.put(this.c);
            var2.put((byte) this.d.length);
            var2.put(this.d);
        } catch (BufferOverflowException var4) {
            RobotLog.logStacktrace(var4);
        }

        return var2.array();
    }

    public String toString() {
        Object[] var1 = new Object[]{Long.valueOf(this.e), Boolean.valueOf(this.f), this.a};
        return String.format("command: %20d %5s %s", var1);
    }
}
