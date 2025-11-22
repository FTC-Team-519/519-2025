package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.IntakeColorSensor.*;
import org.firstinspires.ftc.teamcode.util.Rotator;

public class Robot {
    private final DcMotor leftFrontDrive,leftBackDrive,rightFrontDrive,rightBackDrive,intakeMotor;

    IMU imu;

    public Rotator getRotator() {
        return rotator;
    }

    private final Rotator rotator;

    private final RobotCamera camera;

    public Outtake getOuttake() {
        return outtake;
    }

    private final Outtake outtake;

    public Kicker getKicker() {
        return kicker;
    }

    private final Kicker kicker;

    public Robot(HardwareMap hardwareMap) {
        leftFrontDrive = hardwareMap.get(DcMotor.class,"leftFront");
        leftBackDrive = hardwareMap.get(DcMotor.class,"leftBack");
        rightFrontDrive = hardwareMap.get(DcMotor.class,"rightFront");
        rightBackDrive = hardwareMap.get(DcMotor.class,"rightBack");
        intakeMotor = hardwareMap.get(DcMotor.class,"intakeMotor");

        outtake = new Outtake(hardwareMap);
        rotator = new Rotator(hardwareMap);
        kicker = new Kicker(hardwareMap);
        camera = new RobotCamera(hardwareMap);

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);

        intakeMotor.setTargetPosition(0);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rotator.getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotator.resetEncoder();

        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.DOWN, RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD)));
    }

    public void runOuttake(double power) {outtake.runMotors(power);}

    public void BLASTTTTTTTTT() {
        runOuttake(1.0d);
    }

    public double distanceFromPiece() {return rotator.distance();}

    public double[] getDistancesFromAprilTag() {return camera.distances();}

    public int[] getIds() {return camera.getIDs();}

    public void stopStreaming() {camera.stopStreaming();}

    public void resumeStreaming() {camera.resumeStreaming();}

    public DcMotor getIntakeMotor() {
        return intakeMotor;
    }
    public DcMotor getRotatorMotor() {
        return rotator.getMotor();
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

    public void updateRotatorStuff() {
        rotator.updateCurrentArtifacts();
    }

    public boolean fixRotatorArtifacts(pieceType[] motif) {
        return rotator.fixCurrentArtifacts(motif);
    }

    public pieceType[] currentRotatorArtifacts() {
        return rotator.getCurrentArtifacts();
    }

    public int getRotatorEncoderPosition() {
        return rotator.getEncoderPosition();
    }

    public int getRotatorPosition() {
        return rotator.getPosition();
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
    public String pieceType() {
        return pieceType(getPieceType());
    }

    public String pieceType(pieceType p) {
        switch(p) {
            case NOT_THERE:
                return "Not there";
            case GREEN:
                return "Green";
            case PURPLE:
                return "Purple";
            default:
                return "I have no earthly idea";
        }
    }

    public String piecesIn() {
        String ans = "";
        for(pieceType p: currentRotatorArtifacts()) {
            if(p!=null) {
                ans += pieceType(p) + " ";
            } else {
                ans += "null ";
            }
        }
        return ans;
    }

    public pieceType getPieceType() {
        return rotator.getPieceColor();
    }

    public void engageDisengageKicker() {
        kicker.runEngager();
    }

    public void engageDisengageKicker(boolean goingBack) {
        kicker.runEngager(goingBack);
    }
    public void getReadyToKick() {
        kicker.runEngager(false);
        kicker.runRotator(0.5d); // FIXME: Get a good speed
    }

    public void changeKicking() {
        if(kicker.isGoing()) {
            stopKicking();
        } else {
            getReadyToKick();
        }
    }

    public void stopKicking() {
        kicker.runEngager(true);
        kicker.stop();
    }

    public void runKicker(double power) {
        kicker.runRotator(power);
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

    //IMU
    public YawPitchRollAngles getOrientation(){
        assert imu != null;
        return imu.getRobotYawPitchRollAngles();
    }

    public double getYaw(){
        return getOrientation().getYaw();
    }

    public void resetYaw(){
        imu.resetYaw();
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
