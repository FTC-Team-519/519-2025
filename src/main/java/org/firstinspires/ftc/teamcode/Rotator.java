package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

public class Rotator {

    private final DcMotor motor;
    private final ColorSensor colorSensor;

    private boolean goingRight = true;

    private double targetPosition;

    private turnModes turnMode = turnModes.RUN_TO_POSITION;

    public enum turnModes {
        CONSTANT_MOVEMENT,
        RUN_TO_POSITION,
        RUN_ON_POWER
    }



    public Rotator(DcMotor motor1, ColorSensor colorSensor1) {
        motor = motor1;
        colorSensor = colorSensor1;
    }


    //FIXME: Get proper hue values for artifacts
    public String getCurrentColor() {
        if(colorSensor.red()==0) {
        }
        return "";
    }

    public double[] getCurrentHSV() {
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

    public double[] rgbToHSV(double r, double g, double b) {
        double[] ans = new double[3];
        double hue,saturation,value;

        double maximum = Math.max(Math.max(r,g),b);
        double minimum = Math.min(Math.min(r,g),b);
        double delta = maximum - minimum;

        if(delta==0) {hue = 0;}
        else if(maximum==r) {hue = 60 * (((g-b)/(255*delta))%6);}
        else if(maximum==g) {hue = 60 * ((b-r)/(255*delta)+2);}
        else {hue = 60 * ((r-g)/(255*delta)+4);}

        if(maximum==0) {saturation = 0;}
        else {saturation = delta/(255*maximum);}

        value = maximum/255;
        ans[0] = hue;
        ans[1] = saturation;
        ans[2] = value;

        return ans;
    }

    public void runMotor(double power) {
        switch (turnMode) {
            case RUN_ON_POWER:
                motor.setPower(power);
            case RUN_TO_POSITION:
                motor.setPower(1.0d);
            case CONSTANT_MOVEMENT:
                if(goingRight) {
                    motor.setPower(1.0d);
                } else {
                    motor.setPower(-1.0d);
                }
        }
    }

    public void setToConstant(boolean direction) {
        goingRight = direction;
        turnMode = turnModes.CONSTANT_MOVEMENT;
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setToPosition(double target) {
        targetPosition = target;
        turnMode = turnModes.RUN_TO_POSITION;
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void setToPower() {
        turnMode = turnModes.RUN_ON_POWER;
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}
