package com.qualcomm.wirelessp2p;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.FileReader;
import android.content.Context;
import com.qualcomm.robotcore.util.RunShellCommand;
import android.net.wifi.WifiManager;

public class WifiDirectChannelSelection
{
    public static final int INVALID = -1;
    private final String a;
    private final String b;
    private final String c;
    private final WifiManager d;
    private final RunShellCommand e;
    
    public WifiDirectChannelSelection(final Context context, final WifiManager d) {
        this.e = new RunShellCommand();
        this.a = context.getFilesDir().getAbsolutePath() + "/";
        this.d = d;
        this.b = this.a + "get_current_wifi_direct_staus";
        this.c = this.a + "config_wifi_direct";
    }
    
    private int a() throws RuntimeException {
        for (final String s : this.e.run("/system/bin/ps").split("\n")) {
            if (s.contains("wpa_supplicant")) {
                return Integer.parseInt(s.split("\\s+")[1]);
            }
        }
        throw new RuntimeException("could not find wpa_supplicant PID");
    }
    
    private void a(final int n, final int n2) {
        try {
            final char[] array = new char[8192];
            final FileReader fileReader = new FileReader(this.a + "p2p_supplicant.conf");
            final int read = fileReader.read(array);
            fileReader.close();
            final String s = new String(array, 0, read);
            RobotLog.v("P2P ORIG FILE: \n" + s);
            String s2 = s.replaceAll("p2p_listen_reg_class\\w*=.*", "").replaceAll("p2p_listen_channel\\w*=.*", "").replaceAll("p2p_oper_reg_class\\w*=.*", "").replaceAll("p2p_oper_channel\\w*=.*", "").replaceAll("p2p_pref_chan\\w*=.*", "").replaceAll("(?s)network\\s*=\\{.*\\}", "").replaceAll("(?m)^\\s+$", "");
            if (n != -1 && n2 != -1) {
                s2 = s2 + "p2p_oper_reg_class=" + n + "\n" + "p2p_oper_channel=" + n2 + "\n" + "p2p_pref_chan=" + n + ":" + n2 + "\n";
            }
            RobotLog.v("P2P NEW FILE: \n" + s2);
            final FileWriter fileWriter = new FileWriter(this.a + "p2p_supplicant.conf");
            fileWriter.write(s2);
            fileWriter.close();
        }
        catch (FileNotFoundException ex) {
            RobotLog.e("File not found: " + ex.toString());
            ex.printStackTrace();
        }
        catch (IOException ex2) {
            RobotLog.e("FIO exception: " + ex2.toString());
            ex2.printStackTrace();
        }
    }
    
    private void b() {
        try {
            final char[] array = new char[4096];
            final FileReader fileReader = new FileReader(this.a + "wpa_supplicant.conf");
            final int read = fileReader.read(array);
            fileReader.close();
            final String s = new String(array, 0, read);
            RobotLog.v("WPA FILE: \n" + s);
            final String replaceAll = s.replaceAll("(?s)network\\s*=\\{.*\\}", "").replaceAll("(?m)^\\s+$", "");
            RobotLog.v("WPA REPLACE: \n" + replaceAll);
            final FileWriter fileWriter = new FileWriter(this.a + "wpa_supplicant.conf");
            fileWriter.write(replaceAll);
            fileWriter.close();
        }
        catch (FileNotFoundException ex) {
            RobotLog.e("File not found: " + ex.toString());
            ex.printStackTrace();
        }
        catch (IOException ex2) {
            RobotLog.e("FIO exception: " + ex2.toString());
            ex2.printStackTrace();
        }
    }
    
    private void c() throws IOException {
        final String format = String.format("cp /data/misc/wifi/wpa_supplicant.conf %s/wpa_supplicant.conf \ncp /data/misc/wifi/p2p_supplicant.conf %s/p2p_supplicant.conf \nchmod 666 %s/*supplicant* \n", this.a, this.a, this.a);
        final String format2 = String.format("cp %s/p2p_supplicant.conf /data/misc/wifi/p2p_supplicant.conf \ncp %s/wpa_supplicant.conf /data/misc/wifi/wpa_supplicant.conf \nrm %s/*supplicant* \nchown system.wifi /data/misc/wifi/wpa_supplicant.conf \nchown system.wifi /data/misc/wifi/p2p_supplicant.conf \nkill -HUP %d \n", this.a, this.a, this.a, this.a());
        final FileWriter fileWriter = new FileWriter(this.b);
        fileWriter.write(format);
        fileWriter.close();
        final FileWriter fileWriter2 = new FileWriter(this.c);
        fileWriter2.write(format2);
        fileWriter2.close();
        this.e.run("chmod 700 " + this.b);
        this.e.run("chmod 700 " + this.c);
    }
    
    private void d() {
        new File(this.b).delete();
        new File(this.c).delete();
    }
    
    public void config(final int n, final int n2) throws IOException {
        try {
            this.d.setWifiEnabled(false);
            this.c();
            this.e.runAsRoot(this.b);
            this.a(n, n2);
            this.b();
            this.e.runAsRoot(this.c);
            this.d.setWifiEnabled(true);
        }
        finally {
            this.d();
        }
    }
}
