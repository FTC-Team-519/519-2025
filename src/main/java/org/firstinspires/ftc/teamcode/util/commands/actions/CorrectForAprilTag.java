package org.firstinspires.ftc.teamcode.util.commands.actions;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.RobotMath;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class CorrectForAprilTag implements Command {


    private double[] distance;

    private final double X_EPSILON = 5.0;
    private final double YAW_EPSILON = 1.0;

    private final Robot robot;

    public CorrectForAprilTag(Robot robot1) {
        robot = robot1;
        distance = robot.getDistancesFromAprilTag();
    }

    @Override
    public void init() {
        robot.setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.resumeStreaming();
    }

    @Override
    public void run() {
        double[] powers = RobotMath.motorPowers(distance[0],distance[3]);

        double lF = powers[0];
        double rF = powers[1];
        double lB = powers[2];
        double rB = powers[3];

        robot.setLeftFrontPower(lF);
        robot.setRightFrontPower(rF);
        robot.setLeftBackPower(lB);
        robot.setRightBackPower(rB);

        distance = robot.getDistancesFromAprilTag();
    }

    @Override
    public boolean isDone() {
        return Math.abs(distance[0])<X_EPSILON && Math.abs(distance[3])<YAW_EPSILON;
    }

    @Override
    public void shutdown() {
        robot.setAllDrivePower(0.0);
        robot.setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}
