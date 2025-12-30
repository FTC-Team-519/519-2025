package org.firstinspires.ftc.teamcode.util.commands.actions;

import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.hardware.Robot;

public class FixArtifacts implements Command {
    Command command = null;
    public FixArtifacts(Robot robot) {
        command = new DiskRotatePID(robot,robot.rotationsForMotif());
    }

    public void init() {
        command.init();
    }

    public void run() {
        command.run();
    }

    public boolean isDone() {
        return command.isDone();
    }

    public void shutdown() {
        command.shutdown();
    }
}
