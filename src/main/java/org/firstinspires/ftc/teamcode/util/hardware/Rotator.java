package org.firstinspires.ftc.teamcode.util.hardware;

import android.graphics.Color;
import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.teamcode.util.hardware.IntakeColorSensor.pieceType;
import org.firstinspires.ftc.teamcode.util.RobotMath;

import java.util.Arrays;

public class Rotator {

    public static final double CLICKS_PER_ROTATION = 751.8;
    public final double GEAR_RATIO = 2.0;
    //might need to tweak
    public final double PRECISION = 10; //unit is clicks
    public final double POWER_CUTTOFF = 0.05;
    public static final double MAX_SPEED = 0.3;

    //pid stuff
    public static final double POS_COEF = 0.007;
    public static final double DERIVATIVE_COEF = -0.40;

    private final DcMotor motor;

    public IntakeColorSensor getColorSensor1() {
        return colorSensor1;
    }

    private final IntakeColorSensor colorSensor1;

    private final TouchSensor magLimSwitch;

    public IntakeColorSensor getColorSensor2() {
        return colorSensor2;
    }

    private final IntakeColorSensor colorSensor2;

    private int currentPosition = 0;

    private boolean seen = false;

    public void setToNext() {
        motor.setTargetPosition(motor.getTargetPosition()+(int)(getEncoderClicksPerRotation()/3.0));
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }


    public pieceType[] rotatePieceArray(pieceType[] orig) {
        pieceType[] ans = new pieceType[3];
        ans[0] = orig[2];
        ans[1] = orig[0];
        ans[2] = orig[1];
        return ans;
    }

    public Rotator(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, "rotatorMotor");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setTargetPosition(0);
        colorSensor1 = new IntakeColorSensor(hardwareMap,"colorSensor1");
        colorSensor2 = new IntakeColorSensor(hardwareMap,"colorSensor2");
        magLimSwitch = hardwareMap.get(TouchSensor.class,"magLimSwitch");
    }

    public boolean isAligned() {return magLimSwitch.isPressed();}

    public pieceType getPieceColor() {
       return IntakeColorSensor.combine(colorSensor1.get_piece(), colorSensor2.get_piece());
    }

    public double distance() {
        return Math.min(colorSensor1.get_distance_inch(), colorSensor1.get_distance_inch());
    }

    public boolean doesIntakeContainPiece() {
        return getPieceColor() != IntakeColorSensor.pieceType.NOT_THERE;
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

    public int getEncoderPosition() {
        return motor.getCurrentPosition();
    }

    public void runMotor(double power) {
        if (Math.abs(power) > POWER_CUTTOFF) {
            motor.setPower(RobotMath.clamp(power, -MAX_SPEED, MAX_SPEED));
        }else{
            motor.setPower(0.0); // so that it breaks and doens't make a horrible screeching noise
        }
    }

    //needs to be called continually every loop
    public void runMotorToPosition(double power) {
        final int SLOWDOWN_CUTOFF = (int) (50+PRECISION);
        if (Math.abs(motor.getCurrentPosition() - motor.getTargetPosition()) < SLOWDOWN_CUTOFF){
            power *= Math.abs(motor.getCurrentPosition() - motor.getTargetPosition())/(double)SLOWDOWN_CUTOFF;
        }
        if (isAtPosition()) {
            runMotor(0.0);
        } else if (motor.getCurrentPosition() > motor.getTargetPosition()) {
            if (motor.getPower()>0.0){
                power *= 0.5;
            }
            runMotor(-power);
        } else {
            if (motor.getPower()<0.0){
                power *= 0.5;
            }
            runMotor(power);
        }
    }

    public void runMotorToPositionPID(){
        runMotorToPositionPID(POS_COEF, DERIVATIVE_COEF);
    }

    public void runMotorToPositionPID(double p_coef, double d_coef){
        double pos = motor.getTargetPosition() - motor.getCurrentPosition();
        double der = motor.getPower();
        double power = pos * p_coef + der * d_coef;
        runMotor(power);
    }

    public boolean isAtPosition() {
        return Math.abs(motor.getCurrentPosition() - motor.getTargetPosition()) < PRECISION && Math.abs(motor.getPower())<POWER_CUTTOFF;
    }

    public void resetEncoder() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double getEncoderClicksPerRotation() {
        return CLICKS_PER_ROTATION * GEAR_RATIO;
    }

    public void setDiskRotation(boolean rotate_clock) {
        if (rotate_clock) {
            setDiskRotation(1);
        } else {
            setDiskRotation(-1);
        }
    }

    public void setDiskRotation(int rotate_sections) {
        double sector_size = (getEncoderClicksPerRotation() / 3.0);
        double current_sector = (motor.getCurrentPosition() / sector_size);
        if (rotate_sections<0) {
            motor.setTargetPosition((int) ((Math.floor(current_sector + (PRECISION)/ sector_size) + Math.abs(rotate_sections)) * sector_size));
        } else {
            motor.setTargetPosition((int) ((Math.ceil(current_sector - (PRECISION) / sector_size) - Math.abs(rotate_sections) ) * sector_size));
        }
    }
}
