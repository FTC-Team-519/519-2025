package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.util.Robot;

import static java.lang.Math.abs;

@TeleOp(name="BasicOpMode",group="")
public class BasicOpMode extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private Robot robot;

    @Override
    public void runOpMode() {
        boolean aPressed = false;

        robot = new Robot(hardwareMap);

        waitForStart();
        runtime.reset();

        while(opModeIsActive()) {

            double max;

            double axial   = -gamepad1.left_stick_y;
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;

            max = Math.max(abs(leftFrontPower), abs(rightFrontPower));
            max = Math.max(max, abs(leftBackPower));
            max = Math.max(max, abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            double inPower = gamepad1.right_stick_y;
            if(abs(inPower)>1) {
                inPower /= abs(inPower);
            }

            robot.setLeftFrontPower(leftFrontPower);
            robot.setRightFrontPower(rightFrontPower);
            robot.setLeftBackPower(leftBackPower);
            robot.setRightBackPower(rightBackPower);

            robot.runRotatorMotor(gamepad1.right_trigger-gamepad1.left_trigger);

            if(gamepad1.a && !aPressed) {
                robot.swapIntakeRunMode();
                aPressed = true;
            }
            else if(!gamepad1.a && aPressed) {
                aPressed = false;
            }

            if(robot.intakeAtPosition()) {
                robot.swapIntakeRunMode();
            }

            robot.runIntake(inPower);

            telemetry.addData("Hue Value",robot.getHueValue());
            double[] hsv = robot.getHSV();
            telemetry.addData("HSV Values",hsv[0] + " " + hsv[1] + " " + hsv[2] );
            double[] rgb = robot.getRGB();
            telemetry.addData("RGB Values",rgb[0] + " " + rgb[1] + " " + rgb[2]);
            telemetry.addData("Alpha Value",robot.getAlpha());

            telemetry.update();
        }
    }
}
