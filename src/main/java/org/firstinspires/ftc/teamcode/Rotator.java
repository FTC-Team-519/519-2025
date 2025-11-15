package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Arrays;

public class Rotator {

    public final double CLICKS_PER_ROTATION = 751.8;
    public final double GEAR_RATIO = 2.0;

    private final DcMotor motor;
    private final ColorRangeSensor colorSensor;

    private boolean clockwise = true;

    private final pieceType[] currentArtifacts = new pieceType[3];

    private int currentPosition = 0;

    private turnModes turnMode;

    public enum turnModes {
        CONSTANT_MOVEMENT,
        RUN_TO_POSITION,
        RUN_ON_POWER
    }

    public enum pieceType {
        NOT_THERE,
        GREEN,
        PURPLE
    }

    public void updateCurrentArtifacts() {
        if(getPosition()!=currentPosition) {
            currentArtifacts[getPosition()] = getPieceColor();
            currentPosition = getPosition();
        }
    }

    public boolean fixCurrentArtifacts(pieceType[] motif) {
        if(!Arrays.equals(currentArtifacts, motif)) {
            if(Arrays.equals(motif,rotatePieceArray(currentArtifacts))) {
                setToPosition(motor.getCurrentPosition()+ getEncoderClicksPerRotation()/3);
                return true;
            } else if(Arrays.equals(motif,rotatePieceArray(rotatePieceArray(currentArtifacts)))) {
                setToPosition(motor.getCurrentPosition()- getEncoderClicksPerRotation()/3);
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    public pieceType[] rotatePieceArray(pieceType[] orig) {
        pieceType[] ans = new pieceType[3];
        ans[0] = orig[2];
        ans[1] = orig[0];
        ans[2] = orig[1];
        return ans;
    }
    public pieceType[] getCurrentArtifacts() {
        return currentArtifacts;
    }



    public Rotator(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class,"rotatorMotor");
        colorSensor = hardwareMap.get(ColorRangeSensor.class,"colorSensor");
        turnMode = turnModes.RUN_ON_POWER;
    }

    public pieceType getPieceColor() {
        if(!doesIntakeContainPiece(5.0)) {
            return pieceType.NOT_THERE;
        } else if(getCurrentHSV()[0]>160) {
            return pieceType.GREEN;
        } else {
            return pieceType.PURPLE;
        }
    }

    public double distance() {
        return colorSensor.getDistance(DistanceUnit.INCH);
    }

    public pieceType[] getCurrentOrder() {
        return currentArtifacts;
    }


    // this color sensor is placed at the intake (assumption)
    public boolean doesIntakeContainPiece(double visibleDistanceInches) {
        return(colorSensor.getDistance(DistanceUnit.INCH)<visibleDistanceInches);
    }

    public float[] getCurrentHSV() {
        return rgbToHSV(colorSensor.red(),colorSensor.blue(),colorSensor.green());
    }

    public double[] getCurrentRGB() {
        double[] rgb = new double[3];
        rgb[0] = colorSensor.red();
        rgb[1] = colorSensor.blue();
        rgb[2] = colorSensor.green();
        return rgb;
    }

    public double getCurrentAlpha() {
        return colorSensor.alpha();
    }

    public float[] rgbToHSV(int r, int g, int b) {
        float[] ret = new float[3];
        Color.RGBToHSV(r*8,g*8,b*8,ret);
        return ret;
    }

    public DcMotor getMotor() {
        return motor;
    }

    public void runMotor() {
        runMotor(0.0d);
    }

    public int getPosition() {
        return Math.floorMod((int)(Math.floor(motor.getCurrentPosition() /
                getEncoderClicksPerRotation() * 3)),3);
    }

    public void runMotor(double power) {
        switch (turnMode) {
            case RUN_ON_POWER:
                motor.setPower(power);
                break;
            case RUN_TO_POSITION:
                motor.setPower(1.0d);
                if(Math.abs(motor.getCurrentPosition()) > Math.abs(motor.getTargetPosition())) {
                    setToPower();
                }
                break;
            case CONSTANT_MOVEMENT:
                if(clockwise) {
                    motor.setPower(1.0d);
                    break;
                } else {
                    motor.setPower(-1.0d);
                    break;
                }
        }
    }

    public double getEncoderClicksPerRotation() {
        return CLICKS_PER_ROTATION * GEAR_RATIO;
    }

    public void setToConstant(boolean goRight) {
        clockwise = goRight;
        turnMode = turnModes.CONSTANT_MOVEMENT;
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setToPosition(double target) {
        turnMode = turnModes.RUN_TO_POSITION;
        motor.setTargetPosition((int)target);
        if (motor.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void setToPower() {
        turnMode = turnModes.RUN_ON_POWER;
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void rotateDisk(boolean rotate_clock){
        setToPosition(motor.getCurrentPosition() + (rotate_clock?1:-1) * getEncoderClicksPerRotation()/3);
        // we need to test whether rotating clockwise or counterclockwise is positive or negative
        runMotor(); // will automatically stop b\c we are on RUN_USING_ENCODER
    }
}
