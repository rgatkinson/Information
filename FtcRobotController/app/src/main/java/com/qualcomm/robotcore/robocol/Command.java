//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

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
    String name;
    String extra;
    byte[] c;
    byte[] d;
    long e;
    boolean f;
    byte g;

    public Command(String name) {
        this(name, "");
    }

    public Command(String name, String extra) {
        this.f = false;
        this.g = 0;
        this.name = name;
        this.extra = extra;
        this.c = TypeConversion.stringToUtf8(this.name);
        this.d = TypeConversion.stringToUtf8(this.extra);
        this.e = generateTimestamp();
        if(this.c.length > MAX_COMMAND_LENGTH) {
            throw new IllegalArgumentException(String.format("command name length is too long (MAX: %d)", new Object[]{Integer.valueOf(MAX_COMMAND_LENGTH)}));
        } else if(this.d.length > MAX_COMMAND_LENGTH) {
            throw new IllegalArgumentException(String.format("command extra data length is too long (MAX: %d)", new Object[]{Integer.valueOf(MAX_COMMAND_LENGTH)}));
        }
    }

    public Command(byte[] byteArray) throws RobotCoreException {
        this.f = false;
        this.g = 0;
        this.fromByteArray(byteArray);
    }

    public void acknowledge() {
        this.f = true;
    }

    public boolean isAcknowledged() {
        return this.f;
    }

    public String getName() {
        return this.name;
    }

    public String getExtra() {
        return this.extra;
    }

    public byte getAttempts() {
        return this.g;
    }

    public long getTimestamp() {
        return this.e;
    }

    public MsgType getRobocolMsgType() {
        return MsgType.COMMAND;
    }

    public byte[] toByteArray() throws RobotCoreException {
        if(this.g != 127) {
            ++this.g;
        }

        short var1 = (short)(11 + this.c.length + this.d.length);
        ByteBuffer var2 = ByteBuffer.allocate(3 + var1);

        try {
            var2.put(this.getRobocolMsgType().asByte());
            var2.putShort(var1);
            var2.putLong(this.e);
            var2.put((byte)(this.f?1:0));
            var2.put((byte)this.c.length);
            var2.put(this.c);
            var2.put((byte)this.d.length);
            var2.put(this.d);
        } catch (BufferOverflowException var4) {
            RobotLog.logStacktrace(var4);
        }

        return var2.array();
    }

    public void fromByteArray(byte[] byteArray) throws RobotCoreException {
        ByteBuffer var2 = ByteBuffer.wrap(byteArray, 3, byteArray.length - 3);
        this.e = var2.getLong();
        this.f = var2.get() == 1;
        int var3 = TypeConversion.unsignedByteToInt(var2.get());
        this.c = new byte[var3];
        var2.get(this.c);
        this.name = TypeConversion.utf8ToString(this.c);
        var3 = TypeConversion.unsignedByteToInt(var2.get());
        this.d = new byte[var3];
        var2.get(this.d);
        this.extra = TypeConversion.utf8ToString(this.d);
    }

    public String toString() {
        return String.format("command: %20d %5s %s", new Object[]{Long.valueOf(this.e), Boolean.valueOf(this.f), this.name});
    }

    public boolean equals(Object o) {
        if(o instanceof Command) {
            Command var2 = (Command)o;
            if(this.name.equals(var2.name) && this.e == var2.e) {
                return true;
            }
        }

        return false;
    }

    public int hashCode() {
        return (int)((long)this.name.hashCode() & this.e);
    }

    public int compareTo(Command another) {
        int var2 = this.name.compareTo(another.name);
        return var2 != 0?var2:(this.e < another.e?-1:(this.e > another.e?1:0));
    }

    public int compare(Command c1, Command c2) {
        return c1.compareTo(c2);
    }

    public static long generateTimestamp() {
        return System.nanoTime();
    }
}
