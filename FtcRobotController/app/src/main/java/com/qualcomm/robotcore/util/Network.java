//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.robotcore.util;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class Network {
    public Network() {
    }

    public static InetAddress getLoopbackAddress() {
        try {
            return InetAddress.getByAddress(new byte[]{(byte)127, (byte)0, (byte)0, (byte)1});
        } catch (UnknownHostException var1) {
            return null;
        }
    }

    public static ArrayList<InetAddress> getLocalIpAddresses() {
        ArrayList var0 = new ArrayList();

        try {
            Iterator var1 = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();

            while(var1.hasNext()) {
                NetworkInterface var2 = (NetworkInterface)var1.next();
                var0.addAll(Collections.list(var2.getInetAddresses()));
            }
        } catch (SocketException var3) {
            ;
        }

        return var0;
    }

    public static ArrayList<InetAddress> getLocalIpAddress(String networkInterface) {
        ArrayList var1 = new ArrayList();

        try {
            Iterator var2 = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();

            while(var2.hasNext()) {
                NetworkInterface var3 = (NetworkInterface)var2.next();
                if(var3.getName() == networkInterface) {
                    var1.addAll(Collections.list(var3.getInetAddresses()));
                }
            }
        } catch (SocketException var4) {
            ;
        }

        return var1;
    }

    public static ArrayList<InetAddress> removeIPv6Addresses(Collection<InetAddress> addresses) {
        ArrayList var1 = new ArrayList();
        Iterator var2 = addresses.iterator();

        while(var2.hasNext()) {
            InetAddress var3 = (InetAddress)var2.next();
            if(var3 instanceof Inet4Address) {
                var1.add(var3);
            }
        }

        return var1;
    }

    public static ArrayList<InetAddress> removeIPv4Addresses(Collection<InetAddress> addresses) {
        ArrayList var1 = new ArrayList();
        Iterator var2 = addresses.iterator();

        while(var2.hasNext()) {
            InetAddress var3 = (InetAddress)var2.next();
            if(var3 instanceof Inet6Address) {
                var1.add(var3);
            }
        }

        return var1;
    }

    public static ArrayList<InetAddress> removeLoopbackAddresses(Collection<InetAddress> addresses) {
        ArrayList var1 = new ArrayList();
        Iterator var2 = addresses.iterator();

        while(var2.hasNext()) {
            InetAddress var3 = (InetAddress)var2.next();
            if(!var3.isLoopbackAddress()) {
                var1.add(var3);
            }
        }

        return var1;
    }

    public static ArrayList<String> getHostAddresses(Collection<InetAddress> addresses) {
        ArrayList var1 = new ArrayList();

        String var4;
        for(Iterator var2 = addresses.iterator(); var2.hasNext(); var1.add(var4)) {
            InetAddress var3 = (InetAddress)var2.next();
            var4 = var3.getHostAddress();
            if(var4.contains("%")) {
                var4 = var4.substring(0, var4.indexOf(37));
            }
        }

        return var1;
    }
}
