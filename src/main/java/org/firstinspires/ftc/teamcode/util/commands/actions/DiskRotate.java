package org.firstinspires.ftc.teamcode.util.commands.actions;

import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class DiskRotate implements Command {

    private Robot robot;
    private boolean clockwise;
    private double power;

    public DiskRotate(Robot robot, boolean clockwise, double power) {
        this.robot = robot;
        this.clockwise = clockwise;
        this.power = power;
    }

    @Override
    public void init() {
        robot.getRotator().setDiskRotation(clockwise);
    }

    @Override
    public void run() {
        robot.getRotator().runMotorToPosition(power);
    }

    @Override
    public boolean isDone() {
        return robot.getRotator().isAtPosition();
    }

    @Override
    public void shutdown() {
        robot.getRotator().runMotor(0.0);
    }
}
