package com.qualcomm.robotcore.util;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Network {
   public static ArrayList<String> getHostAddresses(Collection<InetAddress> var0) {
      ArrayList var1 = new ArrayList();

      String var3;
      for(Iterator var2 = var0.iterator(); var2.hasNext(); var1.add(var3)) {
         var3 = ((InetAddress)var2.next()).getHostAddress();
         if(var3.contains("%")) {
            var3 = var3.substring(0, var3.indexOf(37));
         }
      }

      return var1;
   }

   public static ArrayList<InetAddress> getLocalIpAddress(String param0) {
      // $FF: Couldn't be decompiled
   }

   public static ArrayList<InetAddress> getLocalIpAddresses() {
      // $FF: Couldn't be decompiled
   }

   public static InetAddress getLoopbackAddress() {
      try {
         InetAddress var1 = InetAddress.getByAddress(new byte[]{(byte)127, (byte)0, (byte)0, (byte)1});
         return var1;
      } catch (UnknownHostException var2) {
         return null;
      }
   }

   public static ArrayList<InetAddress> removeIPv4Addresses(Collection<InetAddress> var0) {
      ArrayList var1 = new ArrayList();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         InetAddress var3 = (InetAddress)var2.next();
         if(var3 instanceof Inet6Address) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public static ArrayList<InetAddress> removeIPv6Addresses(Collection<InetAddress> var0) {
      ArrayList var1 = new ArrayList();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         InetAddress var3 = (InetAddress)var2.next();
         if(var3 instanceof Inet4Address) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public static ArrayList<InetAddress> removeLoopbackAddresses(Collection<InetAddress> var0) {
      ArrayList var1 = new ArrayList();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         InetAddress var3 = (InetAddress)var2.next();
         if(!var3.isLoopbackAddress()) {
            var1.add(var3);
         }
      }

      return var1;
   }
}
