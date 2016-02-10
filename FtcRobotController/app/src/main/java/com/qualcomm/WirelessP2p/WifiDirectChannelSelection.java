//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.wirelessp2p;

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

    public WifiDirectChannelSelection(Context context, WifiManager wifiManager) {
        this.a = context.getFilesDir().getAbsolutePath() + "/";
        this.d = wifiManager;
        this.b = this.a + "get_current_wifi_direct_staus";
        this.c = this.a + "config_wifi_direct";
    }

    public void config(int wifiClass, int wifiChannel) throws IOException {
        try {
            this.d.setWifiEnabled(false);
            this.c();
            this.e.runAsRoot(this.b);
            this.a(wifiClass, wifiChannel);
            this.b();
            this.e.runAsRoot(this.c);
            this.d.setWifiEnabled(true);
        } finally {
            this.d();
        }

    }

    private int a() throws RuntimeException {
        String var1 = this.e.run("/system/bin/ps");
        String[] var2 = var1.split("\n");
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if(var5.contains("wpa_supplicant")) {
                String[] var6 = var5.split("\\s+");
                return Integer.parseInt(var6[1]);
            }
        }

        throw new RuntimeException("could not find wpa_supplicant PID");
    }

    private void b() {
        try {
            char[] var1 = new char[4096];
            FileReader var2 = new FileReader(this.a + "wpa_supplicant.conf");
            int var3 = var2.read(var1);
            var2.close();
            String var4 = new String(var1, 0, var3);
            RobotLog.v("WPA FILE: \n" + var4);
            var4 = var4.replaceAll("(?s)network\\s*=\\{.*\\}", "");
            var4 = var4.replaceAll("(?m)^\\s+$", "");
            RobotLog.v("WPA REPLACE: \n" + var4);
            FileWriter var5 = new FileWriter(this.a + "wpa_supplicant.conf");
            var5.write(var4);
            var5.close();
        } catch (FileNotFoundException var6) {
            RobotLog.e("File not found: " + var6.toString());
            var6.printStackTrace();
        } catch (IOException var7) {
            RobotLog.e("FIO exception: " + var7.toString());
            var7.printStackTrace();
        }

    }

    private void a(int var1, int var2) {
        try {
            char[] var3 = new char[8192];
            FileReader var4 = new FileReader(this.a + "p2p_supplicant.conf");
            int var5 = var4.read(var3);
            var4.close();
            String var6 = new String(var3, 0, var5);
            RobotLog.v("P2P ORIG FILE: \n" + var6);
            var6 = var6.replaceAll("p2p_listen_reg_class\\w*=.*", "");
            var6 = var6.replaceAll("p2p_listen_channel\\w*=.*", "");
            var6 = var6.replaceAll("p2p_oper_reg_class\\w*=.*", "");
            var6 = var6.replaceAll("p2p_oper_channel\\w*=.*", "");
            var6 = var6.replaceAll("p2p_pref_chan\\w*=.*", "");
            var6 = var6.replaceAll("(?s)network\\s*=\\{.*\\}", "");
            var6 = var6.replaceAll("(?m)^\\s+$", "");
            if(var1 != -1 && var2 != -1) {
                var6 = var6 + "p2p_oper_reg_class=" + var1 + "\n";
                var6 = var6 + "p2p_oper_channel=" + var2 + "\n";
                var6 = var6 + "p2p_pref_chan=" + var1 + ":" + var2 + "\n";
            }

            RobotLog.v("P2P NEW FILE: \n" + var6);
            FileWriter var7 = new FileWriter(this.a + "p2p_supplicant.conf");
            var7.write(var6);
            var7.close();
        } catch (FileNotFoundException var8) {
            RobotLog.e("File not found: " + var8.toString());
            var8.printStackTrace();
        } catch (IOException var9) {
            RobotLog.e("FIO exception: " + var9.toString());
            var9.printStackTrace();
        }

    }

    private void c() throws IOException {
        String var1 = String.format("cp /data/misc/wifi/wpa_supplicant.conf %s/wpa_supplicant.conf \ncp /data/misc/wifi/p2p_supplicant.conf %s/p2p_supplicant.conf \nchmod 666 %s/*supplicant* \n", new Object[]{this.a, this.a, this.a});
        String var2 = String.format("cp %s/p2p_supplicant.conf /data/misc/wifi/p2p_supplicant.conf \ncp %s/wpa_supplicant.conf /data/misc/wifi/wpa_supplicant.conf \nrm %s/*supplicant* \nchown system.wifi /data/misc/wifi/wpa_supplicant.conf \nchown system.wifi /data/misc/wifi/p2p_supplicant.conf \nkill -HUP %d \n", new Object[]{this.a, this.a, this.a, Integer.valueOf(this.a())});
        FileWriter var3 = new FileWriter(this.b);
        var3.write(var1);
        var3.close();
        var3 = new FileWriter(this.c);
        var3.write(var2);
        var3.close();
        this.e.run("chmod 700 " + this.b);
        this.e.run("chmod 700 " + this.c);
    }

    private void d() {
        File var1 = new File(this.b);
        var1.delete();
        var1 = new File(this.c);
        var1.delete();
    }
}
