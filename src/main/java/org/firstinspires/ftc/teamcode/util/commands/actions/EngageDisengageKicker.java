package org.firstinspires.ftc.teamcode.util.commands.actions;

import org.firstinspires.ftc.teamcode.util.Kicker;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class EngageDisengageKicker implements Command {

    private final Kicker kicker;
    private final double power;
    private final boolean engaging;

    public EngageDisengageKicker(Kicker kicker, double power, boolean engaging) {
        this.kicker = kicker;
        this.power = power;
        this.engaging = engaging;
    }

    @Override
    public void init() {
        kicker.runEngager();
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
        return !kicker.isGoing();
    }

    @Override
    public void shutdown() {
    }
}
