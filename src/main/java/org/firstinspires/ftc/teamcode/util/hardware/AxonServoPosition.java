package org.firstinspires.ftc.teamcode.util.hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Servo.*;
import org.firstinspires.ftc.teamcode.util.RobotMath;

public class AxonServoPosition {
    // Encoder for servo position feedback
    private final AnalogInput servoEncoder;
    // Continuous rotation servo
    private final Servo servo;
    // Run-to-position mode flag
    private boolean rtp = true;

    private ConstantRotationDirection crDirection = ConstantRotationDirection.Stationary;
    // Current power applied to servo
    private double power;
    // Maximum allowed power
    private double maxPower;

    private double targetRotation;
    private double currentRotation;

    public ConstantRotationDirection getCrDirection() {
        return crDirection;
    }

    public void setCrDirection(ConstantRotationDirection crDirection) {
        this.crDirection = crDirection;
    }

    public enum ConstantRotationDirection {
        C,
        CC,
        Stationary,
    }

    // Initialization and debug fields
    public double STARTPOS;
    public int cliffs = 0;
    public double homeAngle;
    private boolean rotating = false;

    // region constructors

    // Basic constructor, defaults to FORWARD direction
    public AxonServoPosition(HardwareMap hardwareMap) {
        rtp = true;
        this.servo = hardwareMap.servo.get("diskRotator");
        servoEncoder = hardwareMap.analogInput.get("diskEncoder");
    }

    // Constructor with explicit direction
    public AxonServoPosition(HardwareMap hardwareMap, Servo.Direction direction) {
        this(hardwareMap);
        servo.setDirection(direction);
    }

    // Initialization logic for servo and encoder
    // endregion

    // Set servo direction
    public void setDirection(Direction direction) {
        servo.setDirection(direction);
    }

    // Enable or disable run-to-position mode
    public void setRtp(boolean rtp) {
        this.rtp = rtp;
    }

    // Get run-to-position mode state
    public boolean getRtp() {
        return rtp;
    }

    // Get current target rotation
    public double getTargetRotation() {
        return servo.getPosition();
    }

    // Increment target rotation by a value
    public void changeTargetRotation(double change) {
        servo.setPosition(RobotMath.trueMod(getTargetRotation() + change, 1.0));
    }

    // Set target rotation and reset PID
    public void setTargetRotation(double target) {
        servo.setPosition(target);
    }

    // Get current angle from encoder (in degrees)
    public double getCurrentRotation() {
        if (servoEncoder == null) return 0;
        return (servoEncoder.getVoltage() / 3.3);
    }

    // Check if servo is at target (default tolerance)
    public boolean isAtTarget() {
        return isAtTarget(1.0 / 360.0);
    }

    // Check if servo is at target (custom tolerance)
    public boolean isAtTarget(double tolerance) {
        return Math.abs(servo.getPosition() - getCurrentRotation()) < tolerance;
    }


    // Main update loop: updates rotation, computes PID, applies power
    public synchronized void update() {
        //rotation power kinda
        //prob not linear
        double rotation_power_kinda = 0.01; //FIXME:Tune this
        if (!rtp) {
            if (crDirection == ConstantRotationDirection.C) {
                changeTargetRotation(rotation_power_kinda);
            } else if (crDirection == ConstantRotationDirection.CC) {
                changeTargetRotation(-rotation_power_kinda);
            } else if (crDirection == ConstantRotationDirection.Stationary) {
                setTargetRotation(getCurrentRotation());
            }


        }
    }

    // Log current state for telemetry/debug

    public String log() {
        return String.format(
                "Current Volts: %.3f\n" +
                        "Current Angle: %.2f\n" +
                        "Target Rotation: %.2f\n" +
                        servoEncoder.getVoltage(),
                getCurrentRotation(),
                getTargetRotation()
        );
    }

    // TeleOp test class for manual tuning and testing
    @TeleOp(name = "Pos. Rotation Axon Test", group = "test")
    public static class PAxonTest extends LinearOpMode {

        @Override
        public void runOpMode() throws InterruptedException {
            AxonServoPosition servo = new AxonServoPosition(hardwareMap);
            servo.setRtp(true);
            servo.setCrDirection(ConstantRotationDirection.Stationary);

            waitForStart();

            while (!isStopRequested()) {

                if (servo.getRtp()) {
                    if (gamepad1.dpadUpWasPressed()) {
                        servo.setTargetRotation(1.0);
                    } else if (gamepad1.dpadDownWasPressed()) {
                        servo.setTargetRotation(0.0);
                    } else if (gamepad1.dpadLeftWasPressed()){
                        servo.setTargetRotation(0.5);
                    } else if (gamepad1.dpadRightWasReleased()) {
                        servo.servo.setDirection(Direction.FORWARD);
                        servo.changeTargetRotation(0.05);
                    }
                } else {
                    servo.update();
                    // Manual controls for target and PID tuning
                    if (gamepad1.dpad_up) {
                        servo.setCrDirection(ConstantRotationDirection.C);
                    } else if (gamepad1.dpad_down) {
                        servo.setCrDirection(ConstantRotationDirection.CC);
                    } else {
                        servo.setCrDirection(ConstantRotationDirection.Stationary);
                    }
                }


                if(gamepad1.aWasPressed()){
                    servo.setRtp(!servo.getRtp());
                }

                telemetry.addData("volts", servo.servoEncoder.getVoltage());
                telemetry.addData("current angle", servo.getCurrentRotation());
                telemetry.addData("target angle", servo.getTargetRotation());
                telemetry.addData("rotate to position", servo.getRtp());
                telemetry.update();
            }
        }
    }
}