package org.firstinspires.ftc.teamcode.util.commands.actions;

import org.firstinspires.ftc.teamcode.util.hardware.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class OuttakeCommand implements Command {

    private final Robot robot;
    private final double power;

    public OuttakeCommand(Robot robot, double power) {
        this.robot = robot;
        this.power = power;
    }

    @Override
    public void init() {
        robot.runOuttake(power);
    }

    @Override
    public void run() {
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public void shutdown() {
    }
}
