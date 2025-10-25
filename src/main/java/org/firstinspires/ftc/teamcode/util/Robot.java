package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.teamcode.Rotator;

public class Robot {
    private final DcMotor leftFrontDrive,leftBackDrive,rightFrontDrive,rightBackDrive,intakeMotor;

    private final Rotator rotator;

    public Robot(HardwareMap hardwareMap) {
        leftFrontDrive = hardwareMap.get(DcMotor.class,"leftFront");
        leftBackDrive = hardwareMap.get(DcMotor.class,"leftBack");
        rightFrontDrive = hardwareMap.get(DcMotor.class,"rightFront");
        rightBackDrive = hardwareMap.get(DcMotor.class,"rightBack");
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

    public double getHueValue() {
        return rotator.getCurrentHSV()[0];
    }

    public double[] getHSV() {
        return rotator.getCurrentHSV();
    }

    public double[] getRGB() {
        return rotator.getCurrentRGB();
    }

    public double getAlpha() {
        return rotator.getCurrentAlpha();
    }

    public DcMotor getLeftFrontDrive() {
        return leftFrontDrive;
    }

    public DcMotor getLeftBackDrive() {
        return leftBackDrive;
    }

    public DcMotor getRightFrontDrive() {
        return rightFrontDrive;
    }

    public DcMotor getRightBackDrive() {
        return rightBackDrive;
    }


    //CODE FROM LAST YEAR MAY NOT WORK
    public boolean isTargetPositionsForDriveMotorsSet(int position) {
        return(leftFrontDrive.getTargetPosition()==position && leftBackDrive.getTargetPosition()==position &&
                rightFrontDrive.getTargetPosition()==position && rightBackDrive.getTargetPosition()==position);
    }

    public static double getCountsPerInchForDriveMotors() {
        final double     COUNTS_PER_MOTOR_REV    = 384.5  ;    // eg: Using 5203 Yellowjacket 435 RPM w/ given encoder
        final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // Simple Bevel Gear ratio is 2:1
        final double     WHEEL_DIAMETER_INCHES   = 4;     // For figuring circumference
        return (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math.PI);
    }

    public void setDriveMode(DcMotor.RunMode mode) {
        leftFrontDrive.setMode(mode);
        rightFrontDrive.setMode(mode);
        leftBackDrive.setMode(mode);
        rightBackDrive.setMode(mode);
    }

    public void setLeftDrivePower(double power) {
        leftFrontDrive.setPower(power);
        leftBackDrive.setPower(power);
    }

    public void setRightDrivePower(double power) {
        rightFrontDrive.setPower(power);
        rightBackDrive.setPower(power);
    }

    public void setAllDrivePower(double power) {
        setLeftDrivePower(power);
        setRightDrivePower(power);
    }

    public void setStrafeLeftPower(double power){
        leftFrontDrive.setPower(-power);
        leftBackDrive.setPower(power);
        rightFrontDrive.setPower(power);
        rightBackDrive.setPower(-power);
    }

    public void setStrafeRightPower(double power){
        leftFrontDrive.setPower(power);
        leftBackDrive.setPower(-power);
        rightFrontDrive.setPower(-power);
        rightBackDrive.setPower(power);
    }

    public void setDriveTargetPosition(int position) {
        leftFrontDrive.setTargetPosition(position);
        rightFrontDrive.setTargetPosition(position);
        leftBackDrive.setTargetPosition(position);
        rightBackDrive.setTargetPosition(position);
    }

    public void rotateClockwise(double speed) {
        leftFrontDrive.setPower(speed);
        rightFrontDrive.setPower(-speed);
        leftBackDrive.setPower(speed);
        rightBackDrive.setPower(-speed);
    }

    public void rotateCounterClockwise(double speed) {
        leftFrontDrive.setPower(-speed);
        rightFrontDrive.setPower(speed);
        leftBackDrive.setPower(-speed);
        rightBackDrive.setPower(speed);
    }




    public boolean atDriveTargetPosition(boolean isGoingForward) {
        if (isGoingForward) {
            return atDriveTargetPositionForward();
        }
        else {
            return atDriveTargetPositionBackward();
        }
    }

    public boolean atDriveTargetPositionForward() {
        return(leftFrontDrive.getCurrentPosition() >= leftFrontDrive.getTargetPosition() &&
                rightFrontDrive.getCurrentPosition() >= rightFrontDrive.getTargetPosition() &&
                leftBackDrive.getCurrentPosition() >= leftBackDrive.getTargetPosition() &&
                rightBackDrive.getCurrentPosition() >= rightBackDrive.getTargetPosition());
    }

    public boolean atDriveTargetPositionBackward() {
        return(leftFrontDrive.getCurrentPosition() <= leftFrontDrive.getTargetPosition() &&
                rightFrontDrive.getCurrentPosition() <= rightFrontDrive.getTargetPosition() &&
                leftBackDrive.getCurrentPosition() <= leftBackDrive.getTargetPosition() &&
                rightBackDrive.getCurrentPosition() <= rightBackDrive.getTargetPosition());
    }
    //END OF CODE FROM LAST YEAR

}
