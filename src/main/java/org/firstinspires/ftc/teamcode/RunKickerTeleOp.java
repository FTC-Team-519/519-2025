package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.*;

import static java.lang.Math.abs;

@TeleOp(name="RunKickerTeleOp",group="")
public class RunKickerTeleOp extends LinearOpMode {

    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        Servo engager = hardwareMap.get(Servo.class,"kickerEngager");
        CRServo rotator = hardwareMap.get(CRServo.class,"kickerRotator");

        waitForStart();
        runtime.reset();

        while(opModeIsActive()) {
            if(gamepad1.a) {
                engager.setPosition(0.75d);
            } else {
                engager.setPosition(0.5d);
            }
        }
    }
}

