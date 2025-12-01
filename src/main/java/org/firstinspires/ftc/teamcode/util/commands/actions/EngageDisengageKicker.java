package org.firstinspires.ftc.teamcode.util.commands.actions;

import org.firstinspires.ftc.teamcode.util.hardware.Kicker;
import org.firstinspires.ftc.teamcode.util.hardware.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class EngageDisengageKicker implements Command {

    private final Kicker kicker;
    private final double power;
    private final boolean engaging;

    public EngageDisengageKicker(Robot robot, double power, boolean engaging) {
        this.kicker = robot.getKicker();
        this.power = power;
        this.engaging = engaging;
    }

    @Override
    public void init() {
        kicker.runEngager(!engaging);
    }

    @Override
    public void run() {
        if (engaging) {
            kicker.runRotator(power);
        } else {
            kicker.stop();
        }
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public void shutdown() {
    }
}
