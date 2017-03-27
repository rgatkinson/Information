package com.ftdi.j2xx.protocol;

public class SpiSlaveEvent {
   private int m_iEventType;
   private boolean m_bSync;
   private Object m_pArg0;
   private Object m_pArg1;
   private Object m_pArg2;

   public SpiSlaveEvent(int iEventType, boolean bSync, Object pArg0, Object pArg1, Object pArg2) {
      this.m_iEventType = iEventType;
      this.m_bSync = bSync;
      this.m_pArg0 = pArg0;
      this.m_pArg1 = pArg1;
      this.m_pArg2 = pArg2;
   }

   public Object getArg0() {
      return this.m_pArg0;
   }

   public void setArg0(Object arg) {
      this.m_pArg0 = arg;
   }

   public Object getArg1() {
      return this.m_pArg1;
   }

   public void setArg1(Object arg) {
      this.m_pArg1 = arg;
   }

   public Object getArg2() {
      return this.m_pArg2;
   }

   public void setArg2(Object arg) {
      this.m_pArg2 = arg;
   }

   public int getEventType() {
      return this.m_iEventType;
   }

   public void setEventType(int type) {
      this.m_iEventType = type;
   }

   public boolean getSync() {
      return this.m_bSync;
   }

   public void setSync(boolean bSync) {
      this.m_bSync = bSync;
   }
}
