package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.teamcode.Rotator;

public class Robot {
    private final DcMotor leftFrontDrive,leftBackDrive,rightFrontDrive,rightBackDrive,intakeMotor;

    private final Rotator rotator;

    public Robot(HardwareMap hardwareMap) {
        leftFrontDrive = hardwareMap.get(DcMotor.class,"leftFrontDrive");
        leftBackDrive = hardwareMap.get(DcMotor.class,"leftBackPower");
        rightFrontDrive = hardwareMap.get(DcMotor.class,"rightFrontDrive");
        rightBackDrive = hardwareMap.get(DcMotor.class,"rightBackDrive");
        intakeMotor = hardwareMap.get(DcMotor.class,"intakeMotor");

        rotator = new Rotator(
                hardwareMap.get(DcMotor.class,"rotatorMotor"),
                hardwareMap.get(ColorSensor.class,"colorSensor")
        );

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);

        intakeMotor.setTargetPosition(0);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setLeftFrontPower(double power) {
        leftFrontDrive.setPower(power);
    }
    public void setLeftBackPower(double power) {
        leftBackDrive.setPower(power);
    }
    public void setRightFrontPower(double power) {
        rightFrontDrive.setPower(power);
    }
    public void setRightBackPower(double power) {
        rightBackDrive.setPower(power);
    }

    public void runRotatorMotor(double power) {
        rotator.runMotor(power);
    }

    public boolean intakeAtPosition() {
        return intakeMotor.getMode().equals(DcMotor.RunMode.RUN_TO_POSITION) &&
                intakeMotor.getCurrentPosition()==intakeMotor.getTargetPosition();
    }

    public void runIntake(double inPower) {
        if(intakeMotor.getMode().equals(DcMotor.RunMode.RUN_TO_POSITION)) {
            intakeMotor.setPower(1.0d);
        } else {
            intakeMotor.setPower(inPower);
        }
    }

    public void swapIntakeRunMode() {
        if(intakeMotor.getMode().equals(DcMotor.RunMode.RUN_USING_ENCODER)) {
            intakeMotor.setTargetPosition(0);
            intakeMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeMotor.setPower(1.0d);
        } else {
            intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            intakeMotor.setPower(0.0d);
        }
    }
}
