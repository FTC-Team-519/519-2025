package org.firstinspires.ftc.teamcode.autos;

import org.firstinspires.ftc.teamcode.util.hardware.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.*;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;

public class ShootThreeArtifacts {
    public static Command init(Robot robot){
        final double shoot_power = 0.67;

        return new SequentialCommandGroup(
                new DriveInDirection(36 * 6.0/5.0,  3.0 * Math.PI / 4.0, robot),

                //first shot
                new OuttakeCommand(robot, shoot_power),
                new TimerCommand(0.75),
                new EngageDisengageKicker(robot, 1.0, true),
                new TimerCommand(0.75),
                new EngageDisengageKicker(robot, 1.0, false),
                new DiskRotatePID(robot, true),

                //second shot
                new OuttakeCommand(robot, shoot_power),
                new TimerCommand(0.75),
                new EngageDisengageKicker(robot, 1.0, true),
                new TimerCommand(0.75),
                new EngageDisengageKicker(robot, 1.0, false),
                new DiskRotatePID(robot, true),

                //third shot
                new OuttakeCommand(robot, shoot_power),
                new TimerCommand(0.75),
                new EngageDisengageKicker(robot, 1.0, true),
                new TimerCommand(0.75),
                new EngageDisengageKicker(robot, 1.0, false),
                new DiskRotatePID(robot, true),

                new OuttakeCommand(robot, 0.0)
        );
    }
}
