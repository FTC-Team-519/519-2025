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

        rotator = new Rotator(hardwareMap);

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);

        intakeMotor.setTargetPosition(0);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rotator.getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotator.setToPower();
    }

    public DcMotor getLeftFrontDrive() {
        return leftFrontDrive;
    }
    public DcMotor getLeftBackDrive() {
        return leftBackDrive;
    }
    public DcMotor getRightBackDrive() {
        return rightBackDrive;
    }
    public DcMotor getRightFrontDrive() {
        return rightFrontDrive;
    }
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
        rotator.updateCurrentStuff();
    }

    public boolean fixRotatorStuff(Rotator.pieceType[] motif) {
        return rotator.fixCurrentStuff(motif);
    }

    public Rotator.pieceType[] currentRotatorStuff() {
        return rotator.getCurrentStuff();
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

    public double getHueValue() {
        return rotator.getCurrentHSV()[0];
    }

    public String pieceType() {
        return pieceType(getPieceType());
    }

    public String pieceType(Rotator.pieceType p) {
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
        for(Rotator.pieceType p:currentRotatorStuff()) {
            if(p!=null) {
                ans += pieceType(p) + " ";
            } else {
                ans += "null ";
            }
        }
        return ans;
    }

    public Rotator.pieceType getPieceType() {
        return rotator.getPieceColor();
    }

    public float[] getHSV() {
        return rotator.getCurrentHSV();
    }

    public double[] getRGB() {
        return rotator.getCurrentRGB();
    }

    public double getAlpha() {
        return rotator.getCurrentAlpha();
    }
}
