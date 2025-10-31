package org.firstinspires.ftc.teamcode.util.commands.actions;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class DriveSideways implements Command {
    private double distance;
    private Robot robot;

    //drive left is Negative <- mathew said this after he was wrong the first time
    public DriveSideways(double inches, Robot robot) {
        distance = inches*Robot.getCountsPerInchForDriveMotors();

        // init robot
        this.robot = robot;
    }

    public void init() {
        this.robot.setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.robot.setDriveTargetPosition((int)distance);
    }

    public void run() {
        if(distance>0) {
            this.robot.setStrafeRightPower(1.0);
        }
        else if(distance<0) {
            this.robot.setStrafeLeftPower(1.0);
        }
    }

    public void shutdown() {
        this.robot.setAllDrivePower(0.0d);
        this.robot.setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public boolean isDone() {
        if(this.robot.getLeftFrontDrive().getTargetPosition()>0) {
            return this.robot.getLeftFrontDrive().getCurrentPosition() >= this.robot.getLeftFrontDrive().getTargetPosition();
        }
        else {
            return this.robot.getLeftFrontDrive().getCurrentPosition() <= this.robot.getLeftFrontDrive().getTargetPosition();
        }
    }
}