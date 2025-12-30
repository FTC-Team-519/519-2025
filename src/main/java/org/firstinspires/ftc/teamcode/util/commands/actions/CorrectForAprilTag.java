package org.firstinspires.ftc.teamcode.util.commands.actions;

import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.util.hardware.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class CorrectForAprilTag implements Command {


    private Command seq;

    private final Robot robot;

    public CorrectForAprilTag(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        double[] distance = robot.getDistancesFromAprilTag();
        if(distance[1]>1) { // if we actually have a read
            Command rotateRobot = new RotateRobotCommandPID(distance[3],robot);
            // note this is faulty if the distance is less than 1, but we should never get that close while still being able to read
            if(distance[0]<0) {
                seq = new SequentialCommandGroup(
                        rotateRobot,
                        new DriveInDirection(-distance[0], 1.0 / 4 * Math.PI, robot)
                );
            } else {
                seq = new SequentialCommandGroup(
                        rotateRobot,
                        new DriveInDirection(-distance[0], - 3.0 / 4 * Math.PI,robot)
                );
            }
        } else {
            seq = new FinishedCommand();
        }
    }

    @Override
    public void run() {
        seq.run();
    }

    @Override
    public boolean isDone() {
        return seq.isDone();
    }

    @Override
    public void shutdown() {
        seq.shutdown();
    }

}
