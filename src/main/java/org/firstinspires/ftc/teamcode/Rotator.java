package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

public class Rotator {

    private final CRServo crServo;
    private final AnalogInput encoder;
    private final ColorSensor colorSensor;



    public Rotator() {
        crServo = hardwareMap.get(CRServo.class,"rotatorServo");
        encoder = hardwareMap.get(AnalogInput.class,"rotatorEncoder");
        colorSensor = hardwareMap.get(ColorSensor.class,"rotatorColorSensor");
    }

    public String getCurrentColor() {
        if(colorSensor.red()==0) {}
        return "";
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

    // FIXME: Find range of getVoltage and make this better based on it
    public double getCurrentAngle() {return encoder.getVoltage() * 360;}

}
