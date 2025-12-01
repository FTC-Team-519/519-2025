package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.util.hardware.IntakeColorSensor;
import org.firstinspires.ftc.teamcode.util.hardware.Robot;

import static java.lang.Math.abs;

@TeleOp(name="BasicOpMode",group="")
public class BasicOpMode extends LinearOpMode {

    private final ElapsedTime runtime = new ElapsedTime();
    private Robot robot;

    @Override
    public void runOpMode() {
        boolean aPressed = false;
        boolean a2Pressed = false;

        robot = new Robot(hardwareMap);
        IntakeColorSensor.pieceType[] motif = new IntakeColorSensor.pieceType[3];
        motif[0] = IntakeColorSensor.pieceType.PURPLE;
        motif[1] = IntakeColorSensor.pieceType.GREEN;
        motif[2] = IntakeColorSensor.pieceType.PURPLE;

        waitForStart();
        runtime.reset();

        while(opModeIsActive()) {
            robot.updateRotatorStuff();

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
            telemetry.addData("Rotator power",gamepad1.right_trigger + " - " + gamepad1.left_trigger);

            if(gamepad1.a && !aPressed) {
                robot.fixRotatorArtifacts(motif);
                aPressed = true;
            }
            else if(!gamepad1.a && aPressed) {
                aPressed = false;
            }
            //couldn't you just use gamepad1.aWasReleased() or gamepad1.aWasPressed()

            if(gamepad2.b) {
                robot.BLASTTTTTTTTT();
            } else {
                robot.runOuttake(-gamepad2.left_stick_y);
            }

            if(gamepad2.a && !a2Pressed) {
                robot.changeKicking();
                a2Pressed = true;
            } else if(a2Pressed && !gamepad2.a) {
                a2Pressed = false;
            }

            if(robot.intakeAtPosition()) {
                robot.swapIntakeRunMode();
            }

            robot.runIntake(inPower);
            telemetry.addData("Piece Color",robot.pieceType());
            telemetry.addData("Rotator Position",robot.getRotatorPosition());
            telemetry.addData("Intake Position",robot.getIntakeMotor().getCurrentPosition());
            telemetry.addData("Rotator ACTUAL Position",robot.getRotatorEncoderPosition());
            telemetry.addData("Pieces Inside",robot.piecesIn());
            telemetry.addData("Distance From Piece",robot.distanceFromPiece());

            telemetry.update();
        }
    }
}
