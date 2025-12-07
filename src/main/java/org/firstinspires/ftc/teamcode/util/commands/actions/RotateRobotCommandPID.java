package org.firstinspires.ftc.teamcode.util.commands.actions;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.util.RobotMath;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.hardware.Robot;

// CLOCKWISE IS NEGATIVE IMU, positive boolean is clockwise, negative is counterclockwise
public class RotateRobotCommandPID implements Command {
    private final double targetAngle;
    private final Robot robot;

    // PID coefficients (only P and D)
    private double kP = 0.015; // tune
    private double kD = 0.003; // tune

    private double prevError = 0;
    private long prevTime = 0;

    public RotateRobotCommandPID(double angle, Robot robot, double kP, double kD) {
        this(angle, robot);

        this.kD = kD;
        this.kP = kP;
    }

    public RotateRobotCommandPID(double angle,Robot robot) {
        this.targetAngle = RobotMath.trueMod(angle + robot.getYaw() + 180.0, 360) - 180;
        this.robot = robot;
    }

    public void init() {
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.resetYaw();
        prevTime = System.nanoTime();
    }

    public void run() {
        double currentYaw = robot.getYaw();

        double error = Math.min(RobotMath.trueMod(targetAngle - currentYaw, 360.0), RobotMath.trueMod( currentYaw - targetAngle, 360.0));

        double derivative = Math.abs(this.robot.getLeftBackDrive().getPower());

        double output = kP * error + kD * derivative;

        // Clamp output
        output = Math.max(-1.0, Math.min(1.0, output));

        boolean clockwise = true;


        if (clockwise){
            robot.rotateClockwise(output);
        }else{
            robot.rotateClockwise(-output);
        }
    }

    public boolean isDone() {
        double currentYaw = robot.getYaw();

        double error = Math.min(RobotMath.trueMod(targetAngle - currentYaw, 360.0), RobotMath.trueMod( currentYaw - targetAngle, 360.0));
        return (error <= 5) && (Math.abs(robot.getLeftBackDrive().getPower()) < 0.05);
    }

    public void shutdown() {
        robot.setAllDrivePower(0);
        robot.resetYaw();
        robot.setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
