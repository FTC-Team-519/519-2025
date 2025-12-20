package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.util.hardware.Robot;

public abstract class OpModeBase extends OpMode {

    protected boolean isTimeToRun = true;

    protected Robot robot;



    @Override
    public void init() {
        robot = new Robot(hardwareMap);
        robot.resumeMotifStreaming();
    }

    @Override
    public abstract void loop();
}
