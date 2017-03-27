package com.ftdi.j2xx.protocol;

import android.os.Handler;
import android.util.Log;
import com.ftdi.j2xx.protocol.SpiSlaveEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SpiSlaveThread extends Thread {
   public static final int THREAD_INIT = 0;
   public static final int THREAD_RUNNING = 1;
   public static final int THREAD_DESTORYED = 2;
   private Handler m_pUIHandler;
   private Queue m_pMsgQueue = new LinkedList();
   private Lock m_pMsgLock = new ReentrantLock();
   private Object m_pSendWaitCond = new Object();
   private Object m_pResponseWaitCond = new Object();
   private boolean m_bSendWaitCheck;
   private boolean m_bResponseWaitCheck;
   private int m_iThreadState = 0;

   protected abstract boolean pollData();

   protected abstract void requestEvent(SpiSlaveEvent var1);

   protected abstract boolean isTerminateEvent(SpiSlaveEvent var1);

   public SpiSlaveThread() {
      this.setName("SpiSlaveThread");
   }

   public boolean sendMessage(SpiSlaveEvent event) {
      while(this.m_iThreadState != 1) {
         try {
            Thread.sleep(100L);
         } catch (InterruptedException var6) {
            ;
         }
      }

      this.m_pMsgLock.lock();
      if(this.m_pMsgQueue.size() > 10) {
         this.m_pMsgLock.unlock();
         Log.d("FTDI", "SpiSlaveThread sendMessage Buffer full!!");
         return false;
      } else {
         this.m_pMsgQueue.add(event);
         Object var2;
         if(this.m_pMsgQueue.size() == 1) {
            var2 = this.m_pSendWaitCond;
            synchronized(this.m_pSendWaitCond) {
               this.m_bSendWaitCheck = true;
               this.m_pSendWaitCond.notify();
            }
         }

         this.m_pMsgLock.unlock();
         if(event.getSync()) {
            var2 = this.m_pResponseWaitCond;
            synchronized(this.m_pResponseWaitCond) {
               this.m_bResponseWaitCheck = false;

               while(!this.m_bResponseWaitCheck) {
                  try {
                     this.m_pResponseWaitCond.wait();
                  } catch (InterruptedException var4) {
                     this.m_bResponseWaitCheck = true;
                  }
               }
            }
         }

         return true;
      }
   }

   public void run() {
      boolean bQuit = false;
      this.m_iThreadState = 1;

      while(!Thread.interrupted() && !bQuit) {
         this.pollData();
         this.m_pMsgLock.lock();
         if(this.m_pMsgQueue.size() <= 0) {
            this.m_pMsgLock.unlock();
         } else {
            SpiSlaveEvent event = (SpiSlaveEvent)this.m_pMsgQueue.peek();
            this.m_pMsgQueue.remove();
            this.m_pMsgLock.unlock();
            this.requestEvent(event);
            if(event.getSync()) {
               Object var3 = this.m_pResponseWaitCond;
               synchronized(this.m_pResponseWaitCond) {
                  while(this.m_bResponseWaitCheck) {
                     try {
                        Thread.sleep(100L);
                     } catch (InterruptedException var5) {
                        bQuit = true;
                     }
                  }

                  this.m_bResponseWaitCheck = true;
                  this.m_pResponseWaitCond.notify();
               }
            }

            bQuit = this.isTerminateEvent(event);
         }
      }

      this.m_iThreadState = 2;
   }
}
