package org.firstinspires.ftc.teamcode.util.hardware;

import com.qualcomm.robotcore.hardware.*;
public class Kicker {

    private final Servo engager;
    private final CRServo rotator;

    private final double retractedPosition = 0.5d;
    private final double extendedPosition = 0.70d;

    private boolean going = false;

    public Kicker(HardwareMap hardwareMap) {
        engager = hardwareMap.get(Servo.class,"kickerEngager");
        rotator = hardwareMap.get(CRServo.class,"kickerRotator");
        engager.setPosition(retractedPosition);
        engager.setDirection(Servo.Direction.FORWARD);
        rotator.setDirection(CRServo.Direction.FORWARD);
        rotator.setPower(0.0d);
    }

    public void runEngager() {
        if (engager.getPosition() == retractedPosition) {
            engager.setPosition(extendedPosition);
            going = true;
        } else {
            engager.setPosition(retractedPosition);
            stop();
            going = false;
        }
    }

    public void runEngager(boolean goingBack) {
        if(goingBack) {
            engager.setPosition(retractedPosition);
            going = false;
        } else {
            engager.setPosition(extendedPosition);
            going = true;
        }
    }

    public boolean isGoing() {return going;}

    public void stop() {
        runRotator(0.0d);
    }

    public void runRotator(double power) {
        rotator.setPower(power);
    }

}
