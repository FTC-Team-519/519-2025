package org.firstinspires.ftc.teamcode.util.commands.actions;

import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class IntakeCommand implements Command {

    private TimerCommand timeout_seconds;
    private double power;
    private Robot robot;

    public IntakeCommand(double timeout_seconds, double power, Robot robot) {
        this.robot = robot;
        this.power = power;
        this.timeout_seconds = new TimerCommand(timeout_seconds);
    }

    @Override
    public void init() {
        timeout_seconds.init();
    }

    @Override
    public void run() {
        robot.runIntake(power);
    }

    @Override
    public boolean isDone() {
        return robot.getRotator().doesIntakeContainPiece(5) || timeout_seconds.isDone();
    }

    @Override
    public void shutdown() {
        robot.runIntake(0);
    }
}
