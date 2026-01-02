package org.firstinspires.ftc.teamcode.util.commands.actions;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.util.hardware.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

// Uses encoders
public class DriveForward implements Command {
    private final double distance;
    private final Robot robot;
    private double power;

    public DriveForward(double inches, double power, Robot robot) {
        distance = inches*Robot.getCountsPerInchForDriveMotors()/2;

        // init robot
        this.robot = robot;
        this.power = power;
    }

    public void init() {
        this.robot.setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.robot.setDriveTargetPosition((int)distance);
    }
    //    @Override
    public void run() {
        if(distance>=0) {
            this.robot.setAllDrivePower(power);
        } else {
            this.robot.setAllDrivePower(-power);
        }
    }

    public void shutdown() {
        this.robot.setAllDrivePower(0.0);
        this.robot.setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    //    @Override
    public boolean isDone() {
        if(distance>=0) {
            return robot.atDriveTargetPosition(true);
        } else {
            return robot.atDriveTargetPosition(false);
        }
    }
}