package org.firstinspires.ftc.teamcode.util.hardware;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class AxonServo {

    private final Servo servo;

    private final AnalogInput position;

    public AxonServo(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class,"axon");
        position = hardwareMap.get(AnalogInput.class,"axonEncoder");
    }

    public void setPosition(double pos) {
        servo.setPosition(pos);
    }

    public void setDirection(Servo.Direction direction) {
        servo.setDirection(direction);
    }

    public double getPosition() {
        return servo.getPosition();
    }

    public double getTruePosition() {
        return position.getVoltage();
    }

    public void setPositionToDefault() { // needs to be tested, might not have to be negative, might have to be scaled
        servo.setPosition(-position.getVoltage());
    }
}
