package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class OpModeBase extends OpMode {

    protected boolean isTimeToRun = true;

    protected Robot robot;



    @Override
    public void init() {
        robot = new Robot(hardwareMap);
    }

    @Override
    public abstract void loop();
}
