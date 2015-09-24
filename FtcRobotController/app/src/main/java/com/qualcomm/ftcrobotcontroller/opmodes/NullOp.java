package com.qualcomm.ftcrobotcontroller.opmodes;

import java.util.Date;
import java.text.SimpleDateFormat;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class NullOp extends OpMode
{
    private ElapsedTime runtime;
    private String startDate;
    
    public NullOp() {
        this.runtime = new ElapsedTime();
    }
    
    @Override
    public void init() {
    }
    
    @Override
    public void init_loop() {
        this.startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        this.runtime.reset();
        this.telemetry.addData("Null Op Init Loop", this.runtime.toString());
    }
    
    @Override
    public void loop() {
        this.telemetry.addData("1 Start", "NullOp started at " + this.startDate);
        this.telemetry.addData("2 Status", "running for " + this.runtime.toString());
    }
}
