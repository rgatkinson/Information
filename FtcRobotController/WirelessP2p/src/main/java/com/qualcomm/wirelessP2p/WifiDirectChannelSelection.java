package com.qualcomm.wirelessP2p;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.RunShellCommand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WifiDirectChannelSelection {
    public static final int INVALID = -1;
    private final String a;
    private final String b;
    private final String c;
    private final WifiManager d;
    private final RunShellCommand e = new RunShellCommand();

    public WifiDirectChannelSelection(Context var1, WifiManager var2) {
        this.a = var1.getFilesDir().getAbsolutePath() + "/";
        this.d = var2;
        this.b = this.a + "get_current_wifi_direct_staus";
        this.c = this.a + "config_wifi_direct";
    }

    private int a() throws RuntimeException {
        String[] var1 = this.e.run("/system/bin/ps").split("\n");
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            String var4 = var1[var3];
            if (var4.contains("wpa_supplicant")) {
                return Integer.parseInt(var4.split("\\s+")[1]);
            }
        }

        throw new RuntimeException("could not find wpa_supplicant PID");
    }

    private void a(int param1, int param2) {
        // $FF: Couldn't be decompiled
    }

    private void b() {
        try {
            char[] var3 = new char[4096];
            FileReader var4 = new FileReader(this.a + "wpa_supplicant.conf");
            int var5 = var4.read(var3);
            var4.close();
            String var6 = new String(var3, 0, var5);
            RobotLog.v("WPA FILE: \n" + var6);
            String var7 = var6.replaceAll("(?s)network\\s*=\\{.*\\}", "").replaceAll("(?m)^\\s+$", "");
            RobotLog.v("WPA REPLACE: \n" + var7);
            FileWriter var8 = new FileWriter(this.a + "wpa_supplicant.conf");
            var8.write(var7);
            var8.close();
        } catch (FileNotFoundException var9) {
            RobotLog.e("File not found: " + var9.toString());
            var9.printStackTrace();
        } catch (IOException var10) {
            RobotLog.e("FIO exception: " + var10.toString());
            var10.printStackTrace();
        }
    }

    private void c() throws IOException {
        Object[] var1 = new Object[]{this.a, this.a, this.a};
        String var2 = String.format("cp /data/misc/wifi/wpa_supplicant.conf %s/wpa_supplicant.conf \ncp /data/misc/wifi/p2p_supplicant.conf %s/p2p_supplicant.conf \nchmod 666 %s/*supplicant* \n", var1);
        Object[] var3 = new Object[]{this.a, this.a, this.a, Integer.valueOf(this.a())};
        String var4 = String.format("cp %s/p2p_supplicant.conf /data/misc/wifi/p2p_supplicant.conf \ncp %s/wpa_supplicant.conf /data/misc/wifi/wpa_supplicant.conf \nrm %s/*supplicant* \nchown system.wifi /data/misc/wifi/wpa_supplicant.conf \nchown system.wifi /data/misc/wifi/p2p_supplicant.conf \nkill -HUP %d \n", var3);
        FileWriter var5 = new FileWriter(this.b);
        var5.write(var2);
        var5.close();
        FileWriter var6 = new FileWriter(this.c);
        var6.write(var4);
        var6.close();
        this.e.run("chmod 700 " + this.b);
        this.e.run("chmod 700 " + this.c);
    }

    private void d() {
        (new File(this.b)).delete();
        (new File(this.c)).delete();
    }

    public void config(int var1, int var2) throws IOException {
        try {
            this.d.setWifiEnabled(false);
            this.c();
            this.e.runAsRoot(this.b);
            this.a(var1, var2);
            this.b();
            this.e.runAsRoot(this.c);
            this.d.setWifiEnabled(true);
        } finally {
            this.d();
        }

    }
}
