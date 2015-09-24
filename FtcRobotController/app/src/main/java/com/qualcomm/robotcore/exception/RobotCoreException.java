package com.qualcomm.robotcore.exception;

public class RobotCoreException extends Exception
{
    private Exception a;
    
    public RobotCoreException(final String s) {
        super(s);
        this.a = null;
    }
    
    public RobotCoreException(final String s, final Exception a) {
        super(s);
        this.a = null;
        this.a = a;
    }
    
    public Exception getChainedException() {
        return this.a;
    }
    
    public boolean isChainedException() {
        return this.a != null;
    }
}
