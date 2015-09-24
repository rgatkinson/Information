package com.qualcomm.robotcore.util;

class Dimmer$2 implements Runnable {
    @Override
    public void run() {
        Dimmer.this.b.getWindow().setAttributes(Dimmer.this.c);
    }
}