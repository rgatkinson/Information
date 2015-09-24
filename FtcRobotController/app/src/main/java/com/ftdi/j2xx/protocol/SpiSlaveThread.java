package com.ftdi.j2xx.protocol;

import com.ftdi.j2xx.protocol.SpiSlaveEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SpiSlaveThread extends Thread {
   public static final int THREAD_DESTORYED = 2;
   public static final int THREAD_INIT = 0;
   public static final int THREAD_RUNNING = 1;
   private Queue<SpiSlaveEvent> a = new LinkedList();
   private Lock b = new ReentrantLock();
   private Object c = new Object();
   private Object d = new Object();
   private boolean e;
   private boolean f;
   private int g = 0;

   public SpiSlaveThread() {
      this.setName("SpiSlaveThread");
   }

   protected abstract boolean isTerminateEvent(SpiSlaveEvent var1);

   protected abstract boolean pollData();

   protected abstract void requestEvent(SpiSlaveEvent var1);

   public void run() {
      // $FF: Couldn't be decompiled
   }

   public boolean sendMessage(SpiSlaveEvent param1) {
      // $FF: Couldn't be decompiled
   }
}
