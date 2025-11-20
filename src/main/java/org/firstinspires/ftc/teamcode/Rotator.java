package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.util.RobotMath;

import java.util.Arrays;

public class Rotator {

    public final double CLICKS_PER_ROTATION = 751.8;
    public final double GEAR_RATIO = 2.0;
    //might need to tweak
    public final double PRECISION = 15; //unit is clicks
    public static final double MAX_SPEED = 0.5;

    private final DcMotor motor;
    private final ColorRangeSensor colorSensor1;
    private final ColorRangeSensor colorSensor2;

    private final pieceType[] currentArtifacts = new pieceType[3];

    private int currentPosition = 0;

    public enum pieceType {
        NOT_THERE,
        GREEN,
        PURPLE
    }

    public void updateCurrentArtifacts() {
        if (getPosition() != currentPosition) {
            currentArtifacts[getPosition()] = getPieceColor();
            currentPosition = getPosition();
        }
    }

    //assuming proper alignment
    public boolean fixCurrentArtifacts(pieceType[] motif) {
        if (!Arrays.equals(currentArtifacts, motif)) {
            if (Arrays.equals(motif, rotatePieceArray(currentArtifacts))) {
                motor.setTargetPosition((int) (motor.getCurrentPosition() + getEncoderClicksPerRotation() / 3));
                return true;
            } else if (Arrays.equals(motif, rotatePieceArray(rotatePieceArray(currentArtifacts)))) {
                motor.setTargetPosition((int) (motor.getCurrentPosition() - getEncoderClicksPerRotation() / 3));
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
        motor = hardwareMap.get(DcMotor.class, "rotatorMotor");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setTargetPosition(0);
        colorSensor1 = hardwareMap.get(ColorRangeSensor.class, "colorSensor1");
        colorSensor2 = hardwareMap.get(ColorRangeSensor.class, "colorSensor2");
    }

    public pieceType getPieceColor() {
        if (!doesIntakeContainPiece(5.0)) {
            return pieceType.NOT_THERE;
        } else if (getCurrentHSV()[1] == 0) { // we see white
            return null;
        } else if (getCurrentHSV()[0] > 160) {
            return pieceType.GREEN;
        } else {
            return pieceType.PURPLE;
        }
    }

    public double distance() {
        return Math.min(colorSensor1.getDistance(DistanceUnit.INCH), colorSensor1.getDistance(DistanceUnit.INCH));
    }

    public pieceType[] getCurrentOrder() {
        return currentArtifacts;
    }

    public boolean doesIntakeContainPiece(double visibleDistanceInches) {
        return (distance() < visibleDistanceInches);
    }

    public float[] getCurrentHSV() {
        int[] rgb = getCurrentRGB();
        return rgbToHSV(rgb[0], rgb[1], rgb[2]);
    }

    public int[] getCurrentRGB() {
        int[] rgb = new int[3];
        rgb[0] = colorSensor1.red();
        rgb[1] = colorSensor1.blue();
        rgb[2] = colorSensor1.green();
        if (rgbToHSV(rgb[0], rgb[1], rgb[2])[1] == 0) { //color is white
            rgb[0] = colorSensor2.red();
            rgb[1] = colorSensor2.blue();
            rgb[2] = colorSensor2.green();
        }
        return rgb;
    }

    public int getCurrentAlpha() {
        int[] rgb = new int[3];
        int alpha = colorSensor1.alpha();
        rgb[0] = colorSensor1.red();
        rgb[1] = colorSensor1.blue();
        rgb[2] = colorSensor1.green();
        if (rgbToHSV(rgb[0], rgb[1], rgb[2])[1] == 0) { //color is white
            alpha = colorSensor2.alpha();
        }
        return alpha;
    }

    public float[] rgbToHSV(int r, int g, int b) {
        float[] ret = new float[3];
        Color.RGBToHSV(r * 8, g * 8, b * 8, ret);
        return ret;
    }

    public DcMotor getMotor() {
        return motor;
    }

    public void runMotor() {
        runMotor(0.0d);
    }

    public int getPosition() {
        return Math.floorMod((int) (Math.floor(motor.getCurrentPosition() /
                getEncoderClicksPerRotation() * 3)), 3);
    }

    public void runMotor(double power) {

        motor.setPower(RobotMath.clamp(power, -MAX_SPEED, MAX_SPEED));
    }

    //needs to be called continually every loop
    public void runMotorToPosition(double power) {
        if (Math.abs(motor.getCurrentPosition() - motor.getTargetPosition()) < 100){
            power *= 0.5;
        }
        if (isAtPosition()) {
            runMotor(0.0);
        } else if (motor.getCurrentPosition() > motor.getTargetPosition()) {
            runMotor(-power);
        } else {
            runMotor(power);
        }
    }

    public boolean isAtPosition() {
        return Math.abs(motor.getCurrentPosition() - motor.getTargetPosition()) < PRECISION;
    }

    public void resetEncoder() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double getEncoderClicksPerRotation() {
        return CLICKS_PER_ROTATION * GEAR_RATIO;
    }

    public void setDiskRotation(boolean rotate_clock) {
        double sector_size = (getEncoderClicksPerRotation() / 3);
        double current_sector = (motor.getCurrentPosition() / sector_size);
        if (rotate_clock) {
            motor.setTargetPosition((int) ((Math.floor(current_sector) + 1 + PRECISION / sector_size) * sector_size));
        } else {
            motor.setTargetPosition((int) ((Math.ceil(current_sector) - 1 - PRECISION / sector_size) * sector_size));
        }
        runMotor(); // will automatically stop b\c we are on RUN_USING_ENCODER
    }
}
