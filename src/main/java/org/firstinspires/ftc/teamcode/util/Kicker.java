package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.*;
public class Kicker {

    private final Servo engager;
    private final CRServo rotator;

    // FIXME: Get proper values for each of these positions
    private final double retractedPosition = 0.0d;
    private final double extendedPosition = 1.0/3.0;

    public Kicker(HardwareMap hardwareMap) {
        engager = hardwareMap.get(Servo.class,"kickerEngager");
        rotator = hardwareMap.get(CRServo.class,"kickerRotator");
        engager.setPosition(retractedPosition);
    }

    public void runEngager() {
        if (engager.getPosition() == retractedPosition) {
            engager.setPosition(extendedPosition);
        } else {
            engager.setPosition(retractedPosition);
            stop();
        }
    }

    public void runEngager(boolean goingBack) {
        if(goingBack) {
            engager.setPosition(retractedPosition);
        } else {
            engager.setPosition(extendedPosition);
        }
    }

    public void stop() {
        runRotator(0.0d);
    }

    public void runRotator(double power) {
        rotator.setPower(power); // FIXME: Reverse servo direction if need be to properly elevate artifacts (Do that in the constructor though)
    }

}
