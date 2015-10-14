//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.TypeConversion;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Telemetry implements RobocolParsable {
    public static final String DEFAULT_TAG = "TELEMETRY_DATA";
    private static final Charset a = Charset.forName("UTF-8");
    private final Map<String, String> b = new HashMap();
    private final Map<String, Float> c = new HashMap();
    private String d = "";
    private long e = 0L;

    public Telemetry() {
    }

    public Telemetry(byte[] byteArray) throws RobotCoreException {
        this.fromByteArray(byteArray);
    }

    public synchronized long getTimestamp() {
        return this.e;
    }

    public synchronized String getTag() {
        return this.d.length() == 0 ? "TELEMETRY_DATA" : this.d;
    }

    public synchronized void setTag(String tag) {
        this.d = tag;
    }

    public synchronized void addData(String key, String msg) {
        this.b.put(key, msg);
    }

    public synchronized void addData(String key, Object msg) {
        this.b.put(key, msg.toString());
    }

    public synchronized void addData(String key, float msg) {
        this.c.put(key, Float.valueOf(msg));
    }

    public synchronized void addData(String key, double msg) {
        this.c.put(key, Float.valueOf((float) msg));
    }

    public synchronized Map<String, String> getDataStrings() {
        return this.b;
    }

    public synchronized Map<String, Float> getDataNumbers() {
        return this.c;
    }

    public synchronized boolean hasData() {
        return !this.b.isEmpty() || !this.c.isEmpty();
    }

    public synchronized void clearData() {
        this.e = 0L;
        this.b.clear();
        this.c.clear();
    }

    public MsgType getRobocolMsgType() {
        return MsgType.TELEMETRY;
    }

    public synchronized byte[] toByteArray() throws RobotCoreException {
        this.e = System.currentTimeMillis();
        if (this.b.size() > 256) {
            throw new RobotCoreException("Cannot have more than 256 string data points");
        } else if (this.c.size() > 256) {
            throw new RobotCoreException("Cannot have more than 256 number data points");
        } else {
            int var1 = this.a() + 8;
            int var2 = 3 + var1;
            if (var2 > 4098) {
                throw new RobotCoreException(String.format("Cannot send telemetry data of %d bytes; max is %d", Integer.valueOf(var2), Integer.valueOf(4098)));
            } else {
                ByteBuffer var3 = ByteBuffer.allocate(var2);
                var3.put(this.getRobocolMsgType().asByte());
                var3.putShort((short) var1);
                var3.putLong(this.e);
                if (this.d.length() == 0) {
                    var3.put((byte) 0);
                } else {
                    byte[] var4 = this.d.getBytes(a);
                    if (var4.length > 256) {
                        throw new RobotCoreException(String.format("Telemetry tag cannot exceed 256 bytes [%s]", this.d));
                    }

                    var3.put((byte) var4.length);
                    var3.put(var4);
                }

                var3.put((byte) this.b.size());
                Iterator var8 = this.b.entrySet().iterator();

                Entry var5;
                byte[] var6;
                while (var8.hasNext()) {
                    var5 = (Entry) var8.next();
                    var6 = ((String) var5.getKey()).getBytes(a);
                    byte[] var7 = ((String) var5.getValue()).getBytes(a);
                    if (var6.length > 256 || var7.length > 256) {
                        throw new RobotCoreException(String.format("Telemetry elements cannot exceed 256 bytes [%s:%s]", var5.getKey(), var5.getValue()));
                    }

                    var3.put((byte) var6.length);
                    var3.put(var6);
                    var3.put((byte) var7.length);
                    var3.put(var7);
                }

                var3.put((byte) this.c.size());
                var8 = this.c.entrySet().iterator();

                while (var8.hasNext()) {
                    var5 = (Entry) var8.next();
                    var6 = ((String) var5.getKey()).getBytes(a);
                    float var9 = ((Float) var5.getValue()).floatValue();
                    if (var6.length > 256) {
                        throw new RobotCoreException(String.format("Telemetry elements cannot exceed 256 bytes [%s:%f]", var5.getKey(), Float.valueOf(var9)));
                    }

                    var3.put((byte) var6.length);
                    var3.put(var6);
                    var3.putFloat(var9);
                }

                return var3.array();
            }
        }
    }

    public synchronized void fromByteArray(byte[] byteArray) throws RobotCoreException {
        this.clearData();
        ByteBuffer var2 = ByteBuffer.wrap(byteArray, 3, byteArray.length - 3);
        this.e = var2.getLong();
        int var3 = TypeConversion.unsignedByteToInt(var2.get());
        if (var3 == 0) {
            this.d = "";
        } else {
            byte[] var4 = new byte[var3];
            var2.get(var4);
            this.d = new String(var4, a);
        }

        byte var12 = var2.get();

        int var6;
        for (int var5 = 0; var5 < var12; ++var5) {
            var6 = TypeConversion.unsignedByteToInt(var2.get());
            byte[] var7 = new byte[var6];
            var2.get(var7);
            int var8 = TypeConversion.unsignedByteToInt(var2.get());
            byte[] var9 = new byte[var8];
            var2.get(var9);
            String var10 = new String(var7, a);
            String var11 = new String(var9, a);
            this.b.put(var10, var11);
        }

        byte var13 = var2.get();

        for (var6 = 0; var6 < var13; ++var6) {
            int var14 = TypeConversion.unsignedByteToInt(var2.get());
            byte[] var15 = new byte[var14];
            var2.get(var15);
            String var16 = new String(var15, a);
            float var17 = var2.getFloat();
            this.c.put(var16, Float.valueOf(var17));
        }

    }

    private int a() {
        byte var1 = 0;
        int var4 = var1 + 1 + this.d.getBytes(a).length;
        ++var4;

        Iterator var2;
        Entry var3;
        for (var2 = this.b.entrySet().iterator(); var2.hasNext(); var4 += 1 + ((String) var3.getValue()).getBytes(a).length) {
            var3 = (Entry) var2.next();
            var4 += 1 + ((String) var3.getKey()).getBytes(a).length;
        }

        ++var4;

        for (var2 = this.c.entrySet().iterator(); var2.hasNext(); var4 += 4) {
            var3 = (Entry) var2.next();
            var4 += 1 + ((String) var3.getKey()).getBytes(a).length;
        }

        return var4;
    }
}
