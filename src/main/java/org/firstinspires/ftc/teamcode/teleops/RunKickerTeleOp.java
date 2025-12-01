package org.firstinspires.ftc.teamcode.teleops;

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
            if(gamepad1.aWasPressed()) {
                engager.setPosition(0.75d);
            } else if(gamepad1.aWasReleased()){
                engager.setPosition(0.5d);
            } else {
                engager.setPosition(gamepad1.left_stick_y);
            }
        }
    }
}

