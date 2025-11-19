package org.firstinspires.ftc.teamcode.util.commands.actions;

import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class StartOuttakeCommand implements Command {

    private final Robot robot;
    private final double power;

    public StartOuttakeCommand(Robot robot, double power) {
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
