package org.firstinspires.ftc.teamcode.util.commands.actions;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.RobotMath;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;

public class CorrectForAprilTag implements Command {


    private Command seq;

    private final Robot robot;

    public CorrectForAprilTag(Robot robot) {
        this.robot = robot;
        double[] distance = robot.getDistancesFromAprilTag();
        if(distance[1]>1) { // if we actually have a read
//            seq = new SequentialCommandGroup(
//                    new CorrectRotationForAprilTag(robot),
//                    new DriveInDirection(-distance[0], 1.0/4 * Math.PI,robot)
//            );
            seq = new CorrectRotationForAprilTag(robot);
        } else {
            seq = new FinishedCommand();
        }
    }

    @Override
    public void init() {
        seq.init();
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
