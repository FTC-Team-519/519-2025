package org.firstinspires.ftc.teamcode.util.commands.actions;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.util.hardware.Robot;
import org.firstinspires.ftc.teamcode.util.RobotMath;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class CorrectRotationForAprilTag implements Command {

    private Robot robot;

    private double[] distance = new double[4];

    private boolean seesTag = true;

    private final double YAW_EPSILON;

    private final int MOTOR_EPSILON = 5;

    private boolean ran = false;

    public CorrectRotationForAprilTag(Robot robot, double yaw_epsilon) {
        this.robot = robot;
        this.YAW_EPSILON = yaw_epsilon;
    }

    public CorrectRotationForAprilTag(Robot robot) {
        this.robot = robot;
        this.YAW_EPSILON = 1.0d;
    }

    @Override
    public void init() {
        distance = robot.getDistancesFromAprilTag();
        robot.setRotationClockwiseReset(0);
        robot.resumeStreaming();
    }

    @Override
    public void run() {

        double[] getDists = robot.getDistancesFromAprilTag();

        if(getDists[1]>1) {
            distance = robot.getDistancesFromAprilTag();
            int ticks = RobotMath.angleToTicks(distance[3]);
            if(!ran) {
                robot.setRotationClockwiseReset(ticks);
                ran = true;
            } else {
                robot.setRotationClockwise(ticks);
            }
            seesTag = true;
        } else {
            seesTag = false;
        }

        robot.setAllDrivePower(1.0d);

    }

    @Override
    public boolean isDone() {
        return (seesTag && Math.abs(distance[3])<YAW_EPSILON) || (!seesTag && robot.atDriveTargetPosition(MOTOR_EPSILON));
    }

    @Override
    public void shutdown() {
        robot.setAllDrivePower(0.0);
        robot.setDriveMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDriveMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        robot.setAllDrivePower(0.0);
    }
}
