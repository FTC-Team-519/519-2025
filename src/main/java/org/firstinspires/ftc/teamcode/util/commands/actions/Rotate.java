package org.firstinspires.ftc.teamcode.util.commands.actions;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

//CLOCKWISE IS NEGATIVE IMU, positive boolean is clockwise, negative is counterclockwise
public class Rotate implements Command {
    private double absTargetAngle;
    private boolean isClockwise;
    private Robot robot;
    private double startSpeed;

    public Rotate(double angle, boolean isClockwise, double speed, Robot robot) {
        this.absTargetAngle = Math.abs(angle);
        this.isClockwise = isClockwise;
        startSpeed = speed;
        // init robot
        this.robot = robot;

    }

    public void init() {
        this.robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.resetYaw();

    }

    public void run() {
        if(!isClockwise) {
            if(Math.abs(robot.getYaw())+10 >= absTargetAngle) {
                robot.rotateCounterClockwise(startSpeed / 2);
            } else {
                robot.rotateCounterClockwise(startSpeed);
            }
        }
        else {
            if(Math.abs(robot.getYaw())+10 >= absTargetAngle){
                robot.rotateClockwise(startSpeed/2.0);
            } else {
                robot.rotateClockwise(startSpeed);
            }
        }
    }

    public void shutdown() {
        this.robot.setAllDrivePower(0.0d);
        robot.resetYaw();
        this.robot.setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public boolean isDone() {
        return (Math.abs(robot.getYaw()) >= absTargetAngle);
    }
}