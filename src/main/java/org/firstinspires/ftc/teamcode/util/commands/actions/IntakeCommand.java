package org.firstinspires.ftc.teamcode.util.commands.actions;

import org.firstinspires.ftc.teamcode.util.hardware.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class IntakeCommand implements Command {

    private final TimerCommand timeout_seconds;
    private final double power;
    private final Robot robot;

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
        return timeout_seconds.isDone();//return robot.getRotator().doesIntakeContainPiece() || timeout_seconds.isDone();
    }

    @Override
    public void shutdown() {
        robot.runIntake(0);
    }
}
