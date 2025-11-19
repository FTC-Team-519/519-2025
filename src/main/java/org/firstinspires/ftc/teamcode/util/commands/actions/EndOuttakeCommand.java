package org.firstinspires.ftc.teamcode.util.commands.actions;

import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class EndOuttakeCommand implements Command {

    private final Robot robot;

    public EndOuttakeCommand(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        robot.runOuttake(0);
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
