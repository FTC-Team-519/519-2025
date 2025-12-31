package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.util.hardware.Robot;

@TeleOp
public class OuttakePIDFTuning extends OpMode {

    private double P = 0;
    private double F = 0;

    private final double[] increments = {0.0001, 0.001, 0.01, 0.1, 1.0, 10.0};
    private int index = 0;

    private final double highTargetVelocity = 1580;
    private final double lowTargetVelocity = 1380;

    private double targetVelocity = highTargetVelocity;

    private double currentVelocity = targetVelocity;

    private Robot robot;

    public void init() {
        robot = new Robot(hardwareMap);
    }

    public void loop() {
        if(gamepad1.dpadUpWasPressed()) {
            F += increments[index];
        }
        if(gamepad1.dpadDownWasPressed()) {
            F -= increments[index];
        }
        if(gamepad1.dpadRightWasPressed()) {
            P += increments[index];
        }
        if(gamepad1.dpadLeftWasPressed()) {
            P -= increments[index];
        }

        robot.changeOuttakePIDFCoefficients(P, F);

        robot.getOuttake().setVelocity(targetVelocity);

        currentVelocity = robot.getOuttake().getLeftMotor().getVelocity();

        if(gamepad1.aWasPressed()) {
            targetVelocity = (highTargetVelocity + lowTargetVelocity) - targetVelocity;
        }

        if(gamepad1.bWasPressed()) {
            index = (index + 1) % increments.length;
        }

        telemetry();
    }

    public void telemetry() {
        telemetry.addData("P", P);
        telemetry.addData("F", F);
        telemetry.addData("Target Velocity", targetVelocity);
        telemetry.addData("Current Velocity", currentVelocity);
        telemetry.addData("Increment Velocity", increments[index]);
        telemetry.addData("error",targetVelocity-currentVelocity);
        telemetry.update();
    }
}
