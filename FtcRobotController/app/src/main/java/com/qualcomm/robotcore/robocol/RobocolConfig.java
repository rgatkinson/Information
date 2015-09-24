package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.util.Network;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

public class RobocolConfig {
   public static final int MAX_PACKET_SIZE = 4098;
   public static final int PORT_NUMBER = 20884;
   public static final int TIMEOUT = 1000;
   public static final int TTL = 3;
   public static final int WIFI_P2P_SUBNET_MASK = -256;

   public static InetAddress determineBindAddress(InetAddress param0) {
      // $FF: Couldn't be decompiled
   }

   public static InetAddress determineBindAddressBasedOnIsReachable(ArrayList<InetAddress> var0, InetAddress var1) {
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         InetAddress var3 = (InetAddress)var2.next();

         boolean var8;
         try {
            var8 = var3.isReachable(NetworkInterface.getByInetAddress(var3), 3, 1000);
         } catch (SocketException var9) {
            Object[] var7 = new Object[]{var3.getHostAddress()};
            RobotLog.v(String.format("socket exception while trying to get network interface of %s", var7));
            continue;
         } catch (IOException var10) {
            Object[] var5 = new Object[]{var1.getHostAddress(), var3.getHostAddress()};
            RobotLog.v(String.format("IO exception while trying to determine if %s is reachable via %s", var5));
            continue;
         }

         if(var8) {
            return var3;
         }
      }

      return Network.getLoopbackAddress();
   }

   public static InetAddress determineBindAddressBasedOnWifiP2pSubnet(ArrayList<InetAddress> var0, InetAddress var1) {
      int var2 = TypeConversion.byteArrayToInt(var1.getAddress());
      Iterator var3 = var0.iterator();

      InetAddress var4;
      do {
         if(!var3.hasNext()) {
            return Network.getLoopbackAddress();
         }

         var4 = (InetAddress)var3.next();
      } while((-256 & TypeConversion.byteArrayToInt(var4.getAddress())) != (var2 & -256));

      return var4;
   }
}
