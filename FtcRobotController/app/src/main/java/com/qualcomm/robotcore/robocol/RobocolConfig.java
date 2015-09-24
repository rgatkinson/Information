package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.util.TypeConversion;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ArrayList;
import java.net.SocketException;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.NetworkInterface;
import java.util.Collection;
import com.qualcomm.robotcore.util.Network;
import java.net.InetAddress;

public class RobocolConfig
{
    public static final int MAX_PACKET_SIZE = 4098;
    public static final int PORT_NUMBER = 20884;
    public static final int TIMEOUT = 1000;
    public static final int TTL = 3;
    public static final int WIFI_P2P_SUBNET_MASK = -256;
    
    public static InetAddress determineBindAddress(final InetAddress inetAddress) {
        final ArrayList<InetAddress> removeIPv6Addresses = Network.removeIPv6Addresses(Network.removeLoopbackAddresses(Network.getLocalIpAddresses()));
        for (final InetAddress inetAddress2 : removeIPv6Addresses) {
            try {
                final Enumeration<InetAddress> inetAddresses = NetworkInterface.getByInetAddress(inetAddress2).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    final InetAddress inetAddress3 = inetAddresses.nextElement();
                    if (inetAddress3.equals(inetAddress)) {
                        return inetAddress3;
                    }
                }
                continue;
            }
            catch (SocketException ex) {
                RobotLog.v(String.format("socket exception while trying to get network interface of %s", inetAddress2.getHostAddress()));
                continue;
            }
            break;
        }
        return determineBindAddressBasedOnWifiP2pSubnet(removeIPv6Addresses, inetAddress);
    }
    
    public static InetAddress determineBindAddressBasedOnIsReachable(final ArrayList<InetAddress> list, final InetAddress inetAddress) {
        for (final InetAddress inetAddress2 : list) {
            try {
                if (inetAddress2.isReachable(NetworkInterface.getByInetAddress(inetAddress2), 3, 1000)) {
                    return inetAddress2;
                }
                continue;
            }
            catch (SocketException ex) {
                RobotLog.v(String.format("socket exception while trying to get network interface of %s", inetAddress2.getHostAddress()));
                continue;
            }
            catch (IOException ex2) {
                RobotLog.v(String.format("IO exception while trying to determine if %s is reachable via %s", inetAddress.getHostAddress(), inetAddress2.getHostAddress()));
                continue;
            }
            break;
        }
        return Network.getLoopbackAddress();
    }
    
    public static InetAddress determineBindAddressBasedOnWifiP2pSubnet(final ArrayList<InetAddress> list, final InetAddress inetAddress) {
        final int byteArrayToInt = TypeConversion.byteArrayToInt(inetAddress.getAddress());
        for (final InetAddress inetAddress2 : list) {
            if ((0xFFFFFF00 & TypeConversion.byteArrayToInt(inetAddress2.getAddress())) == (byteArrayToInt & 0xFFFFFF00)) {
                return inetAddress2;
            }
        }
        return Network.getLoopbackAddress();
    }
}
