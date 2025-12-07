package org.firstinspires.ftc.teamcode.util.commands.actions;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.hardware.Robot;

// CLOCKWISE IS NEGATIVE IMU, positive boolean is clockwise, negative is counterclockwise
public class RotateRobotCommandPID implements Command {
    private final double absTargetAngle;
    private final boolean isClockwise;
    private final Robot robot;

    // PID coefficients (only P and D)
    private double kP = 0.015; // tune
    private double kD = 0.003; // tune

    private double prevError = 0;
    private long prevTime = 0;

    public RotateRobotCommandPID(double angle, boolean isClockwise, Robot robot, double kP, double kD) {
        this.absTargetAngle = Math.abs(angle);
        this.isClockwise = isClockwise;
        this.robot = robot;

        this.kD = kD;
        this.kP = kP;
    }

    public RotateRobotCommandPID(double angle, boolean isClockwise, Robot robot) {
        this.absTargetAngle = Math.abs(angle);
        this.isClockwise = isClockwise;
        this.robot = robot;
    }

    public void init() {
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.resetYaw();
        prevTime = System.nanoTime();
    }

    public void run() {
        double currentYaw = Math.abs(robot.getYaw());
        double error = absTargetAngle - currentYaw;

        long now = System.nanoTime();
        double dt = (now - prevTime) / 1e9;
        prevTime = now;

        double derivative = (error - prevError) / dt;
        prevError = error;

        double output = kP * error + kD * derivative;

        // Clamp output
        output = Math.max(-1.0, Math.min(1.0, output));

        if (isClockwise) {
            robot.rotateClockwise(Math.abs(output));
        } else {
            robot.rotateCounterClockwise(Math.abs(output));
        }
    }

    public boolean isDone() {
        return Math.abs(robot.getYaw()) >= absTargetAngle;
    }

    public void shutdown() {
        robot.setAllDrivePower(0);
        robot.resetYaw();
        robot.setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
