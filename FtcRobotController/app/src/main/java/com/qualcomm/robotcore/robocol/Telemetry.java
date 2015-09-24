package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteBuffer;
import java.util.Iterator;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.Charset;

public class Telemetry implements RobocolParsable
{
    public static final String DEFAULT_TAG = "TELEMETRY_DATA";
    private static final Charset a;
    private final Map<String, String> b;
    private final Map<String, Float> c;
    private String d;
    private long e;
    
    static {
        a = Charset.forName("UTF-8");
    }
    
    public Telemetry() {
        this.b = new HashMap<String, String>();
        this.c = new HashMap<String, Float>();
        this.d = "";
        this.e = 0L;
    }
    
    public Telemetry(final byte[] array) throws RobotCoreException {
        this.b = new HashMap<String, String>();
        this.c = new HashMap<String, Float>();
        this.d = "";
        this.e = 0L;
        this.fromByteArray(array);
    }
    
    private int a() {
        final int n = 1 + (0 + (1 + this.d.getBytes(Telemetry.a).length));
        final Iterator<Map.Entry<String, String>> iterator = this.b.entrySet().iterator();
        int n2 = n;
        while (iterator.hasNext()) {
            final Map.Entry<String, String> entry = iterator.next();
            n2 = n2 + (1 + entry.getKey().getBytes(Telemetry.a).length) + (1 + entry.getValue().getBytes(Telemetry.a).length);
        }
        final int n3 = n2 + 1;
        final Iterator<Map.Entry<String, Float>> iterator2 = this.c.entrySet().iterator();
        int n4 = n3;
        while (iterator2.hasNext()) {
            n4 = 4 + (n4 + (1 + iterator2.next().getKey().getBytes(Telemetry.a).length));
        }
        return n4;
    }
    
    public void addData(final String s, final double n) {
        synchronized (this) {
            this.c.put(s, (float)n);
        }
    }
    
    public void addData(final String s, final float n) {
        synchronized (this) {
            this.c.put(s, n);
        }
    }
    
    public void addData(final String s, final Object o) {
        synchronized (this) {
            this.b.put(s, o.toString());
        }
    }
    
    public void addData(final String s, final String s2) {
        synchronized (this) {
            this.b.put(s, s2);
        }
    }
    
    public void clearData() {
        synchronized (this) {
            this.e = 0L;
            this.b.clear();
            this.c.clear();
        }
    }
    
    @Override
    public void fromByteArray(final byte[] array) throws RobotCoreException {
        byte b = 0;
        final ByteBuffer wrap;
        synchronized (this) {
            this.clearData();
            wrap = ByteBuffer.wrap(array, 3, -3 + array.length);
            this.e = wrap.getLong();
            final int unsignedByteToInt = TypeConversion.unsignedByteToInt(wrap.get());
            if (unsignedByteToInt == 0) {
                this.d = "";
            }
            else {
                final byte[] array2 = new byte[unsignedByteToInt];
                wrap.get(array2);
                this.d = new String(array2, Telemetry.a);
            }
            for (byte value = wrap.get(), b2 = 0; b2 < value; ++b2) {
                final byte[] array3 = new byte[TypeConversion.unsignedByteToInt(wrap.get())];
                wrap.get(array3);
                final byte[] array4 = new byte[TypeConversion.unsignedByteToInt(wrap.get())];
                wrap.get(array4);
                this.b.put(new String(array3, Telemetry.a), new String(array4, Telemetry.a));
            }
        }
        while (b < wrap.get()) {
            final byte[] array5 = new byte[TypeConversion.unsignedByteToInt(wrap.get())];
            wrap.get(array5);
            this.c.put(new String(array5, Telemetry.a), wrap.getFloat());
            ++b;
        }
    }
    // monitorexit(this)
    
    public Map<String, Float> getDataNumbers() {
        synchronized (this) {
            return this.c;
        }
    }
    
    public Map<String, String> getDataStrings() {
        synchronized (this) {
            return this.b;
        }
    }
    
    @Override
    public MsgType getRobocolMsgType() {
        return MsgType.TELEMETRY;
    }
    
    public String getTag() {
        synchronized (this) {
            String d;
            if (this.d.length() == 0) {
                d = "TELEMETRY_DATA";
            }
            else {
                d = this.d;
            }
            return d;
        }
    }
    
    public long getTimestamp() {
        synchronized (this) {
            return this.e;
        }
    }
    
    public boolean hasData() {
        synchronized (this) {
            return !this.b.isEmpty() || !this.c.isEmpty();
        }
    }
    
    public void setTag(final String d) {
        synchronized (this) {
            this.d = d;
        }
    }
    
    @Override
    public byte[] toByteArray() throws RobotCoreException {
        synchronized (this) {
            this.e = System.currentTimeMillis();
            if (this.b.size() > 256) {
                throw new RobotCoreException("Cannot have more than 256 string data points");
            }
        }
        if (this.c.size() > 256) {
            throw new RobotCoreException("Cannot have more than 256 number data points");
        }
        final int n = 8 + this.a();
        final int n2 = n + 3;
        if (n2 > 4098) {
            throw new RobotCoreException(String.format("Cannot send telemetry data of %d bytes; max is %d", n2, 4098));
        }
        final ByteBuffer allocate = ByteBuffer.allocate(n2);
        allocate.put(this.getRobocolMsgType().asByte());
        allocate.putShort((short)n);
        allocate.putLong(this.e);
        if (this.d.length() == 0) {
            allocate.put((byte)0);
        }
        else {
            final byte[] bytes = this.d.getBytes(Telemetry.a);
            if (bytes.length > 256) {
                throw new RobotCoreException(String.format("Telemetry tag cannot exceed 256 bytes [%s]", this.d));
            }
            allocate.put((byte)bytes.length);
            allocate.put(bytes);
        }
        allocate.put((byte)this.b.size());
        for (final Map.Entry<String, String> entry : this.b.entrySet()) {
            final byte[] bytes2 = entry.getKey().getBytes(Telemetry.a);
            final byte[] bytes3 = entry.getValue().getBytes(Telemetry.a);
            if (bytes2.length > 256 || bytes3.length > 256) {
                throw new RobotCoreException(String.format("Telemetry elements cannot exceed 256 bytes [%s:%s]", entry.getKey(), entry.getValue()));
            }
            allocate.put((byte)bytes2.length);
            allocate.put(bytes2);
            allocate.put((byte)bytes3.length);
            allocate.put(bytes3);
        }
        allocate.put((byte)this.c.size());
        for (final Map.Entry<String, Float> entry2 : this.c.entrySet()) {
            final byte[] bytes4 = entry2.getKey().getBytes(Telemetry.a);
            final float floatValue = entry2.getValue();
            if (bytes4.length > 256) {
                throw new RobotCoreException(String.format("Telemetry elements cannot exceed 256 bytes [%s:%f]", entry2.getKey(), floatValue));
            }
            allocate.put((byte)bytes4.length);
            allocate.put(bytes4);
            allocate.putFloat(floatValue);
        }
        // monitorexit(this)
        return allocate.array();
    }
}
