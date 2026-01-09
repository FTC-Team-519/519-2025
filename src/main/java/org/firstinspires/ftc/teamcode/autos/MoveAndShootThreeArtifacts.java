package org.firstinspires.ftc.teamcode.autos;

import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.AlignRotator;
import org.firstinspires.ftc.teamcode.util.commands.actions.DriveInDirection;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.util.hardware.Robot;

public class MoveAndShootThreeArtifacts {
    public static Command init(Robot robot) {
        final double shoot_power = 0.65; //58 is good for teleop

        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new DriveInDirection(36 * 6.0 / 5.0, 3.0 * Math.PI / 4.0, robot),
                        new AlignRotator(robot)
                ),
                new ShootThreeArtifacts(robot, shoot_power)
        );
    }
}
