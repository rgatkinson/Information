package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.util.RobotLog;

private static class a implements Runnable
{
    private RuntimeException a;
    private boolean b;
    private final LinearOpMode c;
    
    public a(final LinearOpMode c) {
        this.a = null;
        this.b = false;
        this.c = c;
    }
    
    public boolean a() {
        return this.a != null;
    }
    
    public RuntimeException b() {
        return this.a;
    }
    
    public boolean c() {
        return this.b;
    }
    
    @Override
    public void run() {
        this.a = null;
        this.b = false;
        try {
            this.c.runOpMode();
        }
        catch (InterruptedException ex) {
            RobotLog.d("LinearOpMode received an Interrupted Exception; shutting down this linear op mode");
        }
        catch (RuntimeException a) {
            this.a = a;
        }
        finally {
            this.b = true;
        }
    }
}
