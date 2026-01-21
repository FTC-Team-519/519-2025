package org.firstinspires.ftc.teamcode.util.commands.actions;

import org.firstinspires.ftc.teamcode.util.hardware.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class AlignRotator implements Command {

    private final Robot robot;

    public AlignRotator(Robot robot) {
        this.robot = robot;
    }

    public void init() {}

    public void run() {
        robot.runRotatorMotor(-0.08d);
    }

    public boolean isDone() {
        return robot.isRotatorAligned();
    }

    public void shutdown() {
        robot.runRotatorMotor(0.0d);
        robot.getRotator().resetEncoder();
    }
}
