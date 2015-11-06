//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.util.Network;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

public class RobocolConfig {
    public static final int MAX_PACKET_SIZE = 4098;
    public static final int PORT_NUMBER = 20884;
    public static final int TTL = 3;
    public static final int TIMEOUT = 1000;
    public static final int WIFI_P2P_SUBNET_MASK = -256;

    public RobocolConfig() {
    }

    public static InetAddress determineBindAddress(InetAddress destAddress) {
        ArrayList var1 = Network.getLocalIpAddresses();
        var1 = Network.removeLoopbackAddresses(var1);
        var1 = Network.removeIPv6Addresses(var1);
        Iterator var2 = var1.iterator();

        while(var2.hasNext()) {
            InetAddress var3 = (InetAddress)var2.next();

            try {
                NetworkInterface var4 = NetworkInterface.getByInetAddress(var3);
                Enumeration var5 = var4.getInetAddresses();

                while(var5.hasMoreElements()) {
                    InetAddress var6 = (InetAddress)var5.nextElement();
                    if(var6.equals(destAddress)) {
                        return var6;
                    }
                }
            } catch (SocketException var7) {
                RobotLog.v(String.format("socket exception while trying to get network interface of %s", new Object[]{var3.getHostAddress()}));
            }
        }

        return determineBindAddressBasedOnWifiP2pSubnet(var1, destAddress);
    }

    public static InetAddress determineBindAddressBasedOnWifiP2pSubnet(ArrayList<InetAddress> localIpAddresses, InetAddress destAddress) {
        int var2 = TypeConversion.byteArrayToInt(destAddress.getAddress());
        Iterator var3 = localIpAddresses.iterator();

        InetAddress var4;
        int var5;
        do {
            if(!var3.hasNext()) {
                return Network.getLoopbackAddress();
            }

            var4 = (InetAddress)var3.next();
            var5 = TypeConversion.byteArrayToInt(var4.getAddress());
        } while((var5 & -256) != (var2 & -256));

        return var4;
    }

    public static InetAddress determineBindAddressBasedOnIsReachable(ArrayList<InetAddress> localIpAddresses, InetAddress destAddress) {
        Iterator var2 = localIpAddresses.iterator();

        while(var2.hasNext()) {
            InetAddress var3 = (InetAddress)var2.next();

            try {
                NetworkInterface var4 = NetworkInterface.getByInetAddress(var3);
                if(var3.isReachable(var4, 3, 1000)) {
                    return var3;
                }
            } catch (SocketException var5) {
                RobotLog.v(String.format("socket exception while trying to get network interface of %s", new Object[]{var3.getHostAddress()}));
            } catch (IOException var6) {
                RobotLog.v(String.format("IO exception while trying to determine if %s is reachable via %s", new Object[]{destAddress.getHostAddress(), var3.getHostAddress()}));
            }
        }

        return Network.getLoopbackAddress();
    }
}
