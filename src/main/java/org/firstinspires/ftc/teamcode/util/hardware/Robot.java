package org.firstinspires.ftc.teamcode.util.hardware;

import com.qualcomm.hardware.rev.*;
import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.robotcore.external.navigation.*;
import org.firstinspires.ftc.teamcode.util.hardware.IntakeColorSensor.*;

import java.util.Arrays;
//import org.firstinspires.ftc.teamcode.util.structs.ComparableCircularList;

public class Robot {
    private final DcMotor leftFrontDrive,leftBackDrive,rightFrontDrive,rightBackDrive,intakeMotor;

    IMU imu;

    public Rotator getRotator() {
        return rotator;
    }

//    private final ComparableCircularList<Double> cache; // cached x values from april tag detection
//    private final int length = 30; // amount of checks we want to cache

    private final Rotator rotator;

    private final RobotCamera camera;
    private final int cameraID = 0;

    private final RobotCamera motifCam;

    private pieceType[] motif = null;
    private pieceType[] pieces = new pieceType[3];
    private int currentPosition = 0;
    private final int motifCamID = 0;

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
        outtake.changePIDFCoefficients(Outtake.optimalP, Outtake.optimalF);

        rotator = new Rotator(hardwareMap);
        kicker = new Kicker(hardwareMap);
        camera = new RobotCamera(hardwareMap,"camera",cameraID);
        motifCam = new RobotCamera(hardwareMap,"motifCam",motifCamID);

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);

        intakeMotor.setTargetPosition(0);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rotator.getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotator.resetEncoder();

//        cache = new ComparableCircularList<>(length,0.0d);

        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.DOWN, RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD)));
    }

    public void runOuttake(double power) {outtake.runMotors(power);}

    public void changeOuttakePIDFCoefficients(double P, double F) {
        outtake.changePIDFCoefficients(P, F);
    }

    public boolean isRotatorAligned() {return rotator.isAligned();}

    public void BLASTTTTTTTTT() {
        runOuttake(1.0d);
    }

    public double distanceFromPiece() {return rotator.distance();}

    public int rotationsForMotif() {
        if(motif==null || Arrays.equals(motif,pieces)) {return rotator.getPosition();}
        else if(Arrays.equals(motif,rotate(pieces))) {return ((1+rotator.getPosition())%3);}
        else if(Arrays.equals(motif,rotate(rotate(pieces)))) {return ((2+rotator.getPosition())%3);}
        return 0;
    }

    private <T> T[] rotate(T[] orig) {
        T[] ans = orig.clone();
        ans[0] = orig[2];
        ans[1] = orig[0];
        ans[2] = orig[1];
        return ans;
    }

    public double[] getDistancesFromAprilTag() {
//        double[] ans = camera.distances();
//        cache.set(ans[0]);
        return camera.distances();
    }

//    public boolean isLeftFromAprilTag() {
//        return cache.getTotalComparison();
//    }

    public int[] getIds() {return camera.getIDs();}

    //FIXME: this code might crash the robot so I coded it out
    public void stopStreaming() {camera.stopStreaming();}

    public void resumeStreaming() {}//camera.resumeStreaming();
    //public void stopMotifStreaming() {motifCam.stopStreaming();}

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

    public pieceType[] getMotif() {return motif;}

    public void updateMotif() {
        if(motif==null) {
            pieceType[] detMotif = motifCam.getMotif();
            if (detMotif != null) {
                motif = detMotif;
                stopMotifStreaming();
            }
        }
    }

    public pieceType[] getArtifacts() {
        return pieces;
    }

    public void updateArtifacts() {
        if (rotator.getPosition() != currentPosition) {
            pieces[rotator.getPosition()] = rotator.getPieceColor();
            currentPosition = rotator.getPosition();
        }
    }

    public void stopMotifStreaming() {motifCam.stopStreaming();}

    public void resumeMotifStreaming() {motifCam.resumeStreaming();}

    public boolean doesIntakeContainPiece() {return rotator.doesIntakeContainPiece();}

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
        for(pieceType p: pieces) {
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
        kicker.runRotator(1.0d); // FIXME: Get a good speed
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

    public void setRotationClockwiseReset(int pos) {
        leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition()-pos);
        leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition()-pos);
        rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition()+pos);
        rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition()+pos);
        setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void setRotationClockwise(int pos) {
        leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition()-pos);
        leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition()-pos);
        rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition()+pos);
        rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition()+pos);
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




    public boolean atDriveTargetPosition(int epsilon) {
        return Math.abs(leftFrontDrive.getCurrentPosition()-leftFrontDrive.getTargetPosition())<=epsilon &&
                Math.abs(rightFrontDrive.getCurrentPosition()-rightFrontDrive.getTargetPosition())<=epsilon &&
                Math.abs(leftBackDrive.getCurrentPosition()-leftBackDrive.getTargetPosition())<=epsilon &&
                Math.abs(rightBackDrive.getCurrentPosition()-rightBackDrive.getTargetPosition())<=epsilon;
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
