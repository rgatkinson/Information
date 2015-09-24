package com.qualcomm.robotcore.robocol;

import java.nio.BufferOverflowException;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.ByteBuffer;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.charset.Charset;
import java.util.Comparator;

public class Command implements RobocolParsable, Comparable<Command>, Comparator<Command>
{
    public static final int MAX_COMMAND_LENGTH = 256;
    private static final Charset h;
    String a;
    String b;
    byte[] c;
    byte[] d;
    long e;
    boolean f;
    byte g;
    
    static {
        h = Charset.forName("UTF-8");
    }
    
    public Command(final String s) {
        this(s, "");
    }
    
    public Command(final String a, final String b) {
        this.f = false;
        this.g = 0;
        this.a = a;
        this.b = b;
        this.c = TypeConversion.stringToUtf8(this.a);
        this.d = TypeConversion.stringToUtf8(this.b);
        this.e = generateTimestamp();
        if (this.c.length > 256) {
            throw new IllegalArgumentException(String.format("command name length is too long (MAX: %d)", 256));
        }
        if (this.d.length > 256) {
            throw new IllegalArgumentException(String.format("command extra data length is too long (MAX: %d)", 256));
        }
    }
    
    public Command(final byte[] array) throws RobotCoreException {
        this.f = false;
        this.g = 0;
        this.fromByteArray(array);
    }
    
    public static long generateTimestamp() {
        return System.nanoTime();
    }
    
    public void acknowledge() {
        this.f = true;
    }
    
    @Override
    public int compare(final Command command, final Command command2) {
        return command.compareTo(command2);
    }
    
    @Override
    public int compareTo(final Command command) {
        final int compareTo = this.a.compareTo(command.a);
        if (compareTo != 0) {
            return compareTo;
        }
        if (this.e < command.e) {
            return -1;
        }
        if (this.e > command.e) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof Command) {
            final Command command = (Command)o;
            if (this.a.equals(command.a) && this.e == command.e) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void fromByteArray(final byte[] array) throws RobotCoreException {
        boolean f = true;
        final ByteBuffer wrap = ByteBuffer.wrap(array, 3, -3 + array.length);
        this.e = wrap.getLong();
        if (wrap.get() != (f ? 1 : 0)) {
            f = false;
        }
        this.f = f;
        wrap.get(this.c = new byte[TypeConversion.unsignedByteToInt(wrap.get())]);
        this.a = TypeConversion.utf8ToString(this.c);
        wrap.get(this.d = new byte[TypeConversion.unsignedByteToInt(wrap.get())]);
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
    
    @Override
    public MsgType getRobocolMsgType() {
        return MsgType.COMMAND;
    }
    
    public long getTimestamp() {
        return this.e;
    }
    
    @Override
    public int hashCode() {
        return (int)(this.a.hashCode() & this.e);
    }
    
    public boolean isAcknowledged() {
        return this.f;
    }
    
    @Override
    public byte[] toByteArray() throws RobotCoreException {
        if (this.g != 127) {
            ++this.g;
        }
        final short n = (short)(11 + this.c.length + this.d.length);
        final ByteBuffer allocate = ByteBuffer.allocate(n + 3);
        try {
            allocate.put(this.getRobocolMsgType().asByte());
            allocate.putShort(n);
            allocate.putLong(this.e);
            boolean b;
            if (this.f) {
                b = true;
            }
            else {
                b = false;
            }
            allocate.put((byte)(b ? 1 : 0));
            allocate.put((byte)this.c.length);
            allocate.put(this.c);
            allocate.put((byte)this.d.length);
            allocate.put(this.d);
            return allocate.array();
        }
        catch (BufferOverflowException ex) {
            RobotLog.logStacktrace(ex);
            return allocate.array();
        }
    }
    
    @Override
    public String toString() {
        return String.format("command: %20d %5s %s", this.e, this.f, this.a);
    }
}
